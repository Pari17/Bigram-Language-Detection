
import java.util.ArrayList;
import java.util.Scanner;

public class fileInput {

  bigramProcess BigramsProcessObject = new bigramProcess();

  public void fileInput() {
    ArrayList<String> txtFiles = new ArrayList<String>(); // list of all text docs
    ArrayList<String> spiltLis = new ArrayList<String>(); // for spilt
    String callerClassName = Thread.currentThread().getStackTrace()[1].getMethodName(); // Finds
                                                                                        // calling
                                                                                        // class
    txtFiles = BigramsProcessObject.loadTextFiles(bigramProcess.learning);
    BigramsProcessObject.deleteFiles(); // delete all files in the directory
    String learning = "learning";
    String text = BigramsProcessObject.readUTF8File(txtFiles, callerClassName, learning);
    if (txtFiles.size() != 5) {// if not 5 files
      System.out.println(
          "A model is missing. Please ensure that the following items are in the Learning folder");
      System.out.println("English.txt, Spanish.txt, French.txt, German.txt and Italian.txt");
    } else {
      System.out.println("\nModels have been created for: ");
      txtFiles.forEach(System.out::println);
    }
    System.out.println();
  }

  public void detectLanguage() {
    ArrayList<String> txtFiles = new ArrayList<String>(); // list of all text docs
    ArrayList<String> spiltLis = new ArrayList<String>(); // for spilt
    String callerClassName = Thread.currentThread().getStackTrace()[1].getMethodName(); // Finds
                                                                                        // calling
                                                                                        // class
    Scanner in = new Scanner(System.in);
    String menuOpt = "";
    boolean next = false;
    do { // Loop to give option to open 1 or all testing files
      System.out.println("Which files do you wish to open?\n");
      System.out.printf("O - One File\n");
      System.out.printf("M - All Files\n\n");
      System.out.printf("Select Option:\n");
      menuOpt = in.nextLine();
      System.out.println("You entered : " + menuOpt);
      if (menuOpt.compareToIgnoreCase("M") == 0) {
        System.out.printf("\nAll txt files are being loaded.\n");
        txtFiles = BigramsProcessObject.loadTextFiles(bigramProcess.testing);
        next = true;
      }
      if (menuOpt.compareToIgnoreCase("O") == 0) {
        System.out.printf("\nPlease input the text file names\n");
        txtFiles.add(in.nextLine());
        next = true;
      }
    } while (next != true);
    String testing = "testing"; // for file location
    String text = BigramsProcessObject.readUTF8File(txtFiles, callerClassName, testing); // Read all
                                                                                         // or 1
                                                                                         // file
  }
}
