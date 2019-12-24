package bd.com.appcore.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bd.com.appcore.R;


/**
 * Created by 徐敏 on 2017/8/17.
 */

public class CommonLineTextView extends RelativeLayout {
    private TextView mContentTv;
    private TextView mTitleTv;

    private ImageView rightIv;
    private View lineView, topLine, bottomLine;
    private String title;
    private String content;
    private boolean showLeftIcon;
    private boolean showRightIcon;
    private boolean showTextLine;
    private boolean showTopLine;
    private boolean showBottomLine;
    private int leftSrc;
    private int rightSrc;

    public CommonLineTextView(Context context) {
        this(context, null);
    }

    public CommonLineTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonLineTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.appcore_item_common_line_text_layout, this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommonLineTextView);

        title = a.getString(R.styleable.CommonLineTextView_title);
        content = a.getString(R.styleable.CommonLineTextView_data_text);
        showLeftIcon = a.getBoolean(R.styleable.CommonLineTextView_showLeft_icon, true);
        showTextLine = a.getBoolean(R.styleable.CommonLineTextView_showTextLine, true);
        showRightIcon = a.getBoolean(R.styleable.CommonLineTextView_showTextRightIcon, true);
        showTopLine = a.getBoolean(R.styleable.CommonLineTextView_showLeft_top_line, false);
        showBottomLine = a.getBoolean(R.styleable.CommonLineTextView_showLeft_bottom_line, false);
        leftSrc = a.getResourceId(R.styleable.CommonLineTextView_text_left_src, -1);
        rightSrc = a.getResourceId(R.styleable.CommonLineTextView_text_right_src, -1);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitleTv = (TextView) findViewById(R.id.tv_m_name);
        mContentTv = (TextView) findViewById(R.id.tv_m_data);

        lineView = findViewById(R.id.line_view);
        topLine = findViewById(R.id.line_top);
        bottomLine = findViewById(R.id.line_bottom);
        mContentTv.setText(content);
        mTitleTv.setText(title);

        if (!showRightIcon) {
            mContentTv.setCompoundDrawables(null, null, null, null);
        }

        if (!showTextLine) {
            lineView.setVisibility(GONE);
        }

        if (showBottomLine) {
            lineView.setVisibility(GONE);
            bottomLine.setVisibility(VISIBLE);
        }

        if (showTopLine) {
            topLine.setVisibility(VISIBLE);
        }
    }


    public CommonLineTextView setTitle(String title) {
        mTitleTv.setText(title);
        return this;
    }

    public CommonLineTextView setTitle(@StringRes int title) {
        mTitleTv.setText(title);
        return this;
    }

    public CommonLineTextView setContent(String content) {
        mContentTv.setText(content);
        return this;
    }

    public CommonLineTextView setContent(@StringRes int content) {
        mContentTv.setText(content);
        return this;
    }

    public String getContent() {
        return mContentTv.getText().toString();
    }
}
