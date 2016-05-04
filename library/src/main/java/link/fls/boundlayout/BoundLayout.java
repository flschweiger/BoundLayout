/*
 * Copyright (C) 2016 Frederik Schweiger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package link.fls.boundlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class BoundLayout extends FrameLayout {

    private static final int NO_WIDTH = -1;

    private boolean mKeepAspectRatio;
    private int mMaxWidth;
    private int mMaxHeight;
    private int mMinWidth;
    private int mMinHeight;

    public BoundLayout(Context context) {
        this(context, null);
    }

    public BoundLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BoundLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttributes(attrs);
    }

    private void readAttributes(AttributeSet attributeSet) {
        TypedArray attrs = getContext()
                .obtainStyledAttributes(attributeSet, R.styleable.BoundLayout);

        try {
            mKeepAspectRatio = attrs.getBoolean(R.styleable.BoundLayout_keepAspectRatio, false);
            mMaxWidth = attrs.getDimensionPixelSize(R.styleable.BoundLayout_maxWidth, NO_WIDTH);
            mMaxHeight = attrs.getDimensionPixelSize(R.styleable.BoundLayout_maxHeight, NO_WIDTH);
            mMinWidth = attrs.getDimensionPixelSize(R.styleable.BoundLayout_minWidth, NO_WIDTH);
            mMinHeight = attrs.getDimensionPixelSize(R.styleable.BoundLayout_minHeight, NO_WIDTH);
        } finally {
            attrs.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = 0;
        int measuredHeight = 0;

        boolean wrapContentWidth = getLayoutParams().width == LayoutParams.WRAP_CONTENT;
        boolean wrapContentHeight = getLayoutParams().height == LayoutParams.WRAP_CONTENT;

        if (!wrapContentWidth) {
            measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        }

        if (!wrapContentHeight) {
            measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        }

        if (wrapContentWidth || wrapContentHeight) {

            for (int x = 0; x < getChildCount(); x++) {
                final View child = getChildAt(x);

                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();

                if (wrapContentWidth) {
                    int margins = params.leftMargin + params.rightMargin;
                    measuredWidth = Math.max(measuredWidth, child.getMeasuredWidth() + margins);
                }

                if (wrapContentHeight) {
                    int margins = params.topMargin + params.bottomMargin;
                    measuredHeight = Math.max(measuredHeight, child.getMeasuredHeight() + margins);
                }
            }

        }

        int newWidth = measuredWidth;
        int newHeight = measuredHeight;

        if (mMaxWidth != NO_WIDTH && mMaxWidth < measuredWidth) {
            newWidth = mMaxWidth;
        } else if (mMinWidth != NO_WIDTH && mMinWidth > measuredWidth) {
            newWidth = mMinWidth;
        }

        if (mMaxHeight != NO_WIDTH && mMaxHeight < measuredHeight) {
            newHeight = mMaxHeight;
        } else if (mMinHeight != NO_WIDTH && mMinHeight > measuredHeight) {
            newHeight = mMinHeight;
        }

        if (mKeepAspectRatio) {
            float dScaleWidth = 1f;
            float dScaleHeight = 1f;

            if (mMinWidth != NO_WIDTH && mMinHeight != NO_WIDTH && mMinHeight > 0) {

                if (measuredWidth > mMaxWidth) {
                    dScaleWidth = getScaleFactor(mMinWidth, mMaxWidth);
                } else {
                    dScaleWidth = getScaleFactor(mMinWidth, measuredWidth);
                }

                if (measuredHeight > mMaxHeight) {
                    dScaleHeight = getScaleFactor(mMinHeight, mMaxHeight);
                } else {
                    dScaleHeight = getScaleFactor(mMinHeight, measuredHeight);
                }

            } else if (mMaxWidth != NO_WIDTH && mMaxHeight != NO_WIDTH && mMaxHeight > 0) {
                dScaleWidth = getScaleFactor(mMaxWidth, measuredWidth);
                dScaleHeight = getScaleFactor(mMaxHeight, measuredHeight);
            }

            float scaleFactor = Math.min(dScaleHeight, dScaleWidth);
            newWidth = (int) (mMinWidth * scaleFactor);
            newHeight = (int) (mMinHeight * scaleFactor);
        }

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                newWidth,
                MeasureSpec.getMode(widthMeasureSpec));

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                newHeight,
                MeasureSpec.getMode(heightMeasureSpec));

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private float getScaleFactor(int currentSize, int maxSize) {
        float scaleFactor;

        if (currentSize > maxSize) {
            scaleFactor = (float) maxSize / (float) currentSize;
        } else {
            scaleFactor = (float) maxSize / (float) currentSize;
        }

        return scaleFactor;
    }
}
