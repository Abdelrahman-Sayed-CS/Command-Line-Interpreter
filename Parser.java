import java.util.ArrayList;

class Parser {
    String commandName;
    ArrayList<String> arguments = new ArrayList<>();
    //This method will divide the input into commandName and args
    //where "input" is the string command entered by the user
    public boolean parse(String input)
    {
        commandName = input.substring(0, input.indexOf(" "));
        CheckedCommand c = new CheckedCommand();
        // check if this commandName is exist or no
        if(!c.commandIsExist(commandName)){return false;}

        // cut the arguments to strings and store it in array args 
        String arg = "";
        for (int i = input.indexOf(" ")+1; i < input.length(); i++) {
            if(input.charAt(i) != ' ')arg+=input.charAt(i);
            else{
                arguments.add(arg);
                arg = "";
            }
        }
        if(!arg.equals(""))
            arguments.add(arg);

        return false;
    }
    public String getCommandName()
    {
        return commandName;
    }
    public ArrayList<String> getArgs()
    {
        return arguments;
    }
    public void printArguments(){
        for (int i = 0; i < arguments.size(); i++) {
            System.out.print(arguments.get(i) + " ");
        }
        System.out.println();
    }
}