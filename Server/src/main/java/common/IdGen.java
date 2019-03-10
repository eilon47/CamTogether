package common;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class IdGen {
    private static final int MAX_LENGTH = 50;
    public static String generate(String input){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown"
                    + " for incorrect algorithm: " + e);
            return null;
        }
    }
    public static String generate(String item, String forbidden){
        String id = generate(item);
        while (id.equals(forbidden) && item.length() < MAX_LENGTH){
            item = item.concat(item);
            id = generate(item);
        }
        return id;
    }

    public static void main(String[] args){
        String e = "eilon47";
        String id = IdGen.generate(e);
        String id2 = IdGen.generate(e, id);
        System.out.println(id + "\n" + id2);
    }
}
