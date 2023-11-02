import java.util.HashMap;
import java.util.Map;

public class CheckedCommand {
    private Map<String, Boolean> commands = new HashMap<>();

    public CheckedCommand() {
        commands.put("echo", true);
        commands.put("pwd", true);
        commands.put("cd", true);
        commands.put("ls", true);
        commands.put("mkdir", true);
        commands.put("rmdir", true);
        commands.put("touch", true);
        commands.put("cp", true);
        commands.put("rm", true);
        commands.put("cat", true);
        commands.put(">", true);
        commands.put(">>", true);
        commands.put("history", true);
        commands.put("wc", true);
    }
    public Boolean commandIsExist(String command){
        return commands.get(command);
    }
}
