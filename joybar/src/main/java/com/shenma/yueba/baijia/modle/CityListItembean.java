package com.shenma.yueba.baijia.modle;

import java.io.Serializable;

public class CityListItembean implements Serializable {

    private String Id;
    private String Name;
    private String Key;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
