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
    private static final String VAR_DEF = "variable declaration";
    private static final String FUNC_DEC = "function declaration";
    private static final String FUNC_DEF = "function definition";
    private static final String VAR = "variable";
    private static final String FUNC = "function";
    private static final String VOID = "void";
    private static final String INT = "int";
    private static final String CHAR = "char";
    private static final String FLOAT = "float";
    private static final String DOUBLE = "double";
    private static final String LONG = "long";

    ArrayList<String> statements;
    FileHandler file_handler;
    String input_filename;
    String output_filename;
    Graph graph;

    public Parser(String input_filename, String output_filename){
        this.input_filename = input_filename;
        this.output_filename = output_filename;
        graph = new Graph();
        file_handler = new FileHandler(input_filename, output_filename);
        retrieveContents();
    }

    private void retrieveContents(){
        statements = file_handler.retrieveContents();
    }

    public void run(){
        for(Stop stop : file_handler.stop_char ){
          System.out.println(statements.get(stop.index));
        }
    }
    public static void main(String[] args) {
        Checker checker = new Checker();
        System.out.println(checker.isModifier1("int"));
        System.out.println(checker.isModifier2("voID"));
        System.out.println(checker.isAlpha('Y'));
        System.out.println(checker.isAlpha('B'));
        System.out.println(checker.isAlpha('@'));
        System.out.println(checker.isAlpha('b'));
        System.out.println(checker.isAlpha('y'));
    	// try {
    	// 	System.out.print("Enter the input filename (w/ extension): ");
    	// 	Scanner input = new Scanner(System.in);
    	// 	String input_filename = input.nextLine();
     //        if(input_filename.isEmpty()){
     //            input_filename = "input.in";
     //        } 
    	// 	System.out.print("Enter the output filename (w/ extension): ");
    	// 	String output_filename = input.nextLine();
     //        if(output_filename.isEmpty()){
     //            output_filename = "output.out";
     //        } 
    		
            
        
            
     //        System.out.println("Retrieving data from input file : DONE");
     //        Parser parser = new Parser(input_filename, output_filename);

     //        parser.run();
     //  //       Collections.sort(arr);
     //  //       int index = Collections.binarySearch(arr, statements.get(0));
     //  //       System.out.println(arr.get(index));
    		       
    	// 	input.close();
    	// } catch(Exception e) {
    	// 	e.printStackTrace();
    	// }
    }
}

/**
* A Helper Class that handles the checking
*/
class Checker {
    ArrayList<String> modifier;

    public Checker(){
        modifier = new ArrayList<>();
        init();
    }

    /**
    * Function initializer of ArrayLists with values
    */
    private void init(){

        /**
        * Initializes modifier1 with the 
        * modifier including void
        */ 

        modifier.add("int");
        modifier.add("void");
        modifier.add("float");
        modifier.add("double");
        modifier.add("char");
        Collections.sort(modifier);
    }

    /**
    * Checks if the word is a modifier including void
    */
    public boolean isModifier1(String word) {
      int index = Collections.binarySearch(modifier, word);
      if(index >= 0){
        return modifier.get(index).equals(word);
      }
      return false;     
    }

    /**
    * Checks if the word is a modifier excluding void
    */

    public boolean isModifier2(String word) {

        int index = Collections.binarySearch(modifier, word);
        if(index >= 0){
            if(modifier.get(index).equals(word)){
              return !modifier.get(index).equals("void");
            }
        } 
        return false;
    }
    
    /**
    * Checks if the character is an alphabet
    */
    public boolean isAlpha(char ch) {
      return (ch >= 'A' && ch <= 'Z') || 
             (ch >= 'a' && ch <= 'z');
    }
     
