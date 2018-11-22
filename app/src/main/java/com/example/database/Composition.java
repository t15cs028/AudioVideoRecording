package com.example.database;

public enum Composition {
    ID(Integer.valueOf(0), "ID"),
    NAME(Integer.valueOf(1), "name"),
    DESCRIPTION(Integer.valueOf(2), "description"),
    FILE_ID(Integer.valueOf(3), "fileID"),
    THUMB_ID(Integer.valueOf(4), "thumbID"),
    TAG(Integer.valueOf(5), "tag");



    private Integer num;
    private String name;

    private Composition(Integer num, String name){
        this.num = num;
        this.name = name;
    }

    public Integer getNumber(){
        return this.num;
    }

    public String getName(){
        return this.name;
    }
}
