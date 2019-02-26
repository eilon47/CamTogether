package commands;

public enum CommandsEnum {
    NEW_PHOTO("10"),
    GET_PHOTO("11"),
    REMOVE_PHOTO("12"),

    NEW_ALBUM("20"),
    REMOVE_ALBUM("21"),
    ADD_USER_TO_ALBUM("22"),
    REMOVE_USER_FROM_ALBUM("23"),
    ADD_MANAGER_TO_ALBUM("24"),
    SET_ALBUM_LOCATION("25"),
    SET_ALBUM_TIME("26"),

    ADD_NEW_USER("30"),
    DELETE_USER("31"),

    EXIY("0");
    private String value;

    CommandsEnum(String s){
        this.value = s;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getValue(){
        return value;
    }

    public static CommandsEnum toCommandsEnum(String s){
        for (CommandsEnum command : CommandsEnum.values()){
            if(command.getValue().equals(s)){
                return command;
            }
        }
        return null;
    }

}
