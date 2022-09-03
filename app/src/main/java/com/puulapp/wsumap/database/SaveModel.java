package com.puulapp.wsumap.database;

public class SaveModel {
    String item_image, item_name, item_desc, item_lat, item_lng, item_key, item_campus;
    int id;

    public SaveModel(int id, String item_image, String item_name, String item_desc, String item_lat, String item_lng, String item_key, String item_campus) {
        this.item_image = item_image;
        this.item_name = item_name;
        this.item_desc = item_desc;
        this.item_lat = item_lat;
        this.item_lng = item_lng;
        this.item_key = item_key;
        this.item_campus = item_campus;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem_campus() {
        return item_campus;
    }

    public void setItem_campus(String item_campus) {
        this.item_campus = item_campus;
    }

    public String getItem_key() {
        return item_key;
    }

    public void setItem_key(String item_key) {
        this.item_key = item_key;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public String getItem_lat() {
        return item_lat;
    }

    public void setItem_lat(String item_lat) {
        this.item_lat = item_lat;
    }

    public String getItem_lng() {
        return item_lng;
    }

    public void setItem_lng(String item_lng) {
        this.item_lng = item_lng;
    }
}
