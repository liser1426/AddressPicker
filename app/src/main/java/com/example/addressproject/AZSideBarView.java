package com.example.addressproject;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;
import java.util.List;


public class AZSideBarView extends View {

	private int mBackgroundColor;
	private int mStrokeColor;
	private int mTextColor;
	private int mTextSize;
	private int mSelectTextColor;
	private int mSelectTextSize;
	private int mHintTextColor;
	private int mHintTextSize;
	private int mHintCircleRadius;
	private int mHintCircleColor;
	private int mHintShape;
	private int mContentPadding;
	private int mBarPadding;
	private int mBarWidth;

	private List<String>           mLetters;
	private RectF                  mSlideBarRect;
	private TextPaint              mTextPaint;
	private Paint                  mPaint;
	private int                    mSelect;
	private int                    mPreSelect;
	private int                    mNewSelect;
	private ValueAnimator          mRatioAnimator;
	private float                  mAnimationRatio;
	private OnLetterChangeListener mListener;
	private int                    mTouchY;

	public AZSideBarView(Context context) {
		this(context, null);
	}

	public AZSideBarView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AZSideBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initAttribute(attrs, defStyleAttr);
		initData();
	}
	
	public void initLetters(List<String> mLetters){
		this.mLetters = mLetters;
		invalidate();		
	}
	

	private void initAttribute(AttributeSet attrs, int defStyleAttr) {
		TypedArray typeArray = getContext().obtainStyledAttributes(attrs, R.styleable.AZSideBarView, defStyleAttr, 0);
		mBackgroundColor = typeArray.getColor(R.styleable.AZSideBarView_backgroundColor, Color.TRANSPARENT);
//		mStrokeColor = typeArray.getColor(R.styleable.AZSideBarView_strokeColor, Color.parseColor("#000000"));
		mTextColor = typeArray.getColor(R.styleable.AZSideBarView_textColor, Color.parseColor("#969696"));
		mTextSize = typeArray.getDimensionPixelOffset(R.styleable.AZSideBarView_textSize,
													  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10,
																					  getResources().getDisplayMetrics()));
		mSelectTextColor = typeArray.getColor(R.styleable.AZSideBarView_selectTextColor, Color.parseColor("#FF0000"));
		mSelectTextSize = typeArray.getDimensionPixelOffset(R.styleable.AZSideBarView_selectTextSize,
															(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10,
																							getResources().getDisplayMetrics()));
		mHintTextColor = typeArray.getColor(R.styleable.AZSideBarView_hintTextColor, Color.parseColor("#FFFFFF"));
		mHintTextSize = typeArray.getDimensionPixelOffset(R.styleable.AZSideBarView_hintTextSize,
														  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
																						  getResources().getDisplayMetrics()));
		mHintCircleRadius = typeArray.getDimensionPixelOffset(R.styleable.AZSideBarView_hintCircleRadius,
															  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
																							  getResources().getDisplayMetrics()));
		mHintCircleColor = typeArray.getColor(R.styleable.AZSideBarView_hintCircleColor, Color.parseColor("#bef9b81b"));
		mHintShape = typeArray.getInteger(R.styleable.AZSideBarView_hintShape, 0);
		mContentPadding = typeArray.getDimensionPixelOffset(R.styleable.AZSideBarView_contentPadding,
															(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
																							getResources().getDisplayMetrics()));
		mBarPadding = typeArray.getDimensionPixelOffset(R.styleable.AZSideBarView_barPadding,
														(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6,
																						getResources().getDisplayMetrics()));
		mBarWidth = typeArray.getDimensionPixelOffset(R.styleable.AZSideBarView_barWidth,
													  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0,
																					  getResources().getDisplayMetrics()));
		if (mBarWidth == 0) {
			mBarWidth = 2 * mTextSize;
		}
		typeArray.recycle();
	}

	private void initData() {
		mLetters = Arrays.asList(getContext().getResources().getStringArray(R.array.slide_bar_value_list));
		mTextPaint = new TextPaint();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mSelect = -1;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mSlideBarRect == null) {
			mSlideBarRect = new RectF();
		}
		float contentLeft = getMeasuredWidth() - mBarWidth - mBarPadding;
		float contentRight = getMeasuredWidth() - mBarPadding;
		float contentTop = mBarPadding;
		float contentBottom;
//		if(mLetters.size()>=18){
//			contentBottom= (float) (getMeasuredHeight() - mBarPadding);
//		}else{
			contentBottom = (float) (mTextSize*1.2* mLetters.size() - mBarPadding);
