package database;


import java.io.ByteArrayInputStream;

public class CTImageRecord {
    private String name;
    private ByteArrayInputStream data;
    private String user_id;
    private String album;
    int size;

    public CTImageRecord(String name, byte[] data, String user_id, String album) {

        this.name = name;
        this.data = new ByteArrayInputStream(data);
        this.user_id = user_id;
        this.album = album;
        int size = data.length;
    }

    public CTImageRecord(){}

    // USER ID
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    // ALBUM
    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    //NAME
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //DATA
    public ByteArrayInputStream getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = new ByteArrayInputStream(data);
        this.size = data.length;

    }

    //SIZE
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


}
