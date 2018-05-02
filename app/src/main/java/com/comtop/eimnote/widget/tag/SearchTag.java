package com.comtop.eimnote.widget.tag;

import java.io.Serializable;

/**
 * Created by chenxiaojian on 16/3/29.
 */
public class SearchTag implements Serializable {

    private String id;
    private String tagName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }


    @Override
    public String toString() {
        return "SearchTag{" +
                "id='" + id + '\'' +
                ", tagName='" + tagName + '\'' +
                '}';
    }
}
