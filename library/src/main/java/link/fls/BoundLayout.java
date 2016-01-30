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

package link.fls;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class BoundLayout extends FrameLayout {

    private static final int NO_WIDTH = -1;

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

        if (mMaxWidth != NO_WIDTH && mMaxWidth < measuredWidth) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    mMaxWidth,
                    MeasureSpec.getMode(widthMeasureSpec));
        } else if (mMinWidth != NO_WIDTH && mMinWidth > measuredWidth) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    mMinWidth,
                    MeasureSpec.getMode(widthMeasureSpec));
        }

        if (mMaxHeight != NO_WIDTH && mMaxHeight < measuredHeight) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    mMaxHeight,
                    MeasureSpec.getMode(heightMeasureSpec));
        } else if (mMinHeight != NO_WIDTH && mMinHeight > measuredHeight) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    mMinHeight,
                    MeasureSpec.getMode(heightMeasureSpec));
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
