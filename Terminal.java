import java.util.Scanner;

public class Terminal {

    Parser parser;
    public Terminal(String input){
        parser = new Parser();
        parser.parse(input);
    }
    //Implement each command in a method, for example:

    // echo 
    public String echo(){
        return parser.arguments.get(0);
    }
    public String pwd()
    {
        String currentPath = System.getProperty("user.dir");
        return currentPath;
    }
    
    public void cd(String[] args){}
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
        // System.out.println(terminal.echo());
        System.out.println(terminal.pwd());
       
        // System.out.println("line : " + line + "\n");
    }
}