//		}
		mSlideBarRect.set(contentLeft, contentTop, contentRight, contentBottom);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//绘制slide bar 上字母列表
		drawLetters(canvas);
		//绘制选中时的提示信息(圆＋文字)
		drawHint(canvas);
		//绘制选中的slide bar上的那个文字
		drawSelect(canvas);
	}

	/**
	 * 绘制slide bar 上字母列表
	 */
	private void drawLetters(Canvas canvas) {
		//绘制圆角矩形
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(mBackgroundColor);
		canvas.drawRoundRect(mSlideBarRect, mBarWidth / 2.0f, mBarWidth / 2.0f, mPaint);
		//绘制描边
//		mPaint.setStyle(Paint.Style.STROKE);
//		mPaint.setColor(mStrokeColor);
//		canvas.drawRoundRect(mSlideBarRect, mBarWidth / 2.0f, mBarWidth / 2.0f, mPaint);
		//顺序绘制文字
		float itemHeight = (mSlideBarRect.bottom - mSlideBarRect.top - mContentPadding * 2) / mLetters.size();
		for (int index = 0; index < mLetters.size(); index++) {
			float baseLine = TextDrawUtils.getTextBaseLineByCenter(
				mSlideBarRect.top + mContentPadding + itemHeight * index + itemHeight / 2, mTextPaint, mTextSize);
			mTextPaint.setColor(mTextColor);
			mTextPaint.setTextSize(mTextSize);
			mTextPaint.setTextAlign(Paint.Align.CENTER);
			float pointX = mSlideBarRect.left + (mSlideBarRect.right - mSlideBarRect.left) / 2.0f;
			canvas.drawText(mLetters.get(index), pointX, baseLine, mTextPaint);
		}
	}

	/**
	 * 绘制选中时的提示信息(圆＋文字)
	 */
	private void drawSelect(Canvas canvas) {
		if (mSelect != -1) {
			mTextPaint.setColor(mSelectTextColor);
			mTextPaint.setTextSize(mSelectTextSize);
			mTextPaint.setTextAlign(Paint.Align.CENTER);
			float itemHeight = (mSlideBarRect.bottom - mSlideBarRect.top - mContentPadding * 2) / mLetters.size();
			float baseLine = TextDrawUtils.getTextBaseLineByCenter(
				mSlideBarRect.top + mContentPadding + itemHeight * mSelect + itemHeight / 2, mTextPaint, mTextSize);
			float pointX = mSlideBarRect.left + (mSlideBarRect.right - mSlideBarRect.left) / 2.0f;
			canvas.drawText(mLetters.get(mSelect), pointX, baseLine, mTextPaint);
		}
	}

	/**
	 * 绘制选中的slide bar上的那个文字
	 */
	private void drawHint(Canvas canvas) {
		//x轴的移动路径
		float circleCenterX = (getMeasuredWidth() + mHintCircleRadius) -
							  (-getMeasuredWidth() / 2 + (getMeasuredWidth() + mHintCircleRadius)) * mAnimationRatio;
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(mHintCircleColor);
		if (mHintShape == 0) {
			canvas.drawCircle(circleCenterX, getMeasuredHeight() / 2.0f, mHintCircleRadius, mPaint);
		} else {
			canvas.drawRect(circleCenterX - mHintCircleRadius, getMeasuredHeight() / 2.0f - mHintCircleRadius,
							circleCenterX + mHintCircleRadius, getMeasuredHeight() / 2.0f + mHintCircleRadius, mPaint);
		}
		// 绘制圆中心的提示字符
		if (mSelect != -1) {
			String target = mLetters.get(mSelect);
			float textY = TextDrawUtils.getTextBaseLineByCenter(getMeasuredHeight() / 2.0f, mTextPaint, mHintTextSize);
			mTextPaint.setColor(mHintTextColor);
			mTextPaint.setTextSize(mHintTextSize);
			mTextPaint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(target, circleCenterX, textY, mTextPaint);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final float y = event.getY();
		final float x = event.getX();
		mPreSelect = mSelect;
		mNewSelect = (int) (y / (mSlideBarRect.bottom - mSlideBarRect.top) * mLetters.size());
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (x < mSlideBarRect.left || y < mSlideBarRect.top || y > mSlideBarRect.bottom) {
					return false;
				}
				mTouchY = (int) y;
				startAnimator(1.0f);
				break;
			case MotionEvent.ACTION_MOVE:
				mTouchY = (int) y;
				if (mPreSelect != mNewSelect) {
					if (mNewSelect >= 0 && mNewSelect < mLetters.size()) {
						mSelect = mNewSelect;
						if (mListener != null) {
							mListener.onLetterChange(mLetters.get(mNewSelect));
						}
					}
				}
				invalidate();
				invalidate();
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				startAnimator(0f);
				mSelect = -1;
				break;
			default:
				break;
		}
		return true;
	}

	private void startAnimator(float value) {
		if (mRatioAnimator == null) {
			mRatioAnimator = new ValueAnimator();
		}
		mRatioAnimator.cancel();
		mRatioAnimator.setFloatValues(value);
		mRatioAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator value) {
				mAnimationRatio = (float) value.getAnimatedValue();
				//球弹到位的时候，并且点击的位置变了，即点击的时候显示当前选择位置
				if (mAnimationRatio == 1f && mPreSelect != mNewSelect) {
					if (mNewSelect >= 0 && mNewSelect < mLetters.size()) {
						mSelect = mNewSelect;
						if (mListener != null) {
							mListener.onLetterChange(mLetters.get(mNewSelect));
						}
					}
				}
				invalidate();
			}
		});
		mRatioAnimator.start();
	}

	public void setOnLetterChangeListener(OnLetterChangeListener listener) {
		this.mListener = listener;
	}
	

	public interface OnLetterChangeListener {

		void onLetterChange(String letter);
	}
}
