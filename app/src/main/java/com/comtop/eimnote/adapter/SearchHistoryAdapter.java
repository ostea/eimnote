package com.comtop.eimnote.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comtop.eimnote.R;


public class SearchHistoryAdapter extends BaseRecyclerAdapter<SearchHistoryAdapter.SearchItem> {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_CLEAR = 1;


    public SearchHistoryAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        switch (viewType) {
            case TYPE_NORMAL:
                View view = View.inflate(getContext(), R.layout.item_list_search_history, null);
                holder = new SearchHolder(view);
                break;
            case TYPE_CLEAR:
                View clear = View.inflate(getContext(), R.layout.item_list_clear_search, null);
                holder = new ClearHolder(clear);
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof SearchHolder) {
            SearchItem item = mData.get(position);
            ((SearchHolder) holder).textView.setText(item.getSearchText());
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = mData.get(position).getType();
        if (type == TYPE_CLEAR)
            return TYPE_CLEAR;
        else return TYPE_NORMAL;
    }

    private class SearchHolder extends ViewHolder {
        TextView textView;

        public SearchHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_search);
        }
    }

    private class ClearHolder extends ViewHolder {

        public ClearHolder(View itemView) {
            super(itemView);
        }
    }

    public static class SearchItem {
        private String searchText;
        private int type;

        public SearchItem(String item) {
            this.searchText = item;
        }


        public SearchItem(String searchText, int type) {
            this.searchText = searchText;
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getSearchText() {
            return searchText;
        }

        public void setSearchText(String searchText) {
            this.searchText = searchText;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof SearchItem && obj != null) {
                return searchText.equals(((SearchItem) obj).searchText);
            }
            return super.equals(obj);
        }
    }
}
