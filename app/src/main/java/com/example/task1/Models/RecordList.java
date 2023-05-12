package com.example.task1.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RecordList {
    @SerializedName("name")
    private String name="";
    @SerializedName("records")
    private ArrayList<Record> records;
    @SerializedName("size")
    private int size;
    public RecordList()
    {
        records = new ArrayList<>();
        size=0;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }

    public boolean checkAdd(int score)
    {
        if(size < 10) {
            size++;
            return true;
        }
        if(score > records.get(size-1).getScore())
        {
            records.remove(size-1);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "RecordList{" +
                "name='" + name + '\'' +
                ", records=" + records +
                ", size=" + size +
                '}';
    }
}
