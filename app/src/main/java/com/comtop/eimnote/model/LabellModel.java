package com.comtop.eimnote.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LabellModel implements Serializable{

    @SerializedName("tagId")
    private Integer tagId;
    @SerializedName("name")
    private String name;
    private boolean selector;

    public LabellModel(Integer tagId, String name) {
        this.tagId = tagId;
        this.name = name;
        this.selector = false;
    }

    public LabellModel(Integer tagId, String name, boolean selector) {
        this.tagId = tagId;
        this.name = name;
        this.selector = selector;
    }

    public boolean isSelector() {
        return selector;
    }

    public void setSelector(boolean selector) {
        this.selector = selector;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LabellModel{" +
                "tagId='" + tagId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
