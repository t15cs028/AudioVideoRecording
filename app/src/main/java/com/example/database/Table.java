package com.example.database;

import java.io.Serializable;

public enum Table implements Serializable {
    STORIES(Integer.valueOf(0), "stories"),
    STORY(Integer.valueOf(1), "story"),
    COMPOSITION(Integer.valueOf(2), "composition");



    private Integer num;
    private String name;

    private Table(Integer num, String name){
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
