package com.example.storyboard;

import android.graphics.Bitmap;

public class Block {
    private long id;
    private String name;
    private Bitmap thumbnail;
    private Bitmap composition;
    private Bitmap record;

    /*
     * 空のコンストラクタ
     */
    public Block(){

    }

    public Block(long id, String name, Bitmap thumbnail, Bitmap record){
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.record = record;
    }

    public Block(long id, String name, Bitmap thumbnail, Bitmap composition, Bitmap record){
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.composition = composition;
        this.record = record;
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

    public void setComposition(Bitmap composition){
        this.composition = composition;
    }

    public void setRecord(Bitmap record) { this.record = record; }

    public long getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public Bitmap getThumbnail(){
        return thumbnail;
    }

    public Bitmap getComposition(){
        return composition;
    }

    public Bitmap getRecord(){
        return record;
    }
}
