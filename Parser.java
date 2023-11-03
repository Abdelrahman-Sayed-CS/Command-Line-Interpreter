import java.util.ArrayList;


class Parser {
  String commandName;
  ArrayList<String> arguments;

  // This method will divide the input into commandName and args
  // where "input" is the string command entered by the user
  public Parser() {
    arguments = new ArrayList<String>();
  }
  // if closed equal false that is mean exist single cot and 
  // closed still false un till exist another single cot 
  // ex: 'fdfdfdf fdfd.txt' 
  private int parseSingleCot(int i, String input){
    String arg = "";
    int index = -1;
     for (int j = i; j < input.length(); j++) {
       if(input.charAt(j) != '\'')
        {
          arg+=input.charAt(j);
        }
        else{
          arguments.add(arg);
          arg = "";
          index = j+1;
          break;
        }
     }
     return index;
  }
  
   public boolean parse(String input) {
    commandName = "";
    arguments.clear();
    if (input.indexOf(" ") == -1) {
      commandName = input;
      return true;
    }
    commandName = input.substring(0, input.indexOf(" "));
    CheckedCommand c = new CheckedCommand();
    // check if this commandName is exist or no
    if (!c.commandIsExist(commandName)) {
      return false;
    }
    // cut the arguments to strings and store it in array args
    if (input.length() > input.indexOf(" ") + 1) {
      int i = input.indexOf(" ") + 1;
      String arg = "";
      while (i < input.length())
      {
        if(input.charAt(i) == '\'')
        {
          System.out.println("single cot : " + i);
          int k = parseSingleCot(i+1, input);
          if(k != -1)
            i = k; 
          else 
          {
            break;
          }
          continue;
        }
        if (input.charAt(i) != ' ')
          arg += input.charAt(i);
        else {
          arguments.add(arg);
          arg = "";
        }
        i++;
      }
      if (!arg.equals(""))
        arguments.add(arg);
    }
    return true;
  }
  public String getCommandName() {
    return commandName;
  }

  public ArrayList<String> getArgs() {
    return arguments;
  }
}