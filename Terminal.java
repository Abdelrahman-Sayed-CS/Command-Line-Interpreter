
// You will FIND JAR file in commons-io-2.15.0 Folder,
// Please Add it as Referenced Library
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Terminal {
  private Parser parser;
  private ArrayList<String> outputs;
  private ArrayList<String> commandHistory;

  // Start your path as your current user path directory
  private Path currentPath;

  public Terminal() {
    outputs = new ArrayList<>();
    parser = new Parser();
    currentPath = Paths.get(System.getProperty("user.home"));
    commandHistory = new ArrayList<>();
  }

  // Implement each command in a method, for example:
  public void parse(String input) {
    parser.parse(input);
    commandHistory.add(input);
  }

  // To check if a string represents a valid file path or no
  public Boolean isValidPath(String path) {
    File file = new File(path);
    return file.exists() && file.isDirectory();
  }

  public Boolean isValidPath(Path path) {
    return Files.exists(path) && Files.isDirectory(path);
  }

  /**
   * @param path can be null, relative or absolute path
   * @return null => return currentPath Directory
   * @return absolute path => if it was relative path or it was already absolute
   */
  public Path makeAbsoluteIfNot(Path path) {
    if (path == null) {
      path = this.currentPath;
    } else if (!path.isAbsolute()) {
      // Make the relative path absolute path by merging it with the current directory
      path = Paths.get(this.currentPath.toString(), path.toString());
    }
    return path;
  }

  /**
   * @param pathStr as path String
   * @return absolute path as a String
   */
  public String makeAbsoluteIfNot(String pathStr) {
    return makeAbsoluteIfNot(Paths.get(pathStr)).toString();
  }

  public void displayCommandHistory() {
    System.out.println("Command History:");
    int counter = 1;
    for (String command : commandHistory) {
      System.out.println(counter + ". " + command);
      counter++;
    }
  }

  public String getCurrentPath() {
    return currentPath.toString();
  }

  // echo
  public String echo() {
    ArrayList<String> args = new ArrayList<String>();
    args = parser.getArgs();
    String Line = "";
    for (String arg : args) {
      Line += arg + " ";
      // Line += " ";
    }
    return Line;
  }

  // pwd
  public String pwd() {
    return currentPath.toString();
  }

  // ls + ls -r
  public Boolean ls() {
    // ** use this for ls
    // Collections.sort(terminal.outputs);
    // ** use this for ls -r
    // Collections.sort(terminal.outputs, Collections.reverseOrder());
    File dirPath = new File(currentPath.toString());
    if (isValidPath(currentPath.toString())) {
      File[] files = dirPath.listFiles();
      if (files != null) {
        outputs.clear(); // Clear existing outputs
        int counter = 1;
        for (File f : files) {
          if (f.isDirectory()) {
            outputs.add(counter + "- " + f.getName() + File.separator + '\n');
          } else if (f.isFile()) {
            outputs.add(counter + "- " + f.getName() + '\n');
          }
          counter++;
        }
      } else {
        return false;
      }
    }
    return true;
  }

  // mkdir
  // absolute path --> I:\VSprojects\ProjectsOfThirdYear\DataCom\file
  // relative path --> \ProjectsOfThirdYear\DataCom\file
  // If it's not an absolute path, create a full path by merging it with the
  // current directory
  public boolean mkdir(Path path) {
    boolean created = true;

    File directoryFile = new File(path.toString());
    if (!directoryFile.exists()) {
      if (!directoryFile.mkdir()) {
        created = false;
      }
    }
    return created;
  }
  // remove file filePath -->

  public Boolean rm(String filePath) {
    File file = new File(makeAbsoluteIfNot(filePath));
    // Check if the file exists before attempting to delete
    if (file.exists()) {
      // Attempt to delete the file
      if (file.delete()) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  public boolean rmdir(String dirPath) {
    File directory;
    if (dirPath.equals("*")) {
      directory = this.currentPath.toFile();
    } else {
      directory = new File(makeAbsoluteIfNot(dirPath));
    }

    if (directory.isDirectory()) {
      File[] files = directory.listFiles();
      // Check if the directory is empty
      if (files != null && files.length == 0) {
        return directory.delete();
      } else if (dirPath.equals("*")) {
        // Remove all empty directories in the current directory
        File[] currentDirFiles = this.currentPath.toFile().listFiles();
        if (currentDirFiles != null) {
          for (File currentDirFile : currentDirFiles) {
            if (currentDirFile.isDirectory() && currentDirFile.list().length == 0) {
              if (!currentDirFile.delete()) {
                return false;
              }
            }
          }
        }
        return true;
      }
    }

    return false;
  }

  public void cd(String pathStr) {
    if (pathStr.equals("")) {
      this.currentPath = Paths.get(System.getProperty("user.home"));
      return;
    } else if (pathStr.equals("..")) {
      if (this.currentPath.getParent() == null) {
        System.out.println("CurrentDir:" + this.currentPath.toString());
        return;
      }
      // if there is parent directory
      this.currentPath = this.currentPath.getParent();
      System.out.println("Current Dir:" + this.currentPath.toString());
      return;
    }

    Path path = Paths.get(pathStr);
    // Make the relative path absolute path by merging it with the current directory
    path = makeAbsoluteIfNot(path);

    if (isValidPath(path)) {
      this.currentPath = path;
    }
    System.out.println("Current Dir:" + this.currentPath.toString());
  }

  public boolean touch(String pathStr) {
    // path to the file
    Path path = Paths.get(pathStr);

    // If the file directory is the currentPath (parentPath == null)
    // or if it was not absolute path, Then Merge it with the currentPath
    path = makeAbsoluteIfNot(path);

    // path to the file directory
    Path parentPath = path.getParent();

    if (isValidPath(parentPath)) {
      File file = new File(path.toString());
      try {
        if (file.createNewFile()) {
          System.out.println("File created: " + file.getName());
        } else {
          System.out.println("File already exists.");
        }
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }
    } else {
      return false;
    }
    return true;
  }

  // cp
  public boolean cp(String firstFile, String secondFile) {
    Path path1 = Paths.get(firstFile);
    path1 = makeAbsoluteIfNot(path1);

    if (!isValidPath(path1.getParent()) && !Files.isDirectory(path1)) {
      System.out.println("First path is not valid");
      return false;
    }

    Path path2 = Paths.get(secondFile);
    path2 = makeAbsoluteIfNot(path2);

    if (!isValidPath(path2.getParent()) && !Files.isDirectory(path2)) {
      System.out.println("Second path is not valid");
      return false;
    }

    try {
      // Copy firstFile content into SecondFile
      Files.copy(path1, path2, StandardCopyOption.REPLACE_EXISTING);
      System.out.println(path1.getFileName() + " content has been copied into " + path2.getFileName());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean cp_r(String firstDir, String secondDir) {
    Path path1 = Paths.get(firstDir);
    path1 = makeAbsoluteIfNot(path1);

    if (!isValidPath(path1)) {
      System.out.println("First path is not valid");
      return false;
    }

    Path path2 = Paths.get(secondDir);
    path2 = makeAbsoluteIfNot(path2);

    if (!isValidPath(path2)) {
      System.out.println("Second path is not valid");
      return false;
    }

    try {
      // Copy the firstDir and its contents to secondDir
      FileUtils.copyDirectory(path1.toFile(), path2.toFile());
      System.out.println(path1.getFileName() + " content has been copied into " +
          path2.getFileName());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  // remind some thing
  public String cat(String pathFile) {
    try {
      StringBuilder text = new StringBuilder();
      // FileReader file = new FileReader(currentPath.toString() + "/"+ fileName);
      FileReader file = new FileReader(makeAbsoluteIfNot(pathFile));
      try (BufferedReader reader = new BufferedReader(file)) {
        String line;
        while ((line = reader.readLine()) != null) {
          text.append(line).append(System.lineSeparator());
        }
      }
      text = new StringBuilder(text.substring(0, text.length() - 1));
      return text.toString();
    } catch (IOException e) {
      return e.getMessage();
    }
  }

  public void wc(String filePath) {
    int lineCount = 0;
    int wordCount = 0;
    int charCount = 0;
    try (BufferedReader reader = new BufferedReader(new FileReader(makeAbsoluteIfNot(filePath)))) {
      String line;
      while ((line = reader.readLine()) != null) {
        lineCount++;
        charCount += line.length();
        String[] words = line.split("\\s+"); // Split the line into words using whitespace as a delimiter
        wordCount += words.length;
      }
      System.out.println(lineCount + " lines, " + wordCount + " words, " + charCount + " characters, ");
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }

  }

  // print res for any command ex --> ls, ls -r
  public void printOutputForAnyCommand() {
    if (!outputs.isEmpty()) {
      for (String output : outputs) {
        System.out.print(output + " ");
      }
      System.out.println();
    }
  }

  // print res reversed
  public void printOutputForAnyCommandReversed() {
    if (!outputs.isEmpty()) {
      Collections.reverse(outputs);
      for (String output : outputs) {
        System.out.print(output + " ");
      }
      System.out.println();
    }
  }

  // This method will choose the suitable command method to be called
  public void chooseCommandAction() {
    String commandName = this.parser.getCommandName();
    switch (commandName) {
      case "echo" -> {
        String result = echo();
        System.out.println(result);
      }
      case "pwd" -> {
        String result = pwd();
        System.out.println(result);
      }
      case "ls" -> {
        Boolean result = ls();
        ArrayList<String> args = new ArrayList<String>();
        args = parser.getArgs();
        if (result && args.size() >= 1 && args.get(0).equals("-r")) {
          printOutputForAnyCommandReversed();
        } else if (result && args.size() == 0) {
          printOutputForAnyCommand();
        } else {
          System.out.println("Error: Unable to list files.");
        }
      }
      case "mkdir" -> {
        // Check for arguments and execute mkdir
        ArrayList<String> args = parser.getArgs();

        if (!args.isEmpty()) {
          boolean success = true;
          for (String arg : args) {
            Path path = Paths.get(arg);
            path = makeAbsoluteIfNot(path);
            if (isValidPath(path.getParent())) {
              boolean result = mkdir(path);
              if (!result) {
                System.out.println("Error: Unable to create directory: " + arg);
                success = false;
              }
            }
          }
          if (success) {
            System.out.println("All directories created successfully.");
          }
        } else {
          System.out.println("Error: No directory names specified for mkdir.");
        }

      }
      case "rm" -> {
        // Check for arguments
        ArrayList<String> args = parser.getArgs();
        if (args.size() == 1) {
          boolean result = rm(args.get(0));
          if (result) {
            System.out.println("File removed.");
          } else {
            System.out.println("Error: Unable to remove file.");
          }
        } else {
          System.out.println("Error: Invalid number of arguments for rm.");
        }
      }
      case "cd" -> {
        ArrayList<String> args = parser.getArgs();
        if (args.isEmpty()) {
          cd(""); // Call cd with an empty string
        } else if (args.size() == 1) {
          cd(args.get(0));
        } else {
          System.out.println("Error: Invalid number of arguments for cd.");
        }
      }
      case "touch" -> {
        // Check for arguments and execute touch
        ArrayList<String> args = parser.getArgs();
        if (args.size() == 1) {
          boolean result = touch(args.get(0));
          if (!result) {
            System.out.println("Error: Unable to create file.");
          }
        } else {
          System.out.println("Error: Invalid number of arguments for touch.");
        }
      }
      case "cp" -> {
        ArrayList<String> args = parser.getArgs();

        if (args.size() == 3 && args.get(0).equals("-r")) {
          cp_r(args.get(1), args.get(2));
        } else if (args.size() == 2) {
          cp(args.get(0), args.get(1));
        } else {
          System.out
                  .println("Error: Invalid number of arguments for cp\n(Should be just two or 3 if you're using cp -r).");
        }
      }
      case "cat" -> {
        ArrayList<String> args = parser.getArgs();
        if (args.size() == 1) {
          String result = cat(args.get(0));
          if (result != null) {
            System.out.println(result);
          } else {
            System.out.println("Error: Unable to read the file.");
          }
        } else {
          System.out.println("Error: Invalid number of arguments for cat.");
        }
      }
      case "rmdir" -> {
        ArrayList<String> args = parser.getArgs();
        if (args.size() == 1) {
          boolean result = rmdir(args.get(0));
          if (result) {
            System.out.println("Directory removed.");
          } else {
            System.out.println("Error: Directory cannot be removed or is not empty.");
          }
        } else {
          System.out.println("Error: Invalid number of arguments for rmdir.");
        }
      }
      case "history" -> displayCommandHistory();
      case "wc" -> {
        ArrayList<String> args = parser.getArgs();
        if (args.size() == 1) {
          wc(args.get(0));
        } else {
          System.out.println("Error: Invalid number of arguments for wc.");
        }
      }
      case null, default -> System.out.println("Error: Unknown command.");
    }
  }

  public static void main(String[] args) {
    // crete object type parser to divide the input to command and args
    Scanner scanner = new Scanner(System.in);
    Terminal terminal = new Terminal();
    while (true) {
      System.out.print("@" + terminal.getCurrentPath() + ">");
      String line = scanner.nextLine();
      // Compares this String to another String, ignoring case considerations
      if (line.equalsIgnoreCase("exit")) {
        // Exit the program
        break;
      } else {
        // Execute the command
        terminal.parse(line);
        // ArrayList<String> arg = new ArrayList<String>();
        // arg = terminal.parser.getArgs();
        // for (String string : arg) {
        // System.out.println(string);
        // }
        terminal.chooseCommandAction();
      }
    }

    scanner.close();
    // System.out.println(terminal.rm("I:\\VSprojects\\ProjectsOfThirdYear\\DataCom\\file\\fff.txt"));
    // System.out.println(terminal.cat("I:\\VSprojects\\ProjectsOfThirdYear\\DataCom\\file\\FileString.txt"));

    // terminal.mkdir("fdfdf");
    // while (true) {

    // System.out.println("Enter your command : \n");

    // terminal.cd(line);
    // }
    // System.out.println(terminal.mkdir("exampleDirectory"));

    // terminal.outputs.clear();
  }
}