package com.example.storyboard;

import android.graphics.Bitmap;

public class LayoutBlock {
    private long id;
    private String name;
    private String description;
    private Bitmap thumbnail;

    /*
     * 空のコンストラクタ
     */
    public LayoutBlock(){

    }

    public LayoutBlock(long id, Bitmap thumbnail, String name, String description){
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.description = description;
    }

    public void setId(long id){
        this.id = id;
    }

    public void setThumbnail(Bitmap thumbnail){
        this.thumbnail = thumbnail;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setDescription(String description){
        this.description = description;
    }

    public long getId(){
        return id;
    }

    public Bitmap getThumbnail(){
        return thumbnail;
    }

    public String getName() {
        return name;
    }


    public String getDescription(){
        return description;
    }

}
