package com.example.addressproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.List;

/**
 * Created by Android Studio.
 * User: lishuai
 * Date: 2021/1/27
 * Time: 下午3:01
 */
class ItemHeaderDecoration<T extends SectionEntity<FirstLetterBean>> extends RecyclerView.ItemDecoration {
    private Context mContext;
    private List<T> mList;
    public static String currentTag = "0";
    private LayoutInflater mLayoutInflater;
    private int mTitleHight = 50;
    private int mTitleTextSize = 20;
//    MultiItemEntity

    public ItemHeaderDecoration(Context context, List<T> dataList) {
        super();
        this.mContext = context;
        this.mList = dataList;
        mTitleHight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, context.getResources().getDisplayMetrics());
        // mTitleTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, context.getResources().getDisplayMetrics());
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    public static void setCurrentTag(String tag) {
        ItemHeaderDecoration.currentTag = tag;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //获取到视图中第一个可见的item的position
        try {
            int position = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
            String tag = mList.get(position).header;
            View child = parent.findViewHolderForLayoutPosition(position).itemView;
            boolean flag = false;
            if (TextUtils.isEmpty(tag)) {
                while (TextUtils.isEmpty(tag) && position >= 0) {
                    tag = mList.get(position--).header;
                }
                setTopTitle(c, parent, tag, flag);
                return;
            }
            if ((position + 1) < mList.size()) {
                String suspensionTag =  mList.get(position + 1).header;
                while (TextUtils.isEmpty(suspensionTag) && position < mList.size()) {
                    suspensionTag =mList.get(position++).header;
                }
                if (null != tag && !tag.equals(suspensionTag)) {
                    if (child.getHeight() + child.getTop() < mTitleHight) {
                        c.save();
                        flag = true;
                        c.translate(0, child.getHeight() + child.getTop() - mTitleHight);
                    }
                }
            }

            setTopTitle(c, parent, tag, flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTopTitle(Canvas c, RecyclerView parent, String tag, boolean flag) {
        try {
            View topTitleView = mLayoutInflater.inflate(R.layout.item_head, parent, false);
            TextView tvTitle = (TextView) topTitleView.findViewById(R.id.letter_tv);
            tvTitle.setText(tag);
//            tvArrow.setText(Icon.ICON_MAP.get("angleup"));
//            tvArrow.setTypeface(((BaseActivity) mContext).iconfont);
            int toDrawWidthSpec;//用于测量的widthMeasureSpec
            int toDrawHeightSpec;//用于测量的heightMeasureSpec
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) topTitleView.getLayoutParams();
            if (lp == null) {
                lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//这里是根据复杂布局layout的width height，new一个Lp
                topTitleView.setLayoutParams(lp);
            }
            topTitleView.setLayoutParams(lp);
            if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                //如果是MATCH_PARENT，则用父控件能分配的最大宽度和EXACTLY构建MeasureSpec。
                toDrawWidthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight(), View.MeasureSpec.EXACTLY);
            } else if (lp.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
                //如果是WRAP_CONTENT，则用父控件能分配的最大宽度和AT_MOST构建MeasureSpec。
                toDrawWidthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight(), View.MeasureSpec.AT_MOST);
            } else {
                //否则则是具体的宽度数值，则用这个宽度和EXACTLY构建MeasureSpec。
                toDrawWidthSpec = View.MeasureSpec.makeMeasureSpec(lp.width, View.MeasureSpec.EXACTLY);
            }
            //高度同理
            if (lp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                toDrawHeightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom(), View.MeasureSpec.EXACTLY);
            } else if (lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                toDrawHeightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom(), View.MeasureSpec.AT_MOST);
            } else {
                toDrawHeightSpec = View.MeasureSpec.makeMeasureSpec(mTitleHight, View.MeasureSpec.EXACTLY);
            }
            //依次调用 measure,layout,draw方法，将复杂头部显示在屏幕上。
            topTitleView.measure(toDrawWidthSpec, toDrawHeightSpec);
            topTitleView.layout(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getPaddingLeft() + topTitleView.getMeasuredWidth(), parent.getPaddingTop() + topTitleView.getMeasuredHeight());
            topTitleView.draw(c);//Canvas默认在视图顶部，无需平移，直接绘制
            if (flag)
                c.restore();//恢复画布到之前保存的状态
            if (!TextUtils.equals(tag, currentTag)) {
                currentTag = tag;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
