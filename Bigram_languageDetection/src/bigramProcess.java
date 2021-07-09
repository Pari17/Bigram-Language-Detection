
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class bigramProcess {
  static String dir = System.getProperty("user.dir");
  public static String models = dir + "/Models";
  public static String testing = dir + "/Testing";
  public static String learning = dir + "/Learning";

  public String findLocale(String text, String fileName) {
    String localeName = whenConvertString(fileName);
    Locale locale = new Locale(localeName);
    text = extractWords(text, locale);
    return text;
  }

  public String[][] arrCreation(String text) {
    ArrayList<String> spiltLis = new ArrayList<String>();
    String[][] finArr;
    long occurance;
    int listSize;
    spiltLis = popArr(text);
    occurance = spiltLis.stream().distinct().count(); // move to steam - get amount of unique values
    listSize = spiltLis.size(); // size of the list
    finArr = new String[(int) occurance][2]; // create 2D array
    finArr = lisTo2D(spiltLis, listSize, (int) occurance);
    return finArr;
  }

  public String[][] lisTo2D(ArrayList<String> mainLis, int size, int freq) {
    String[][] arr = new String[freq][2];
    String value;
    int count;
    int freqJump = 0; // counts the amount of time to jump through list
    double probability;
    for (int i = 0; i < freq; i++) {
      count = 0;
      for (String biagram : mainLis) { // find how many times it appears in the array
        if (biagram.equals(mainLis.get(freqJump))) {
          count++;
        }
      }
      probability = (double) count / (double) size; // bigram odds
      arr[i][0] = mainLis.get(freqJump);
      arr[i][1] = Double.toString(probability);
      freqJump = freqJump + count; // adds how many times to freq
    }
    return arr;
  }

  public static String extractWords(String inputText, Locale currentLocale) { 
    ArrayList wordList = new ArrayList();
    Locale SpanLocale = new Locale("es", "ES"); // Needed for region
    BreakIterator wordIterator = BreakIterator.getWordInstance(currentLocale); // use locale
    String extractWords = "";
    wordIterator.setText(inputText);
    int start = wordIterator.first();
    int end = wordIterator.next();
    while (end != BreakIterator.DONE) {
      String word = inputText.substring(start, end);
      word = word.toLowerCase();
      if (Character.isLetter(word.charAt(0)) && word.length() > 1) {
        wordList.add(word);
      }
      start = end;
      end = wordIterator.next();
    }
    for (Object word : wordList) {
      extractWords += word + " ";
    }
    return extractWords;
  }

  public ArrayList popArr(String text) {
    ArrayList<String> mainLis = new ArrayList<String>();
    boolean value;
    for (int i = 0; i < text.length() - 1; i++) {
      value = isAdded(text, i); // check to add
      if (value == true) {
        mainLis.add(Character.toString(text.charAt(i)) + Character.toString(text.charAt(i + 1))); // add
                                                                                                  // to
                                                                                                  // string
      }
    }
    mainLis.sort(String::compareToIgnoreCase); // sort
    return mainLis;
  }

  public boolean isAdded(String text, int location) {
    boolean add = true;
    if (location == 0) {// Start of array
      if (text.charAt(location) == ' ' || text.charAt(location + 1) == ' ') {
        add = false;
      }
    } else if (location == text.length() - 1) {// end of array
      if (text.charAt(location) == ' ' || text.charAt(location - 1) == ' ') {
        add = false;
      }
    } else if (text.charAt(location) == ' ' || text.charAt(location + 1) == ' ') {// remove blanks
      add = false;
    }
    return add;
  }

  public ArrayList loadTextFiles(String fileLocation) {
    ArrayList<String> txtFiles = new ArrayList<String>();
    try {
      File folder = new File(fileLocation);
      File[] listOfFiles = folder.listFiles();
      for (File file : listOfFiles) {
        if (file.isFile() && !file.toString().contains(".DS_Store")) { // For mac - .DS_Store is the
                                                                       // hidden cofigeration file
          System.out.println(file);
          txtFiles.add(file.getName()); // Add all languges in to list
        }
        Collections.sort(txtFiles);
      }
    } catch (Exception e) {
      System.out.println("Cannot locate: " + fileLocation);
    }
    return txtFiles;
  }

  public static void deleteFiles() { // delete existing models
    try {
      File folder = new File(models);
      File[] listOfFiles = folder.listFiles();
      for (File f : folder.listFiles()) {// delete files in folder
        f.delete();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String readUTF8File(ArrayList<String> txtFileList, String methodCaller, String file) {
    String langText = "";
    String[][] finArr; // arr for end
    try {
      for (String name : txtFileList) {
        StringBuilder fileContent = new StringBuilder();
        Reader reader =
            new InputStreamReader(new FileInputStream(file + "/" + name), StandardCharsets.UTF_8); // Read
                                                                                                   // as
                                                                                                   // UTF
                                                                                                   // 8
        BufferedReader bufferedReader = new BufferedReader(reader);
        String s = "";
        while ((s = bufferedReader.readLine()) != null) {
          fileContent.append(s + " "); // builds output
        }
        bufferedReader.close();
        reader.close();
        langText = fileContent.toString();
        if (methodCaller == "fileInput") {
          String localeText = findLocale(langText, name);
          finArr = arrCreation(localeText);
          writeUTF8File(models + "/" + name, finArr);
        } else {
          findLanguage(name, langText);
        }
      }
    } catch (FileNotFoundException exception) {
      System.out.println("~~ File not found");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return langText;
  }

  public void findLanguage(String textName, String langText) {
    ArrayList<String> mainLis = new ArrayList<String>(); // for spilt
    ArrayList<String> txtFiles = new ArrayList<String>(); // list of all text docs
    ArrayList<String> modDocs = new ArrayList<String>();
    txtFiles = loadTextFiles(models);
    double num;
    double highNum = 0;
    String langType = "";
    System.out.println("File: " + textName);
    System.out.println("RAW Text: " + langText + "\n");
    for (String modelLang : txtFiles) { // Load all files
      modDocs = readTxtFiles(modelLang);
      String localeText = findLocale(langText, modelLang);
      mainLis = popArr(localeText);
      num = 1;
      double holder;
      for (String bigram : mainLis) { // find bigram odds
        holder = binarySearch(modDocs, 0, modDocs.size(), bigram); // get odds of bigram
        num *= holder; // calculate odds
      }
      System.out.println(modelLang + " has a probability of :" + num); // print odds for each loop
      if (num > highNum) { // update name and value of highest
        highNum = num;
        langType = modelLang;
      }
    }
    System.out.println("");
    System.out.print("The detected language is: ");
    if (langType != "") {
      System.out.println(langType.split("\\.")[0] + ""); // print name - without ".txt"
    } else {
      System.out.println("Unknown");
      System.out.println("Please ensure your sentence does not contain any spelling errors.");
    }
    System.out.println("-------------");
    System.out.println();
  }

  public ArrayList readTxtFiles(String fileLocation) { // Read text files and return as a list
    ArrayList<String> list = new ArrayList<String>();
    try {
      Scanner s = new Scanner(new File(models + "/" + fileLocation));
      while (s.hasNext()) {
        list.add(s.nextLine()); // add to list
      }
      s.close();
    } catch (FileNotFoundException exception) {
      System.out.println("~~ File not found");
    }
    return list;
  }

  public static double binarySearch(ArrayList<String> arr, int first, int last, String target) {
    int mid; // index of the midpoint
    String midvalue; // object that is assigned arr[mid]
    int origLast = last; // save original value of last
    String[] indLine = new String[2];
    ArrayList<String> trigram = new ArrayList<String>();
    ArrayList<String> prob = new ArrayList<String>();
    // arr.forEach(System.out::println);
    for (Object word : arr) {
      indLine = ((String) word).split(" ");
      trigram.add(indLine[0]);
      prob.add(indLine[1]);
    }
    while (first < last) {// test for nonempty sublist
      mid = (first + last) / 2;
      midvalue = trigram.get(mid);
      if (target.equals(midvalue)) {
        return Double.parseDouble((prob.get(mid))); // have a match
      } else if (target.compareTo(midvalue) < 0) {
        last = mid; // search lower sublist. Reset last
      } else {
        first = mid + 1; // search upper sublist. Reset first
      }
    }
    return 0; // target not found
  }

  public static void writeUTF8File(String filePath, String[][] content) {
    try {
      PrintWriter fiwr = new PrintWriter(filePath);
      for (int i = 0; i < content.length; i++) {
        fiwr.write(content[i][0] + " " + content[i][1]);
        fiwr.write(System.getProperty("line.separator")); // prints of different lines
      }
      fiwr.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String whenConvertString(String text) {
    HashMap<String, String> hash_map = new HashMap<String, String>();
    // All text names and corr
    hash_map.put("French.txt", "Locale.FRENCH");
    hash_map.put("English.txt", "Locale.ENGLISH");
    hash_map.put("Spanish.txt", "SpanLocale");
    hash_map.put("German.txt", "Locale.GERMAN");
    hash_map.put("Italian.txt", "Locale.ITALIAN");
    if (hash_map.get(text) == null) { // If a text name if not one from the list
      System.out.println(text + " does not have an assoicated language");
      System.out.println("Only English, French, German, Italian and Spanish can be processed.");
      System.out.println("Program exiting ...");
      System.exit(0);
    }
    return hash_map.get(text);
  }
}
