package com.comtop.eimnote.adapter;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected List<T> mData;
    private Context mContext;

    public CommonAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return (mData != null) ? mData.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return (mData != null) ? mData.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent, mData.get(position));
    }

    protected ViewHolder onCreateViewHolder(ViewGroup parent, int position, int viewType) {
        return null;
    }

    public void onBindViewHolder(ViewHolder holder, T t, int position) {

    }

    public View getView(int position, View convertView, ViewGroup parent, T t) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = onCreateViewHolder(parent, position, getItemViewType(position));
            convertView = holder.getConvertView();
            if (convertView != null) {
                convertView.setTag(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        onBindViewHolder(holder, t, position);
        return convertView;
    }

    protected Context getContext() {
        return mContext;
    }

    protected String getString(@StringRes int resId) {
        return mContext.getString(resId);
    }

    protected String getString(@StringRes int resId, Object... formatArgs) {
        return mContext.getString(resId, formatArgs);
    }

    protected class ViewHolder {

        public View convertView;

        public ViewHolder(View v) {
            convertView = v;
        }

        public View getConvertView() {
            return convertView;
        }

    }

}
