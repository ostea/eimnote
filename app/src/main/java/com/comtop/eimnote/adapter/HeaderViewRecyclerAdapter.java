package com.comtop.eimnote.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class HeaderViewRecyclerAdapter<T> extends BaseRecyclerAdapter<T> {

    public BaseRecyclerAdapter mAdapter;
    public SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    public SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();
    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;

    public HeaderViewRecyclerAdapter(BaseRecyclerAdapter adapter) {
        mAdapter = adapter;
    }

    public HeaderViewRecyclerAdapter(List<View> headerViews, List<View> footerViews,
                                     BaseRecyclerAdapter adapter) {
        this(adapter);
        reset();
        addAllHeaderViews(headerViews);
        addAllFooterViews(footerViews);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // header
        if (mHeaderViews.get(viewType) != null) {
            return new ViewHolder(mHeaderViews.get(viewType), false);
        }
        // footer
        if (mFooterViews.get(viewType) != null) {
            return new ViewHolder(mFooterViews.get(viewType), false);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // header
        if (isHeaderView(position)) {
            return;
        }
        // footer
        if (isFooterView(position)) {
            return;
        }
        mAdapter.onBindViewHolder(holder, position - getHeaderViewCount());
    }

    @Override
    public int getItemCount() {
        return getHeaderViewCount() + getFooterViewCount() + getDataCount();
    }

    @Override
    public int getItemViewType(int position) {
        // header
        if (isHeaderView(position)) {
            return mHeaderViews.keyAt(position);
        }
        // footer
        if (isFooterView(position)) {
            return mFooterViews.keyAt(position - getHeaderViewCount() - getDataCount());
        }
        return mAdapter.getItemViewType(position - getHeaderViewCount());
    }

    public int getHeaderViewCount() {
        return (mHeaderViews == null) ? 0 : mHeaderViews.size();
    }

    public int getFooterViewCount() {
        return (mFooterViews == null) ? 0 : mFooterViews.size();
    }

    public int getDataCount() {
        return (mAdapter == null) ? 0 : mAdapter.getItemCount();
    }

    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFooterView(View view) {
        mFooterViews.put(mFooterViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public boolean isHeaderView(int pos) {
        return pos >= 0 && pos < getHeaderViewCount();
    }

    public boolean isFooterView(int pos) {
        return pos >= getHeaderViewCount() + getDataCount();
    }

    public void reset() {
        mHeaderViews.clear();
        mFooterViews.clear();
    }

    private void addAllFooterViews(List<View> footerViews) {
        if (footerViews != null && !footerViews.isEmpty()) {
            for (View v : footerViews) {
                addFooterView(v);
            }
        }
    }

    private void addAllHeaderViews(List<View> headerViews) {
        if (headerViews != null && !headerViews.isEmpty()) {
            for (View v : headerViews) {
                addHeaderView(v);
            }
        }
    }

}
