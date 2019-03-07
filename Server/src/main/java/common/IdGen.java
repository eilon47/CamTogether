package common;

import java.util.UUID;

public class IdGen {

    public static String generate(String item){
        return  UUID.fromString(item).toString();
    }
    public static String generate(String item, String forbidden){
        String id = generate(item);
        while (id.equals(forbidden)){
            item = item.concat(item);
            id = generate(item);
        }
        return id;
    }
}
