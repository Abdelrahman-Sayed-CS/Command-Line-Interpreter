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

public class Terminal {
    private Parser parser;
    private ArrayList<String> outputs;
    // Start your path as your current user path directory
    private Path currentPath;

    public Terminal() {
        outputs = new ArrayList<>();
        parser = new Parser();
        currentPath = Paths.get(System.getProperty("user.home"));
    }

    // Implement each command in a method, for example:
    public void parse(String input) {
        parser.parse(input);
        parser.printArguments();
    }

    public String getCurrentPath() {
        return currentPath.toString();
    }

    // echo
    public String echo() {
        return parser.arguments.get(0);
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
                for (File f : files) {
                    if (f.isFile()) {
                        outputs.add(f.getName());
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }

    // mkdir
    public boolean mkdir(String dirName) {
        File directoryFile = new File(dirName);
        if (!directoryFile.exists()) {
            if (!directoryFile.mkdir()) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    // remove file filePath -->
    public Boolean rm(String filePath) {
        File file = new File(filePath);
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

    // To check if a string represents a valid file path or no
    public Boolean isValidPath(String path) {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }

    public Boolean isValidPath(Path path) {
        return Files.exists(path) && Files.isDirectory(path);
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
        if (!path.isAbsolute()) {
            // absolute path --> I:\VSprojects\ProjectsOfThirdYear\DataCom\file
            // relative path --> \ProjectsOfThirdYear\DataCom\file
            // Make the relative path absolute path by merging it with the current directory
            path = Paths.get(this.currentPath.toString(), pathStr);
        }

        if (isValidPath(path)) {
            this.currentPath = path;
        }
        System.out.println("Current Dir:" + this.currentPath.toString());
    }

    public boolean touch(String pathStr) {
        // path to the file
        Path path = Paths.get(pathStr);
        // path to the file directory
        Path parentPath = path.getParent();

        // If the file directory is the currentPath(parentPath == null)
        // or if it was not absolute path, then merge it with the currentPath
        if (parentPath == null || !parentPath.isAbsolute()) {
            // Make the relative path absolute path by merging it with the current directory
            path = Paths.get(this.currentPath.toString(), path.toString());
            parentPath = path.getParent();
        }

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

    // remind some thing
    public String cat(String pathFile) {
        try {
            StringBuilder text = new StringBuilder();
            // FileReader file = new FileReader(currentPath.toString() + "/"+ fileName);
            FileReader file = new FileReader(pathFile);
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

    // print res for any command ex --> ls, ls -r
    public void printOutputForAnyCommand() {
        if (outputs.size() > 0) {
            for (int i = 0; i < outputs.size(); i++) {
                System.out.print(outputs.get(i) + " ");
            }
            System.out.println();
        }
    }

    // This method will choose the suitable command method to be called
    public void chooseCommandAction() {

    }

    public static void main(String[] args) {
        // crete object type parser to divide the input to command and args
        Scanner scanner = new Scanner(System.in);
        Terminal terminal = new Terminal();
        while (true) {
            System.out.print("@" + terminal.getCurrentPath() + ">");
            String line = scanner.nextLine();
            terminal.cd(line);
        }
        // System.out.println(terminal.rm("I:\\VSprojects\\ProjectsOfThirdYear\\DataCom\\file\\fff.txt"));
        // scanner.close();
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

// echo + pwd + ls + ls -r + mkdir + touch + cd