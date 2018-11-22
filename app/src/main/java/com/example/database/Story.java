package com.example.database;

public enum Story {
    ID(Integer.valueOf(0), "ID"),
    TURN(Integer.valueOf(1), "turn"),
    NAME(Integer.valueOf(2), "name"),
    COMPOSITION_ID(Integer.valueOf(3), "compositionID"),
    DESCRIPTION(Integer.valueOf(4), "description"),
    FILE_URL(Integer.valueOf(5), "fileURL"),
    STORIES_ID(Integer.valueOf(6), "storiesID");



    private Integer num;
    private String name;

    private Story(Integer num, String name){
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
