package commands;

public class CommandRequest {
    protected CommandsEnum cmd;
    protected String request;
    protected String from;

    public CommandRequest(CommandsEnum cmd, String req, String from){
        this.cmd = cmd;
        this.request = req;
        this.from = from;
    }

    public CommandsEnum getCmd() {
        return cmd;
    }

    public void setCmd(CommandsEnum cmd) {
        this.cmd = cmd;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String toString(){
        return this.getFrom() + "#" + this.getCmd().getValue() + "#" + getRequest();
    }

    public static CommandRequest fromString(String req){
        String[] res = req.split("#");
        String from = res[0];
        String request = res[2];
        CommandsEnum cmd = CommandsEnum.toCommandsEnum(res[1]);
        return new CommandRequest(cmd, request, from);
    }
}
