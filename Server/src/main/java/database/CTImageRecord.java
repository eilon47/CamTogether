package database;


import java.io.ByteArrayInputStream;

public class CTImageRecord {
    private String name;
    private byte[] data;
    private String user_id;
    private String album;
    private int dataSize;
    private int length;
    private int width;



    public CTImageRecord(){}

    public CTImageRecord(String name, byte[] data, String user_id, String album, int dataSize, int length, int width) {
        this.name = name;
        this.data = data;
        this.user_id = user_id;
        this.album = album;
        this.dataSize = dataSize;
        this.length = length;
        this.width = width;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

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
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
        this.dataSize = data.length;

    }



}
