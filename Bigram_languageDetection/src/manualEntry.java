
import java.util.Scanner;

public class manualEntry {
  bigramProcess BigramsProcessObject = new bigramProcess();

  public void process() {
    String text;
    System.out.println("Please enter text");
    Scanner in = new Scanner(System.in);
    text = in.nextLine(); // Add response to 'text'
    System.out.println();
    BigramsProcessObject.findLanguage("Manually Inputted", text);
  }
}
