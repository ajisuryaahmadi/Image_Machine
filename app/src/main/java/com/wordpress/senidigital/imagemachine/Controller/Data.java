package com.wordpress.senidigital.imagemachine.Controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class Data {
    private String id, name, spec, last_main;
    String pic1, pic2, pic3, pic4, pic5;
    int img_count;

    public Data() {
    }

    public Data(String id, String name, String spec, String last_main, int img_count, String pic1, String pic2, String pic3, String pic4, String pic5) {
        this.id = id;
        this.name = name;
        this.spec = spec;
        this.last_main = last_main;
        this.img_count = img_count;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.pic3 = pic3;
        this.pic4 = pic4;
        this.pic5 = pic5;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getLast_Main(){
        return last_main;
    }

    public void setLast_main(String last_main) {
        this.last_main = last_main;
    }

    public String getPic1(){
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2(){
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getPic3(){
        return pic3;
    }

    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }

    public String getPic4(){
        return pic4;
    }

    public void setPic4(String pic4) {
        this.pic4 = pic4;
    }

    public String getPic5(){
        return pic5;
    }

    public void setPic5(String pic5) {
        this.pic5 = pic5;
    }
}