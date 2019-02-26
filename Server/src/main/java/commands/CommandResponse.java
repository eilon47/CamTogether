package commands;

public class CommandResponse {

    protected CommandsEnum cmd;
    protected String responseMsg;
    protected boolean isSucceeded;
    protected String to;

    public CommandResponse(CommandsEnum cmd, String responseMsg, boolean isSucceeded, String to) {
        this.cmd = cmd;
        this.responseMsg = responseMsg;
        this.isSucceeded = isSucceeded;
        this.to = to;
    }


    public CommandsEnum getCmd() {
        return cmd;
    }

    public void setCmd(CommandsEnum cmd) {
        this.cmd = cmd;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public boolean isSucceeded() {
        return isSucceeded;
    }

    public void setSucceeded(boolean succeeded) {
        isSucceeded = succeeded;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String toString(){
        return this.getTo() + "#" + this.getCmd().getValue() + "#" +
                this.getResponseMsg() + "#" + Boolean.toString(this.isSucceeded());
    }

    public static CommandResponse fromString(String str){
        String res[] = str.split("#");
        CommandsEnum cmd = CommandsEnum.toCommandsEnum(res[1]);
        String to = res[0];
        String msg = res[2];
        boolean succ = Boolean.getBoolean(res[3]);
        return new CommandResponse(cmd, msg, succ, to);
    }
}
