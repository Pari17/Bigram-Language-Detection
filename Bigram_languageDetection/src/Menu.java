
import java.util.Scanner;

public class Menu {
  public static void main(String[] args) {
    System.out.print("\u000c");
    manualEntry ManualEntryObject = new manualEntry();
    fileInput FileInputObject = new fileInput();
    String menuOpt = "";
    Scanner in = new Scanner(System.in);
    do {
      System.out.printf("E - Exit\n");
      System.out.printf("CM - Create Models\n");
      System.out.printf("ME - Manual Entry\n");
      System.out.printf("TF - Load Test Files\n");
      System.out.printf("Select Option:\n");
      menuOpt = in.nextLine();
      System.out.println("You entered : " + menuOpt);
      if (menuOpt.compareToIgnoreCase("ME") == 0) {
        System.out.printf("\nyou selected option: Manual Entry\n");
        ManualEntryObject.process();
      }
      if (menuOpt.compareToIgnoreCase("CM") == 0) {
        System.out.printf("\nyou selected option: Create Models\n");
        FileInputObject.fileInput();
      }
      if (menuOpt.compareToIgnoreCase("TF") == 0) {
        System.out.printf("\nyou selected option: Load Test Files\n");
        FileInputObject.detectLanguage();
      }
    } while (menuOpt.compareToIgnoreCase("E") != 0); // close if e is pressed
    System.out.printf("\nEnding Now\n");
  }

}
