package com.example.a26740.todo;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class Event extends DataSupport implements Serializable {
    private int id;
    private String name;
    private boolean checked;
    private int you;

    public int getYou() {
        return you;
    }

    public void setYou(int you) {
        this.you = you;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isChecked(){
        return checked;
    }
    public void setChecked(boolean checked){
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
