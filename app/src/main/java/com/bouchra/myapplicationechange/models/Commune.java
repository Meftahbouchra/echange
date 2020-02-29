package com.bouchra.myapplicationechange.models;

public class Commune {
    private int id , wilaya_id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWilaya_id() {
        return wilaya_id;
    }

    public void setWilaya_id(int wilaya_id) {
        this.wilaya_id = wilaya_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Commune() {
    }

    public Commune(int id, int wilaya_id, String name) {
        this.id = id;
        this.wilaya_id = wilaya_id;
        this.name = name;
    }
}
