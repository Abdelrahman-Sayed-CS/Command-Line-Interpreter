import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Terminal {
    private String directory;
    private Parser parser;
    private ArrayList<String> outputs; 
   
    public Terminal(String input){
        outputs = new ArrayList<>(); 
        parser = new Parser();
        directory = System.getProperty("user.dir");
        parser.parse(input);
    }
    //Implement each command in a method, for example:

    // echo 
    public String echo(){
        return parser.arguments.get(0);
    }
    // pwd
    public String pwd()
    {
        return directory;
    }
    // ls + ls -r
    public Boolean ls(){
        //** use this for ls 
        // Collections.sort(terminal.outputs);
         //** use this for ls -r
        // Collections.sort(terminal.outputs, Collections.reverseOrder());
        File dirPath = new File(directory);
        if(isValidPath(directory)){
            File[] files = dirPath.listFiles();
            if(files != null){
                for (File f : files) {
                    if(f.isFile()){
                        outputs.add(f.getName());
                    }
                }
            }else{
                return false;
            }
        } 
        
        return true;
    }
    // mkdir 
    public boolean mkdir(String dirName){
        File directoryFile = new File(dirName);
        if(!directoryFile.exists()){
            if(!directoryFile.mkdir()){
                return false;
            }
        }else{
            return false;
        }
        return true;
    }
    // To check if a string represents a valid file path or no
    public Boolean isValidPath(String path){
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }
    // print res for any command ex --> ls, ls -r
    public void printOutputForAnyCommand(){
         if(outputs.size() > 0){
            for (int i = 0; i < outputs.size(); i++) {
                System.out.print(outputs.get(i) + " ");
            }
            System.out.println();
        }
    }
    // public void cd(){}
    // ...
    //This method will choose the suitable command method to be called
    public void chooseCommandAction(){}
    public static void main(String[] args)
    {
        // crete object type parser to divide the input to command and args
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your command : \n");
        String line = scanner.nextLine();


        Terminal terminal = new Terminal(line);
        // System.out.println(terminal.mkdir("exampleDirectory"));
        
        // terminal.outputs.clear();

        
    }
}
