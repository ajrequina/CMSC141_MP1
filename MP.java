/**
* Arjemariel J. Requina
* CMSC 141 : Machine Problem Assignment 1
* 
* Description : A program that asks an input file with C statements namely 
*  				variable declarations, function declarations, and function definitions 
*				and have an output file that states whether each snippet of code is 
*				valid definition or declaration or not.
* 
* Main Class : Parser
* 
* Constraints :
*   a. Usage of Regular Expressions are prohibited
*/


import java.io.*;
import java.util.*;



/**
* VAR AND VAR = false, VAR AND FUNC = false, FUNC AND VAR = true, FUNC AND FUNC = false
* The main class of the program. This contains the functions that does the checking of the 
* C statements. 
*/
class Parser {

    private static final String VOID = "void";
    private static final String INT = "int";
    private static final String CHAR = "char";
    private static final String FLOAT = "float";
    private static final String DOUBLE = "double";

    ArrayList<String> words;
    ArrayList<Stop> stop_words;
    ArrayList<Integer> visited;
    ArrayList<Integer> s_queue;
    ArrayList<State> states;
    Checker checker;
    FileHandler file_handler;
    String input_filename;
    String output_filename;
    Graph graph;
    String type;
    ArrayList<String> declared;
    int balance;
    ArrayList<String> gen_var;
    ArrayList<String> arr_var;
    public Parser(String input_filename, String output_filename){
        this.input_filename = input_filename;
        this.output_filename = output_filename;

        graph = new Graph();
        states = graph.states();
        checker = new Checker();
        file_handler = new FileHandler(input_filename, output_filename);
        visited = new ArrayList<>();
        s_queue = new ArrayList<>();
        s_queue.add(0);
        declared = new ArrayList<>();
        gen_var = new ArrayList<>();
        arr_var = new ArrayList<>();
        type = null;
        retrieveContents();
        balance = 0;

    }
    private void retrieveContents(){
        words = file_handler.retrieveContents();
        stop_words = file_handler.stop_words;
  
    }

    /**
    * ; :: MODIFIER || MODIFIER :: ;  || MODIFIER :: MODIFIER
    */

    public void run(){
        ArrayList<Integer> a = new ArrayList<>();
        String s = "";
        if(!stop_words.isEmpty()){
          if(stop_words.size() > 1){
            Stop start = stop_words.remove(0);
            Result result = new Result();
            boolean find_end = false;
            while(!stop_words.isEmpty()){
                Stop end = stop_words.remove(0);
                if(checker.isModifier1(end.detail) && start.detail.equals(";")){
                    start = end;
                    if(!stop_words.isEmpty()){
                        end = stop_words.remove(0);
                    }
                }
                while(!end.detail.equals("}") && !end.detail.equals(";") && !stop_words.isEmpty()){
                    if(!stop_words.isEmpty()){
                        end = stop_words.remove(0);
                    }
                }

                System.out.println(start.detail);
                System.out.println(end.detail);
                result = checkPath(start, end, s_queue);
                System.out.println(result.getResult());
                if(checker.isModifier1(start.detail) && (end.detail.equals(";") || end.detail.equals("}"))){
                  s += result.getResult() + "\n";
                }
                start = end;
            }
            file_handler.writeResults(s);
            System.out.println(s);   
          } else { 
            file_handler.writeResults("invalid variable declaration" + "\n");
          }
        } 
        //Stop start = stop_words.remove(0);

    }
    

