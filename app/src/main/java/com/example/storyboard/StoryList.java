package com.example.storyboard;

import android.graphics.Bitmap;

public class StoryList {
    private long id;
    private String name;
    private Bitmap thumbnail;

    /*
     * 空のコンストラクタ
     */
    public StoryList(){

    }

    public StoryList(long id, String name, Bitmap thumbnail){
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
    }


    public void setId(long id){
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThumbnail(Bitmap thumbnail){
        this.thumbnail = thumbnail;
    }

    public long getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public Bitmap getThumbnail(){
        return thumbnail;
    }

}
