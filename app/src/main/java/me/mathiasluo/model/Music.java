package me.mathiasluo.model;

/**
 * Created by mathiasluo on 16-3-31.
 */
public class Music {

    private String name;
    private String music_id;
    private String path;

    public Music(String name, String music_id, String path) {
        this.name = name;
        this.music_id = music_id;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMusic_id() {
        return music_id;
    }

    public void setMusic_id(String music_id) {
        this.music_id = music_id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
