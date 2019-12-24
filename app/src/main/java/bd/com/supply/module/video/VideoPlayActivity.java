package bd.com.supply.module.video;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


import bd.com.appcore.ui.view.LoadingDialog;
import bd.com.supply.R;
import io.reactivex.annotations.NonNull;


public class VideoPlayActivity extends AppCompatActivity {
    private VideoView mVideo;
    private Context mContext;

    public static void startActivity(Context context,@NonNull String url){
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        mContext = this;
        mVideo = (VideoView) findViewById(R.id.video);

        String url = getIntent().getStringExtra("url");
        mVideo.setMediaController(new MediaController(this));

        mVideo.setVideoURI(Uri.parse(url));
        mVideo.start();
        //设置在视频文件在加载完毕以后的回调函数
        LoadingDialog.loadingDialog(VideoPlayActivity.this);
        mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer mp) {
                LoadingDialog.cancleDialog();
//                Toast.makeText(mContext, "准备播放", Toast.LENGTH_SHORT).show();
            }

        });

        //设置发生错误监听，如果不设置videoview会向用户提示发生错误
        mVideo.setOnErrorListener(new MediaPlayer.OnErrorListener(){
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(mContext, "出错了", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        mVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(mContext, "播放完成了", Toast.LENGTH_LONG).show();

            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
