import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Terminal {
    private String directory;
    private Parser parser;
    private ArrayList<String> outputs;
    // Start your path as your current user path directory
    private Path currentPath = Paths.get(System.getProperty("user.home"));

    public Terminal() {
        return;
    }

    public Terminal(String input) {
        outputs = new ArrayList<>();
        parser = new Parser();
        directory = System.getProperty("user.dir");
        parser.parse(input);
    }
    // Implement each command in a method, for example:

    // echo
    public String echo() {
        return parser.arguments.get(0);
    }

    // pwd
    public String pwd() {
        return directory;
    }

    // ls + ls -r
    public Boolean ls() {
        // ** use this for ls
        // Collections.sort(terminal.outputs);
        // ** use this for ls -r
        // Collections.sort(terminal.outputs, Collections.reverseOrder());
        File dirPath = new File(directory);
        if (isValidPath(directory)) {
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

    // To check if a string represents a valid file path or no
    public Boolean isValidPath(String path) {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }

    public void cd(String pathStr) {
        if (pathStr.equals("..")) {
            if (this.currentPath.getParent() == null) {
                System.out.println("Hey:" + this.currentPath.toString());
                return;
            }
            // if there is parent directory
            this.currentPath = this.currentPath.getParent();
            System.out.println("Hey:" + this.currentPath.toString());
            return;
        }

        Path path = Paths.get(pathStr);
        if (!path.isAbsolute()) {
            // Make the relative path absolute path by merging it with the current directory
            path = Paths.get(this.currentPath.toString(), pathStr);
        }

        if (Files.exists(path) && Files.isDirectory(path)) {
            this.currentPath = path;
        }

        System.out.println(this.currentPath.toString());
    }

    public boolean touch(String pathStr) {

        Path path = Paths.get(pathStr);
        Path parentPath = path.getParent();

        if (!parentPath.isAbsolute()) {
            // Make the relative path absolute path by merging it with the current directory
            parentPath = Paths.get(this.currentPath.toString(), parentPath.toString());
        }

        if (Files.exists(parentPath) && Files.isDirectory(parentPath)) {
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

    // print res for any command ex --> ls, ls -r
    public void printOutputForAnyCommand() {
        if (outputs.size() > 0) {
            for (int i = 0; i < outputs.size(); i++) {
                System.out.print(outputs.get(i) + " ");
            }
            System.out.println();
        }
    }

    // public void cd(){}
    // ...
    // This method will choose the suitable command method to be called
    public void chooseCommandAction() {
    }

    public static void main(String[] args) {
        // crete object type parser to divide the input to command and args
        Scanner scanner = new Scanner(System.in);
        Terminal terminal = new Terminal();
        while (true) {

            System.out.println("Enter your command : \n");
            String line = scanner.nextLine();

            terminal.cd(line);
        }
        // System.out.println(terminal.mkdir("exampleDirectory"));

        // terminal.outputs.clear();
    }
}
