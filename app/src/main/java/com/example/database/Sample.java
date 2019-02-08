package com.example.database;

public enum  Sample {
    ID(Integer.valueOf(0), "ID"),
    FILE_ID(Integer.valueOf(1), "file_id"),
    COMPOSITION_ID(Integer.valueOf(2), "composition_id");

    private Integer num;
    private String name;

    private Sample(Integer num, String name){
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
