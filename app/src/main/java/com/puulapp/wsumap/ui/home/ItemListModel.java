package com.puulapp.wsumap.ui.home;

import java.util.Map;

public class ItemListModel {
    public String name, image, description, lat, lng, key;

    public ItemListModel(String name, String image, String description, String lat, String lng, String key) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.key = key;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
