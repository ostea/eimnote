package com.comtop.eimnote.adapter;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {

    protected List<T> mData;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    protected class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView, boolean clickAble) {
            super(itemView);
            if (!clickAble) {
                return;
            }
            initClickableView(itemView);
        }

        public ViewHolder(View itemView, @IdRes int id) {
            super(itemView);
            initClickableView(itemView.findViewById(id));
        }

        private void initClickableView(View clickAbleView) {
            clickAbleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }

        public ViewHolder(View itemView) {
            this(itemView, true);
        }
    }

    public BaseRecyclerAdapter() {
    }

    public BaseRecyclerAdapter(Context context, List<T> data) {
        this.mData = data;
        this.mContext = context;
    }

    public BaseRecyclerAdapter(Context context) {
        this.mData = new ArrayList<>();
        this.mContext = context;
    }

    /**
     * 如果对象更变才使用该方法来刷新数据，慎用。建议还是维护原对象
     *
     * @param data list
     */
    public void updateData(List<T> data) {
        if (mData == null) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }
        if (data != null) {
            mData.addAll(data);
        }
    }


    public void addAll(List<T> items) {
        if (items != null) {
            this.mData.addAll(items);
            notifyItemRangeInserted(this.mData.size(), items.size());
        }
    }

    public final void addItem(T item) {
        if (item != null) {
            this.mData.add(item);
            notifyItemChanged(mData.size());
        }
    }
    public void addItem(int position, T item) {
        if (item != null) {
            this.mData.add(position, item);
            notifyItemInserted(position);
        }
    }

    public List<T> getAllData() {
        return mData;
    }

    public final void removeData(T item) {
        if (this.mData.contains(item)) {
            int position = mData.indexOf(item);
            this.mData.remove(item);
            notifyItemRemoved(position);
        }
    }

    public final void removeData(int position) {
        if (this.getItemCount() > position) {
            this.mData.remove(mData.get(position));
            notifyItemRemoved(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return getDataCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    protected Context getContext() {
        return mContext;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public int getDataCount() {
        return (mData == null) ? 0 : mData.size();
    }

    public T getData(int position) {
        return mData.get(position);
    }

    protected String getString(@StringRes int resId) {
        return mContext.getString(resId);
    }

    protected int getColor(@ColorRes int id) {
        return mContext.getResources().getColor(id);
    }

    protected String getQuantityString(@PluralsRes int id, int quantity, Object... formatArgs) {
        return mContext.getResources().getQuantityString(id, quantity, formatArgs);
    }

}
