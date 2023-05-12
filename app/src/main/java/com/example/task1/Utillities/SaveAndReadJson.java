package com.example.task1.Utillities;

import android.util.Log;

import com.example.task1.Models.Record;
import com.example.task1.Models.RecordList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SaveAndReadJson {

    private static SaveAndReadJson instance;
    private RecordList recordList;
    private SaveAndReadJson()
    {
        String fromSP = MySP.getInstance().getString("RecordsList","");
        if(!fromSP.isEmpty())
        {
            recordList = new Gson().fromJson(fromSP, RecordList.class);
            Log.d("Json: ", ""+fromSP);
        }
        else
            recordList = new RecordList();
    }

    public RecordList getRecordList() {
        return recordList;
    }

    public static void init()
    {
        if(instance == null)
            instance = new SaveAndReadJson();
    }

    public static SaveAndReadJson getInstance() {return instance;}

    public boolean checkIfCanAddAndSave(int score)
    {
        return recordList.checkAdd(score);
    }

    public void saveToJson(int score, double lan,double lon,String city,String country,String difficulty,
                           String type)
    {
        Record r = new Record(score);
        r.setLat(lan);
        r.setLon(lon);
        r.setCity(city);
        r.setCountry(country);
        r.setDifficulty(difficulty);
        r.setType(type);
        recordList.getRecords().add(r);
        Collections.sort(recordList.getRecords(), new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                return (int)(o2.getScore()- o1.getScore());
            }
        });
        String json = new Gson().toJson(recordList);
        MySP.getInstance().putString("RecordsList",json);
    }
}
