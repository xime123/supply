package bd.com.supply.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bd.com.supply.R;


/**
 * 通用Itemview的封装
 */

public class CommonItemView extends RelativeLayout {
    private TextView mDesTv;
    private TextView mValueTv;

    private ImageView mIcon;
    private ImageView mRightImageView;

    private String des;
    private String vaue;
    private boolean showLeftIcon;
    private boolean showRightIcon;
    private boolean showValRightIcon;

    private int leftSrc;
    private int rightSrc;

    public CommonItemView(Context context) {
        this(context, null);
    }

    public CommonItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.item_common_line_text_layout, this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommonItemView);

        des = a.getString(R.styleable.CommonItemView_textDes);
        vaue = a.getString(R.styleable.CommonItemView_textValue);
        showLeftIcon = a.getBoolean(R.styleable.CommonItemView_isLeftIconShow, false);
        showRightIcon = a.getBoolean(R.styleable.CommonItemView_showmTextRightIcon, true);
        leftSrc = a.getResourceId(R.styleable.CommonItemView_LeftIcon, -1);
        rightSrc = a.getResourceId(R.styleable.CommonItemView_text_right_src, -1);
        showValRightIcon = a.getBoolean(R.styleable.CommonItemView_showValRightIcon, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDesTv = (TextView) findViewById(R.id.tv_des);
        mValueTv = (TextView) findViewById(R.id.tv_value);
        mIcon = findViewById(R.id.iv_icon);
        mRightImageView = findViewById(R.id.iv_right_icon);
        mDesTv.setText(des);
        mValueTv.setText(vaue);

        if (!showRightIcon) {
            mDesTv.setCompoundDrawables(null, null, null, null);
        }

        if (leftSrc != -1) {
            mIcon.setImageDrawable(getResources().getDrawable(leftSrc));
        }

        if (rightSrc != -1) {
            Drawable drawable = getResources().getDrawable(rightSrc);
            drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
            mDesTv.setCompoundDrawables(null, null, drawable, null);
        }

        if (showLeftIcon) {
            mIcon.setVisibility(VISIBLE);
        } else {
            mIcon.setVisibility(GONE);
        }

        if (!showValRightIcon) {
            mValueTv.setCompoundDrawables(null, null, null, null);
        }
    }

    public CommonItemView setRightImageViewVisible(int visible) {
        mRightImageView.setVisibility(visible);
        return this;
    }

    public CommonItemView setRightImageViewRes(@DrawableRes int resId) {
        mRightImageView.setImageResource(resId);
        return this;
    }


    public CommonItemView setTextDes(String des) {
        mDesTv.setText(des);
        return this;
    }

    public CommonItemView setTextDes(@StringRes int strId) {
        mDesTv.setText(strId);
        return this;
    }

    public CommonItemView setTextValue(String value) {
        mValueTv.setText(value);
        return this;
    }

    public CommonItemView setTextValue(@StringRes int strId) {
        mValueTv.setText(strId);
        return this;
    }
}
