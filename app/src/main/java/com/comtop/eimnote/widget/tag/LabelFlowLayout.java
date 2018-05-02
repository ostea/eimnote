package com.comtop.eimnote.widget.tag;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.comtop.eimnote.NoteApp;
import com.comtop.eimnote.util.DensityUtil;

/**
 * Created by chenxiaojian on 16/3/28.
 */
public class LabelFlowLayout extends ViewGroup {
    private int horizontalSpacing = DensityUtil.dip2px(NoteApp.getContext(),8);
    private int verticalSpacing = DensityUtil.dip2px(NoteApp.getContext(),8);

    public LabelFlowLayout(Context context) {
        super(context);
    }

    public LabelFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LabelFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) -
                getPaddingLeft() - getPaddingRight();
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec)
                - getPaddingTop() - getPaddingBottom();
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int hasWidth = 0;//已占用长度，换行清空
        int hasHeight = 0;//已占用位置
        int lineNum = 0;//共几行

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() == View.GONE)
                continue;
            childView.measure(MeasureSpec.makeMeasureSpec(sizeWidth,
                            modeWidth == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : modeWidth),
                    MeasureSpec.makeMeasureSpec(sizeHeight,
                            modeHeight == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : modeHeight));
            LayoutParams layoutParams = (LayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            if (hasWidth != 0) {
                hasWidth = hasWidth + childWidth + horizontalSpacing;
            } else {
                hasWidth += childWidth;
            }

            boolean isNewLine = hasWidth > sizeWidth;
            if (isNewLine) {
                lineNum++;
                hasHeight = hasHeight + childHeight + verticalSpacing;
                hasWidth = childWidth;
            }
            hasHeight = Math.max(hasHeight, childHeight);
            int posX;
            int posY;
            posX = getPaddingLeft() + hasWidth - childWidth;
            posY = getPaddingTop() + hasHeight - childHeight;
            layoutParams.setPosition(posX, posY);
        }
        setMeasuredDimension(resolveSize(sizeWidth, widthMeasureSpec),
                resolveSize(hasHeight+getPaddingBottom()+getPaddingTop(), heightMeasureSpec));

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            child.layout(layoutParams.x, layoutParams.y,
                    layoutParams.x + child.getMeasuredWidth()
                    , layoutParams.y + child.getMeasuredHeight());
        }
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        private int x;
        private int y;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }


        public void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }
}
