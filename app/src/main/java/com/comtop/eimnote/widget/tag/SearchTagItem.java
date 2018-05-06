package com.comtop.eimnote.widget.tag;

import android.content.Context;

import com.comtop.eimnote.model.LabellModel;


/**
 * Created by chenxiaojian on 16/3/29.
 */
public class SearchTagItem {

    private LabellModel searchTag;
    private SearchItemView searchItemView;

    public SearchTagItem(Context context) {
        searchItemView = new SearchItemView(context);
    }

    public LabellModel getSearchTag() {
        return searchTag;
    }

    public void setSearchTag(LabellModel searchTag) {
        this.searchTag = searchTag;
    }

    public SearchItemView getSearchItemView() {
        return searchItemView;
    }

}