    public Result checkPath(Stop start, Stop end, ArrayList<Integer> s_queue){
      String var_name = "";
      Result result = new Result();
      int vis_index = -1;
      ArrayList<Integer> next = new ArrayList<>();
      ArrayList<String> declared = new ArrayList<>();
      boolean stop_checking = false;
      int i = start.index;
      String ass_var = "";
      State curr = null;
      String word = null;
      boolean valid = false;
      while(!s_queue.isEmpty()){
        boolean skip = false;
        int idx = s_queue.remove(0);

        // if(word.equals(";") || word.equals("}") && i == words.size() - 1){
        //     if(curr.next.contains(9)){
        //         System.out.println("VARIABLE");
        //         break;
        //     } if(curr.next.contains(34)){
        //         break;
        //     }
            
        // }

        System.out.println("-- WORD : " + word + " --");
        System.out.println("-- STATE : " + idx);
        if(idx > -1){
            curr = states.get(idx);
            word = words.get(i);
            if(idx == 0){
                if(checker.isModifier1(word)){
                    System.out.println(word + " : MODIFIER11");
                    type = word;
                    skip = true;
                }
            } if(idx == 1 || idx == 2 || idx == 3 || idx == 28 || // ALPHA, NUM, _ : variables
                     idx == 29 || idx == 30|| idx == 57|| idx == 58 || idx == 59) {
                if(idx == 1 || idx == 28 || idx == 57){
                    if(checker.isAlpha(word)){
                        System.out.println(word + " : ALPHABET");
                        skip = true;
                    }
                } else if(idx == 2 || idx == 29 || idx == 58){
                    if(word.equals(curr.detail)){
                        System.out.println(word + " : _");
                        skip = true;
                    }
                } else if(idx == 3 || idx == 30 || idx == 59){
                    if(checker.isNum(word)){
                        System.out.println(word + " : NUM");
                        skip = true;
                    }
                }
                if(skip){
                    var_name = var_name + word;
                    skip = true;
                }
                
            } if(idx == 5 || idx == 19 || idx == 21 ||  // DECLARED
                      idx == 24|| idx == 45 || idx == 62 ) {
                int prev = checker.getPrevious(visited.size() - 1, visited);
                if(prev == 4){
                    if(checker.isValidVar(word)){
                        ass_var = ass_var + word;
                        skip = true;
                    }
                } else if(prev == 17){
                    if(checker.isValidVar(word)){
                        ass_var = ass_var + word;
                        skip = true;
                    }
                } else  if(prev == 20 || prev == 8){
                    if(checker.isValidVar(word)){
                        ass_var = ass_var + word;
                        skip = true;
                    }
                } else  if(prev == 23){
                    if(checker.isValidVar(word)){
                        ass_var = ass_var + word;
                        skip = true;
                    }
                } else if(prev == 35 || prev == 50 || prev == 55){
                    if(checker.isValidVar(word)){
                        ass_var = ass_var + word;
                        skip = true;
                    }
                } else if(prev == 60 || prev == 61){
                    if(checker.isValidVar(word)){
                        ass_var = ass_var + word;
                        skip = true;
                    }
                } 
               
            } if(idx == 6 || idx == 12 || idx == 40 ) { // NUM
                int prev = checker.getPrevious(visited.size() - 1, visited);
                if(idx == 6){
                    if(checker.isNum(word)){
                        skip = true;
                        System.out.println(word + " : NUM");
                    }
                } else if(idx == 12){
                    if(checker.isNum(word)){
                        skip = true;
                        System.out.println(word + " : NUM");
                    }
                } else if(idx == 40){
                    if(checker.isNum(word)){
                        skip = true;
                        System.out.println(word + " : NUM");
                    }
                }

            } if(idx == 8 || idx == 7 || idx == 32 || idx == 60) { // Store variable names
                if(word.equals(curr.detail)){
                    if(idx == 8 || idx == 32){
                        if(!checker.isDeclared(var_name,result.declared)){
                            result.arr_dec.add(var_name);
                            result.declared.add(var_name);
                            skip = true;
                            var_name = "";
                        } 
                    }
                    int prev = checker.getPrevious(visited.size() - 1, visited);
                    if(prev == 31){ 
                        if(!checker.isDeclared(var_name, result.declared)){
                            result.declared.add(var_name);
                            skip = true;
                            var_name = "";
                        }
                    } else if(prev == 57 || prev == 58 || prev == 59){
                       if(!checker.isDeclared(var_name, result.declared)){
                            result.declared.add(var_name);
                            skip = true;
                            var_name = "";
                        }
                    }
                }
            }  if(idx == 35){ // Curly Open
                if(word.equals(curr.detail)){
                    skip = true;
                    result.type = Result.FUNCN;
                    result.subtype = Result.DEFN;
                }
                
            } if(idx == 26){ // Paren Open
                System.out.println(curr.detail + " -- " + curr.index);
                if(word.equals(curr.detail)){
                    skip = true;
                    result.type = Result.FUNCN;
                    result.subtype = Result.DECN;
                    
                }
            } if(idx == 34) { // Paren Close
                if(word.equals(curr.detail)){
                    skip = true;
                    result.type = Result.FUNCN;
                    result.subtype = Result.DECN;
                }
            } if(idx == 15 || idx == 43) { // One Size
                if(checker.hasOneSize(word)){
                    skip = true;
                }
            } if(idx == 9 || idx == 36 || idx == 53 ||  // End }, ;
                      idx == 73|| idx == 75) {
                if(word.equals(curr.detail)){
                    if(idx == 9) {
                        if(checker.isVisited(4, visited)){
                            skip = true;
                        }
                        System.out.println(var_name);
                        if(!checker.isDeclared(var_name, result.declared)){
                            result.declared.add(var_name);
                            var_name = "";
                        } else {
                            result.validity = Result.INVALID;
                        }
                    } else {
                        skip = true;
                    }
                    if(idx == 9){
                        result.validity = Result.VALID;
                        System.out.println(result.getResult());
                        if(ass_var.equals("")){
                            if(checker.isDeclared(ass_var, result.declared)){
                                skip = true;
                                ass_var = "";
                            }
                            ass_var = "";
                        }
                    } else if(idx == 36){
                        result.validity = Result.VALID;
                        result.type = Result.FUNCN;
                        result.subtype = Result.DECN;
                        skip = true;
                    } else if(idx == 75){
                        result.validity = Result.VALID;
                        result.type = Result.FUNCN;
                        result.subtype = Result.DEFN;
                        skip = true;
                    } else  if(idx == 53 || idx == 73){
                        result.validity = Result.VALID;
                        result.type = Result.FUNCN;
                        result.subtype = Result.DEFN;
                        skip = true;
                    }
                }
               
            } if(idx == 7 || idx == 18 || idx == 22 || idx == 72){ // Check Assigned
                if(!ass_var.equals("")){
                    System.out.println(ass_var);
                    if(checker.isDeclared(ass_var, result.declared) && !checker.isDeclared(ass_var, result.arr_dec)){
                        skip = true;
                        ass_var = "";
                    } else {
                        result.validity = Result.INVALID;
                    }
                    ass_var = "";
                } 
                if(idx == 7){
                    skip = true;
                } else if(idx == 18){
                    if(words.get(i++).equals(";") || words.get(i++).equals(",")){
                        skip = true;
                        result.validity = Result.VALID;
                    } else {
                        result.validity = Result.INVALID;
                    }
                }
                
            } if(idx == 27 || idx == 56) { // MODIFIER2

            } if(idx == 38) { // RUNFUNCTION
                skip = true;
                Parser parser = new Parser(input_filename, output_filename)

                //LATER
            } if(idx == 37) { // check return keyword
                if(checker.isReturn(word)){
                    skip = true;
                }
            } if(idx == 47 || idx == 51 || idx == 67 || idx == 69){ // Postincrements og post decrements
                if(!(checker.isVisited(50, visited) && !checker.isVisited(55, visited)) &&
                   !(checker.isVisited(64, visited) && !checker.isVisited(66, visited))) {
                    skip = true;
                }
            } if(idx == 13 || idx == 41){ // DECIMAL point
                if(word.equals(curr.detail)){
                   if(!(checker.isVisited(13, visited)) && !(checker.isVisited(41, visited)) && !type.equals("char")){
                    skip = true;
                   } 
                }
                // if(!(checker.isVisited(13, visited)) && !(checker.isVisited(41, visited)) && !type.equals("char")){
                //     skip = true;
                // }
            } else if(idx == 14 || idx == 42) {
                if(word.equals(curr.detail)){
                    if(type.equals("int") || type.equals("char")){
                        skip = true;
                    }
                }
            } else if(idx == 11 || idx == 39){
                if(word.equals(curr.detail)){
                    if(!type.equals("char")){
                        skip = true;
                    }
                }
            } else if(idx == 46 || idx == 71){ // OPERATORS
                int prev = checker.getPrevious(visited.size() - 1, visited);
                if(prev == 45){ 
                    if(checker.isOperator(word)){
                        skip = true;
                    }
                } else if(prev == 62){
                    if(checker.isOperator(word)){
                        skip = true;
                    }
                }

            } else {
                if(word.equals(curr.detail)){
                    skip = true;
                } 
            }
        }

        if(skip){
             visited.add(curr.index);
             s_queue = (ArrayList<Integer>)curr.next.clone();
             System.out.println(s_queue);
             if(i == end.index){
                break;   
             } if(i < end.index){
                ++i;
             }
        }

      }
      // if(word != null && word.equals(";")){
      //   if(curr.next.contains(9)){
      //       result.validity = Result.VALID;
      //   } 
      // } else if(word != null && word.equals(")")){
      //   if(curr.next.contains(9)){
      //       result.validity = Result.VALID;
      //       result.type = Result.FUNCN;
      //   }
      // }
      //file_handler.writeResults(result.getResult() + "\n");
    
      System.out.println(result.getResult());
      if(result.validity.equals(Result.INVALID)){
        declared.clear();
        visited.clear();
        s_queue.clear();
        s_queue.add(0);
      }
      if(s_queue.contains(-1)){
        s_queue.clear();
        s_queue.add(0);
      }
      this.s_queue = s_queue;  

      return result;
    }




