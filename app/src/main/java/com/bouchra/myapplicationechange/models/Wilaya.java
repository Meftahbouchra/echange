package com.bouchra.myapplicationechange.models;

public class Wilaya {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Wilaya() {
    }

    public Wilaya(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
