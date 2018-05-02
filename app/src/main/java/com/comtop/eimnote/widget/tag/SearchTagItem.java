package com.comtop.eimnote.widget.tag;

import android.content.Context;


/**
 * Created by chenxiaojian on 16/3/29.
 */
public class SearchTagItem {

    private SearchTag searchTag;
    private SearchItemView searchItemView;

    public SearchTagItem(Context context) {
        searchItemView = new SearchItemView(context);
    }

    public SearchTag getSearchTag() {
        return searchTag;
    }

    public void setSearchTag(SearchTag searchTag) {
        this.searchTag = searchTag;
    }

    public SearchItemView getSearchItemView() {
        return searchItemView;
    }

}