    public static void main(String[] args) {
    	try {
    		System.out.print("Enter the input filename (w/ extension): ");
    		Scanner input = new Scanner(System.in);
    		String input_filename = input.nextLine();
            if(input_filename.isEmpty()){
                input_filename = "input.in";
            } 
    		System.out.print("Enter the output filename (w/ extension): ");
    		String output_filename = input.nextLine();
            if(output_filename.isEmpty()){
                output_filename = "output.out";
            } 
    		

            Parser parser = new Parser(input_filename, output_filename);
            parser.run();
    		input.close();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}

/**
* A Helper Class that handles the checking
*/
class Checker {
    ArrayList<String> reserved;
    ArrayList<String> reserved_sym;
    ArrayList<String> operators;
    public Checker(){
        reserved = new ArrayList<>();
        reserved_sym = new ArrayList<>();
        operators = new ArrayList<>();
        init();
    }

    /**
    * Function initializer of ArrayLists with values
    */
    private void init(){

        /**
        * Initializes the reserved ArrayList with the 
        * reserved keywords
        */ 
        reserved.add("int");
        reserved.add("void");
        reserved.add("float");
        reserved.add("double");
        reserved.add("char");
        reserved.add("return");
        reserved.add("for");
        reserved.add("while");
        reserved.add("do");
        Collections.sort(reserved);
        
        /**
        * Initializes the reserved symbols ArrayList
        */
        reserved_sym.add(";");
        // reserved_sym.add(",");
        // reserved_sym.add("(");
        // reserved_sym.add(")");
        // reserved_sym.add("[");
        // reserved_sym.add("]");
        // reserved_sym.add("{");
        reserved_sym.add("}");
        Collections.sort(reserved_sym);

        /**
        *  Initializes the operators ArrayList
        */

        operators.add("+");
        operators.add("-");
        operators.add("/");
        operators.add("*");
        operators.add("%");
        Collections.sort(operators);
    }

    /**
    * Checks if the word is a modifier including void
    */

    public boolean isValidVar(String word) {
        return isAlpha(word) || isNum(word) || word.equals("_");
    }
    public boolean isModifier1(String word) {

      int index = Collections.binarySearch(reserved, word);
      if(index >= 0){
        return reserved.get(index).equals(word) 
           && !reserved.get(index).equals("return");
      }
      return false;     
    }

    /**
    * Checks if the word is a modifier excluding void
    */

    public boolean isModifier2(String word) {

        int index = Collections.binarySearch(reserved, word);
        if(index >= 0){
            if(reserved.get(index).equals(word)){
              return !reserved.get(index).equals("void") &&
                     !reserved.get(index).equals("return");
            }
        } 
        return false;
    }
    
    /**
    * Checks if the word is a return keyword
    */
    public boolean isReturn(String word) {
        int index = Collections.binarySearch(reserved, word);
        if(index >= 0){
            if(reserved.get(index).equals(word)){
                return reserved.get(index).equals("return");
            }
        }
        return false;
    }
    

    public boolean isSymbol(String word){
        int index = Collections.binarySearch(reserved_sym, word);
        if(index >= 0){
            return reserved_sym.get(index).equals(word);
        }
        return false;
    }

    public boolean isOperator(String word){
        int index = Collections.binarySearch(operators, word);
        if(index >= 0){
            return operators.get(index).equals(word);
        }
        return false;
    }
    /**
    * Checks if the character is an alphabet
    */
    public boolean isAlpha(String word) {
      char ch = word.charAt(0);
      System.out.println(ch);
      return (ch >= 'A' && ch <= 'Z') || 
             (ch >= 'a' && ch <= 'z');
    }
     
    /**
    * Checks if the character is a number
    */
    public boolean isNum(String word) {
        char ch = word.charAt(0);
        return (ch >= '0' && ch <= '9');
    }

    public boolean isOpen(String word) {
        return word.equals("(") || 
               word.equals("{") ||
               word.equals("[");
    }

    public boolean isClose(String word) {
        return word.equals(")") || 
               word.equals("}") ||
               word.equals("]");
    }

    public boolean isReserved(String word){
        int index = Collections.binarySearch(reserved, word);
        if(index >= 0){
            return reserved.get(index).equals(word);
        }
        return false;
    }

    public boolean isDeclared(String word, ArrayList<String> declared){
        Collections.sort(declared);
        int index = Collections.binarySearch(declared, word);
        if(index >= 0){
            return declared.get(index).equals(word);
        }
        return false;
    }

    public boolean isVisited(int idx, ArrayList<Integer> indeces){
       return indeces.contains(idx);
    }

    public boolean hasOneSize(String word){
        return word.length() == 1;
    }

    public int getPrevious(int idx, ArrayList<Integer> visited){
      if(idx >= visited.size()){
        return visited.get(idx);
      }
      return -1;
    }
}    



/**
* The helper class that handles the file handling which includes reading the contents from the file 
* and writing the output to a file. All filenames are entered by the user and all files involved
* are assumed to be text file.
*/

class FileHandler {
  String input_filename;
  String output_filename;
  public String file_contents;
  public ArrayList<Stop> stop_words;

  /**
  * The constructor for the class that accepts the input filename and output filename
  * as parameters.
  */
  public FileHandler(String input_filename, String output_filename) {
  	this.input_filename = input_filename;
  	this.output_filename = output_filename;
  	file_contents = "";
    stop_words = new ArrayList<>();
  }
  
  /**
  * The function that retrieves the strings by line from the input file with the filename 
  * stored in the input_filename variable. Afterwards, each line are splitted by space and 
  * each are appended on an ArrayList of Strings. After reading the file, the ArrayList 
  * will be returned.
  */
  public ArrayList<String> retrieveContents() {
  	try {
        Checker checker = new Checker();
        ArrayList<String> contents = new ArrayList<>();
    	File file = new File(input_filename);
	    Scanner reader = new Scanner(file);
        int i = 0;
	    while (reader.hasNextLine()){
			String line = reader.nextLine();
            line = line.replaceAll("\n", " ").replaceAll("\t", " ");
            while(line.indexOf("  ") >= 0){
                line = line.replace("  ", " ");
            }
            //System.out.print(line.length() + " --> ")
			if(!line.isEmpty() && !line.equals(" ")){
				String line_array[] = line.split(" ");
				for(String str : line_array) {
                    //System.out.println(str.equals(" "));
                    if(!str.equals(" ") && !str.equals("\n")){
                       if(checker.isModifier1(str)){
                          Stop stop = new Stop(str, i);
                          this.stop_words.add(stop);
                          System.out.println(str);
                          ++i;
                          contents.add(str);
                       } else {
                         String c[] = str.split("");
                         for(String ch : c){
                            if(ch.equals(" "));
                            if(checker.isSymbol(ch)){
                                Stop stop = new Stop(ch, i);
                                this.stop_words.add(stop);
                                System.out.println(ch);
                            }
                            contents.add(ch);
                            ++i;
                         }
                       }
                    }
				}
				file_contents += line;	
			}
	   	}
        System.out.println(contents);
	   	reader.close();
	   	return contents;
  	} catch(Exception e) {
  		e.printStackTrace();
  	}
    return null;
  }
  
  /**
  * The function that write the results to output file in checking the input file 
  * in which the filename is stored in the output_filename.
  * Accepts a string which contains the results.
  */
  public void writeResults(String result) {
    try {

		Writer writer = new FileWriter(output_filename);
		writer.write(result);
		writer.flush();
		writer.close();

    } catch(Exception e){
		e.printStackTrace();
    }
  }

  /**
  * The function that displays the contents of the file input which are stored 
  * in file_contents variable.
  */
  public void displayContents() {
    System.out.println(file_contents);
  }
}


class Graph {
  public ArrayList<State> states;

  public Graph(){
  	states = new ArrayList<>();
  	construct();
  }

  private void construct(){
  	ArrayList<Integer> next = new ArrayList<>();

    State state = new State("MODIFIER1", 0);
    state.next("1,2");
    states.add(state);

    state = new State("ALPHA", 1);
    state.next("3,1,8,4,10,9,26,2");
    states.add(state);
    System.out.println(state.next);
    state = new State("_", 2);
    state.next("2,9,8,4,10,3,26,1");
    states.add(state);

    state = new State("NUM", 3);
    state.next("3,2,1,9,10,4,8,26");
    states.add(state);

    state = new State("[", 4);
    state.next("6,5,7");
    states.add(state);

    state = new State("DECLARED", 5);
    state.next("7,5");
    states.add(state);

    state = new State("NUM", 6);
    state.next("6,7");
    states.add(state);

    state = new State("]", 7);
    state.next("9,10,8");
    states.add(state);

    state = new State("=", 8);
    state.next("12,14,11,23,17,20,21");
    states.add(state);
    
    state = new State(";", 9);
    state.next("-1");
    states.add(state);

    state = new State(",", 10);
    state.next("12,14,11,1,2");
    states.add(state);

    state = new State("-", 11);
    state.next("12");
    states.add(state);

    state = new State("NUM", 12);
    state.next("13,12,10,18,22,25,9");
    states.add(state);

    state = new State(".", 13);
    state.next("12");
    states.add(state);

    state = new State("'", 14);
    state.next("15");
    states.add(state);

    state = new State("1L", 15);
    state.next("16");
    states.add(state);

    state = new State("'", 16);
    state.next("10,18,22,25,9");
    states.add(state);

    state = new State("{", 17);
    state.next("12,14,11,19");
    states.add(state);
   
    state = new State("}", 18);
    state.next("9,10");
    states.add(state);

    state = new State("DECLARED", 19);
    state.next("10,18");
    states.add(state);

    state = new State("(", 20);
    state.next("21,12,14,11");
    states.add(state);

    state = new State("DECLARED", 21);
    state.next("10,22,9");
    states.add(state);

    state = new State(")", 22);
    state.next("9,10");
    states.add(state);

    state = new State("{", 23);
    state.next("11,12,14,13,24");
    states.add(state);

    state = new State("DECLARED", 24);
    state.next("25");
    states.add(state);

    state = new State("}", 25);
    state.next("9,10");
    states.add(state);

    state = new State("(", 26);
    state.next("27,34");
    states.add(state);

    state = new State("MODIFIER2", 27);
    state.next("28,29");
    states.add(state);

    state = new State("ALPHA", 28);
    state.next("28,29,31,34,30");
    states.add(state);

    state = new State("_", 29);
    state.next("29,28,31,34,30");
    states.add(state);

    state = new State("NUM", 30);
    state.next("30,34,31,28,29");
    states.add(state);

    state = new State("[", 31);
    state.next("32");
    states.add(state);

    state = new State("]", 32);
    state.next("33,34");
    states.add(state);

    state = new State(",", 33);
    state.next("27");
    states.add(state);

    state = new State(")", 34);
    state.next("35,36");
    states.add(state);

    state = new State("{", 35);
    state.next("75,38,54,49,45,56,35");
    states.add(state);

    state = new State(";", 36);
    state.next("-1");
    states.add(state);

    state = new State("RETURN", 37);
    state.next("54,49,40,42,39,45");
    states.add(state);

    state = new State("RUNFUNCTION", 38);
    state.next("38,75,54,49,45,56,37");
    states.add(state);

    state = new State("-", 39);
    state.next("40");
    states.add(state);

    state = new State("NUM", 40);
    state.next("40,41,46,53,71,72,73");
    states.add(state);

    state = new State(".", 41);
    state.next("40");
    states.add(state);

    state = new State("'", 42);
    state.next("43");
    states.add(state);

    state = new State("1L", 43);
    state.next("44");
    states.add(state);

    state = new State("'", 44);
    state.next("46,53,71,72,73");
    states.add(state);

    state = new State("DECLARED", 45);
    state.next("46,53,47,51,60");
    states.add(state);

    state = new State("OPERATORS", 46);
    state.next("40,42,39,45");
    states.add(state);

    state = new State("+", 47);
    state.next("48");
    states.add(state);

    state = new State("+", 48);
    state.next("46,53");
    states.add(state);

    state = new State("+", 49);
    state.next("50");
    states.add(state);

    state = new State("+", 50);
    state.next("45");
    states.add(state);

    state = new State("-", 51);
    state.next("52");
    states.add(state);

    state = new State("-", 52);
    state.next("46,53");
    states.add(state);

    state = new State(";", 53);
    state.next("54,49,45,56,38,75,37");
    states.add(state);

    state = new State("-", 54);
    state.next("55");
    states.add(state);

    state = new State("-", 55);
    state.next("45");
    states.add(state);

    state = new State("MODIFIER2", 56);
    state.next("57,58");
    states.add(state);

    state = new State("ALPHA", 57);
    state.next("57,58,59,60");
    states.add(state);

    state = new State("_", 58);
    state.next("58,57,59,60");
    states.add(state);

    state = new State("NUM", 59);
    state.next("59,57,58,60");
    states.add(state);

    state = new State("=", 60);
    state.next("61,62,63,65,39,40,42");
    states.add(state);

    state = new State("(", 61);
    state.next("62,63,65,39,40,42");
    states.add(state);

    state = new State("DECLARED", 62);
    state.next("73,72");
    states.add(state);

    state = new State("+", 63);
    state.next("64");
    states.add(state);

    state = new State("+", 64);
    state.next("62");
    states.add(state);

    state = new State("-", 65);
    state.next("66");
    states.add(state);

    state = new State("-", 66);
    state.next("62");
    states.add(state);

    state = new State("+", 67);
    state.next("68");
    states.add(state);

    state = new State("+", 68);
    state.next("73,71,72");
    states.add(state);

    state = new State("-", 69);
    state.next("70");
    states.add(state);

    state = new State("-", 70);
    state.next("71,72,73");
    states.add(state);

    state = new State("OPERATORS", 71);
    state.next("39,40,42,62");
    states.add(state);

    state = new State(")", 72);
    state.next("73,74");
    states.add(state);

    state = new State(";", 73);
    state.next("75,54,49,45,38,56,37");
    states.add(state);

    state = new State(",", 74);
    state.next("57,58,45");
    states.add(state);

    state = new State("}", 75);
    state.next("-1");
    states.add(state);
  }

  public ArrayList<State> states(){
    return states;
  }

  public void nextIdx(String str){
    String[] s = str.split(",");
    System.out.println(s[0]);
  }
}


class State {
	public String detail;
	public ArrayList<Integer> next;
	public boolean is_last_state;
    public int index;

	public State(String detail, int index){
      this.detail = detail;
      this.index = index;
      next = new ArrayList<>();
	}


	public void setLastState(boolean is_last_state){
    	this.is_last_state = is_last_state;
	}

    public void next(String str){
        String[] s = str.split(",");
        for(String s_idx : s){
            next.add(Integer.parseInt(s_idx));
        }
    }
}

class Stop {
    public String detail;
    public int index;

    public Stop(String detail, int index) {
        this.detail = detail;
        this.index = index;
    }
}

class Result {

    public static final String DECN = "declaration";
    public static final String DEFN = "definition";
    public static final String VAR = "variable";
    public static final String FUNCN = "function";
    public static final String VALID = "valid";
    public static final String INVALID = "invalid";

    public String type;
    public String subtype;
    public String validity;
    public ArrayList<String> declared;
    public ArrayList<String> arr_dec;

    public Result(){
        declared  = new ArrayList<>();
        type = Result.VAR;
        subtype = Result.DECN;
        validity = Result.INVALID;
        arr_dec = new ArrayList<>();
    }

    public void addDeclared(String var){
        declared.add(var);
    }

    public String getResult(){
        return validity + " " + type + " " + subtype;
    }
}