package com.example.database;

public enum Tag {
    SCENERY(Integer.valueOf(1), "scenery"),
    PORTRAIT(Integer.valueOf(2), "portrait"),
    BUILDING(Integer.valueOf(3), "building"),
    OTHER(Integer.valueOf(4), "other");

    private Integer num;
    private String name;

    private Tag(Integer num, String name){
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
