package com.example.database;

public enum Stories {
    ID(Integer.valueOf(0), "ID"),
    DATE(Integer.valueOf(1), "date"),
    NAME(Integer.valueOf(2), "name"),
    FILE_URL(Integer.valueOf(3), "file_url");



    private Integer num;
    private String name;

    private Stories(Integer num, String name){
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