    /**
    * Checks if the character is a number
    */
    public boolean isNum(char ch) {
     return (ch >= '0' && ch <= '9');
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
  public ArrayList<Stop> stop_char;

  /**
  * The constructor for the class that accepts the input filename and output filename
  * as parameters.
  */
  public FileHandler(String input_filename, String output_filename) {
  	this.input_filename = input_filename;
  	this.output_filename = output_filename;
  	file_contents = "";
    stop_char = new ArrayList<>();
  }
  
  /**
  * The function that retrieves the strings by line from the input file with the filename 
  * stored in the input_filename variable. Afterwards, each line are splitted by space and 
  * each are appended on an ArrayList of Strings. After reading the file, the ArrayList 
  * will be returned.
  */
  public ArrayList<String> retrieveContents() {
  	try {
        ArrayList<String> contents = new ArrayList<>();
    	File file = new File(input_filename);
	    Scanner reader = new Scanner(file);
        int i = 0;
	    while (reader.hasNextLine()){
			String line = reader.nextLine();
			line = line.replace("\t", "").replace("\n", "");
			if(!line.isEmpty() && !line.equals(" ")){
				String line_array[] = line.split(" ");
				for(String str : line_array) {
                    if(!str.equals(" ")){
                       if(str.contains(";")){
                         Stop s = new Stop(";", i);
                         stop_char.add(s);
                       }
                       
                       contents.add(str); 
                       ++i;
                    }
				}
                contents.add("\n");
                ++i;
				file_contents += line;	
			}
	   	}
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
  	State state = new State("IDENTIFIER1", 0);
    next.add(1);
    state.addNextIdx(next);
    states.add(state);
    
    state = new State(" ", 1);
    next = new ArrayList<>();
    next.add(1);
    next.add(2); 
    next.add(32);
    state.addNextIdx(next);
    states.add(state);

    state = new State("ALPHABET", 2);
    next = new ArrayList<>();
    next.add(2);
    next.add(9);
    next.add(10);
    next.add(5);
    next.add(6); 
    next.add(4);
    next.add(3);
    next.add(32);
    state.addNextIdx(next);
    states.add(state);

    state = new State("NUMERIC", 3);
    next = new ArrayList<>();
    next.add(5);
    next.add(3);
    next.add(2);
    next.add(4);
    next.add(6); 
    next.add(32);
    state.addNextIdx(next);
    states.add(state);

    state = new State(",", 4);
    next = new ArrayList<>();
    next.add(1);
    next.add(2); 
    state.addNextIdx(next);
    states.add(state);

    state = new State(" ", 5);
    next = new ArrayList<>();
    next.add(5);
    next.add(6);
    next.add(21); 
    state.addNextIdx(next);
    states.add(state);

    state = new State("=", 6);
    next = new ArrayList<>();
    next.add(7);
    next.add(11);
    next.add(12);
    next.add(14);
    next.add(17);
    next.add(18);
    next.add(19);
    next.add(20);
    next.add(8); 
    state.addNextIdx(next);
    states.add(state);

    state = new State(" ", 7);
    next = new ArrayList<>();
    next.add(7);
    next.add(8);
    next.add(11);
    next.add(12);
    next.add(14);
    next.add(17);
    next.add(18);
    next.add(19);
    next.add(20); 
    state.addNextIdx(next);
    states.add(state);

    state = new State("DECLARED_VAR", 8);
    next = new ArrayList<>();
    next.add(9);
    next.add(10);
    state.addNextIdx(next);
    states.add(state);

    state = new State(" ", 9);
    next = new ArrayList<>();
    next.add(9);
    next.add(10);
    state.addNextIdx(next);
    states.add(state);

    state = new State(";", 10);
    next = new ArrayList<>();
    next.add(-1);
    state.addNextIdx(next);
    states.add(state);

    state = new State("-", 11);
    next = new ArrayList<>();
    next.add(12);
    state.addNextIdx(next);
    states.add(state);

    state = new State("NUMERIC", 12);
    next = new ArrayList<>();
    next.add(12);
    next.add(13);
    next.add(9);
    next.add(10);
    state.addNextIdx(next);
    states.add(state);

    state = new State(".", 13);
    next = new ArrayList<>();
    next.add(12);
    state.addNextIdx(next);
    states.add(state);

    state = new State("'", 14);
    next = new ArrayList<>();
    next.add(15);
    state.addNextIdx(next);
    states.add(state);

    state = new State("ONE", 15);
    next = new ArrayList<>();
    next.add(16);
    state.addNextIdx(next);
    states.add(state);

    state = new State("'", 16);
    next = new ArrayList<>();
    next.add(9);
    next.add(10);
    state.addNextIdx(next);
    states.add(state);

    state = new State("true", 17);
    next = new ArrayList<>();
    next.add(9);
    next.add(10);
    state.addNextIdx(next);
    states.add(state);

    state = new State("false", 18);
    next = new ArrayList<>();
    next.add(9);
    next.add(10);
    state.addNextIdx(next);
    states.add(state);

    state = new State("1", 19);
    next = new ArrayList<>();
    next.add(9);
    next.add(10);
    state.addNextIdx(next);
    states.add(state);

    state = new State("0", 20);
    next = new ArrayList<>();
    next.add(9);
    next.add(10); 
    state.addNextIdx(next);
    states.add(state);

    state = new State("(", 21);
    next = new ArrayList<>();
    next.add(22);
    next.add(0);
    state.addNextIdx(next);
    states.add(state);

    state = new State("IDENTIFIER2", 22);
    next = new ArrayList<>();
    next.add(23);
    state.addNextIdx(next);
    states.add(state);

    state = new State(" ", 23);
    next = new ArrayList<>();
    next.add(23);
    next.add(24);
    state.addNextIdx(next);
    states.add(state);

    state = new State("ALPHABET", 24);
    next = new ArrayList<>();
    next.add(24);
    next.add(25);
    next.add(26);
    next.add(28);
    state.addNextIdx(next);
    states.add(state);

    state = new State("NUMERIC", 25);
    next = new ArrayList<>();
    next.add(26);
    next.add(28);
    next.add(24);
    next.add(25); 
    state.addNextIdx(next);
    states.add(state);

    state = new State(",", 26);
    next = new ArrayList<>();
    next.add(27);
    next.add(22);
    next.add(0); 
    state.addNextIdx(next);
    states.add(state);

    state = new State(" ", 27);
    next = new ArrayList<>();
    next.add(27);
    next.add(22);
    next.add(0); 
    state.addNextIdx(next);
    states.add(state);

    state = new State(")", 28);
    next = new ArrayList<>();
    next.add(9);
    next.add(10);
    next.add(29);
    next.add(30); 
    state.addNextIdx(next);
    states.add(state);

    state = new State(" ", 29);
    next = new ArrayList<>();
    next.add(29);
    next.add(30); 
    state.addNextIdx(next);
    states.add(state);

    state = new State("{", 30);
    next = new ArrayList<>();
    state.addNextIdx(next);
    states.add(state);
    
    state = new State(" ", 31);
    next  = new ArrayList<>();
    next.add(31);
    next.add(0);
    state.addNextIdx(next);
    states.add(state);

    state = new State("_", 32);
    next = new ArrayList<>();
    next.add(32);
    next.add(4);
    next.add(3);
    next.add(2); 
    state.addNextIdx(next);
    states.add(state);
  }
}


class State {
	public String detail;
	public ArrayList<Integer> next_idx;
	public boolean is_last_state;
    public int index;

	public State(String detail, int index){
      this.detail = detail;
      this.index = index;
      next_idx = new ArrayList<>();
	}

	public void addNextIdx(ArrayList<Integer> next_idx) {
		this.next_idx = next_idx;
	}

	public void setLastState(boolean is_last_state){
    	this.is_last_state = is_last_state;
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