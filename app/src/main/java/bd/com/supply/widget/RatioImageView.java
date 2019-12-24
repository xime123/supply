package bd.com.supply.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import bd.com.supply.R;


/**
 *  可以设置 宽高比例的view
 * Created by peter on 2017/12/6.
 */
public class RatioImageView extends ImageView {
    private float h2wratio = 0;
    private float w2hratio = 0;

    public RatioImageView(Context ctx){
        super(ctx);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
    }

    private void init(AttributeSet attrs){
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.RatioImageView);
        h2wratio = a.getFloat(
                R.styleable.RatioImageView_height_according_to_width_ratio, h2wratio);
        w2hratio = a.getFloat(
                R.styleable.RatioImageView_width_according_to_height_ratio, w2hratio);


        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (w2hratio > 0) {//固定高度
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            if (height > 0) {
                width = (int) ((float) height * w2hratio);
            }
            setMeasuredDimension(width, height);

        }else if (h2wratio > 0) {//固定宽度
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);

            if (width > 0) {
                height = (int) ((float) width * h2wratio);
            }
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
