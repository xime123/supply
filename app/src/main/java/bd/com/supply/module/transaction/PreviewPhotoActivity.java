package bd.com.supply.module.transaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bd.com.supply.R;
import bd.com.supply.widget.photoview.PhotoView;
import bd.com.supply.widget.photoview.PhotoView;


public class PreviewPhotoActivity extends FragmentActivity implements View.OnClickListener {

    private ArrayList<String> paths = new ArrayList<>();
    private SparseArray<Drawable> cachePhoto = new SparseArray<>();

    public static void start(View v, int index, List<String> pathArray) {
        if (pathArray.size() <= 0) return;
        start1(v, index, (ArrayList<String>) pathArray);
    }

    public static void start1(View v, int index, ArrayList<String> paths) {
        String name = "preview" + index;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            v.setTransitionName(name);
        }
        Intent intent = new Intent(v.getContext(), PreviewPhotoActivity.class);
        intent.putExtra("i", index);
        intent.putStringArrayListExtra("paths", paths);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Context ctx = v.getContext();
        if (ctx instanceof Activity) {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) ctx, v, name);
            ctx.startActivity(intent, options.toBundle());
        } else {
            ctx.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(0);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
        final int i = getIntent().getIntExtra("i", 0);
        ArrayList<String> tempPaths = getIntent().getStringArrayListExtra("paths");
        if (tempPaths != null) paths.addAll(tempPaths);
        setContentView(R.layout.activity_preview_photo);
        ViewPager previewPager = findViewById(R.id.preview);
        previewPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 10));
        previewPager.setAdapter(new PagerAdapter() {
            @Override
            public void finishUpdate(@NonNull ViewGroup container) {
                super.finishUpdate(container);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startPostponedEnterTransition();
                }
            }

            @Override
            public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                super.setPrimaryItem(container, position, object);
                int count = container.getChildCount();
                View child;
                for (int j = 0; j < count; j++) {
                    child = container.getChildAt(j);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (child == object) {
                            ((View) object).setTransitionName("preview" + i);
                        } else {
                            child.setTransitionName(null);
                        }
                    }
                }
            }

            @Override
            public int getCount() {
                return paths.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                PhotoView photoView = new PhotoView(container.getContext());
                photoView.enable();
                photoView.setOnClickListener(PreviewPhotoActivity.this);
                Drawable d = cachePhoto.get(position);
                if (d == null) {
                    Glide.with(PreviewPhotoActivity.this).load(paths.get(position)).into(photoView);
                } else {
                    photoView.setImageDrawable(d);
                }
                container.addView(photoView);
                return photoView;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                PhotoView photoView = (PhotoView) object;
                photoView.setImageDrawable(null);
                cachePhoto.put(position, photoView.getDrawable());
                container.removeView(photoView);
            }
        });
        previewPager.setCurrentItem(i, false);
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}
