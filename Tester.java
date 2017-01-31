import java.io.*;
import java.util.*;

class Tester {
	String base_filename;
	String test_filename;
    
    public Tester(String base_filename, String test_filename) {
      this.base_filename = base_filename;
      this.test_filename = test_filename;
    }
	public void compareResults(){
	    try {
	    	File file_base = new File(base_filename);
	    	File file_test = new File(test_filename);

		    Scanner reader_base = new Scanner(file_base);
		    Scanner reader_test = new Scanner(file_test);
            
            Writer writer = new FileWriter("results.txt");
            
		    while (reader_base.hasNextLine()){
				String line_base = reader_base.nextLine();
				if(reader_test.hasNextLine()){
					String line_test = reader_test.nextLine();
					String line_base1 = line_base.replace(" ", "").toLowerCase().replace("\t", "").replace("\n", "").trim();
					String line_test1 = line_test.replace(" ", "").toLowerCase().replace("\t", "").replace("\n", "").trim();
				    if(line_test1.equals(line_base1)){
	                  writer.write("PASSED");
				    } else {
	                  writer.write("FAILED: SHOULD BE (" + line_base + ")");
				    }
				} else {
					writer.write("NO RESULT: SHOULD BE (" + line_base + ")");
				}
				
			    writer.write(System.getProperty("line.separator"));
		   	}
		   	writer.flush();
	  		writer.close();
		   	reader_base.close();
		 	reader_test.close();
	   	} catch(Exception e) {
	  		e.printStackTrace();
	  	}
	}
	public static void main(String[] args) {
		Tester tester = new Tester("base.txt", "res.txt");
		tester.compareResults();
	}
}