package bd.com.appcore.ui.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import bd.com.appcore.ActivityManager;

import bd.com.appcore.ui.view.StatusBarUtil;
import bd.com.appcore.util.SystemBarTintManager;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public class BaseCoreActivity extends RxAppCompatActivity {

	private static final String TAG = "BaseCoreActivity";
	protected boolean mResumed;
	protected boolean mCreated;
	protected boolean mStopped;
	protected boolean mIsDestroyed;
	private CompositeSubscription mCompositeSubscription;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, getClass().getName() + ":: onCreate");
		ActivityManager.getInstance().setActivityAttribute(this);
		ActivityManager.getInstance().addManagedActivity(this);
		mCreated = true;
		mIsDestroyed = false;
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, getClass().getName() + ":: onStart");
		mStopped = false;
	}

	/**
	 * 沉浸式效果
	 */
	protected void immerse() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setStatusBarColor(0);
		}
		View decorView = getWindow().getDecorView();
		int flags = decorView.getSystemUiVisibility();
		decorView.setSystemUiVisibility(flags | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, getClass().getName() + ":: onResume");
		mResumed = true;
		ActivityManager.getInstance().setIsForeGround(true);
		ActivityManager.getInstance().setCurrActivity(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, getClass().getName() + ":: onPause");
		mResumed = false;
		ActivityManager.getInstance().setIsForeGround(false);
//		// 及时置空，避免内存溢出
		ActivityManager.getInstance().setCurrActivity(null);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, getClass().getName() + ":: onStop");
		mStopped = true;
	}

	protected void onDestroy() {
		Log.d(TAG, getClass().getName() + ":: onDestroy");
		ActivityManager.getInstance().removeManagedActivity(this);
		removeSubscription();
		super.onDestroy();
		mIsDestroyed = true;
	}

	public void addSubscription(Subscription s) {
		if (this.mCompositeSubscription == null) {
			this.mCompositeSubscription = new CompositeSubscription();
		}
		this.mCompositeSubscription.add(s);
	}

	public void removeSubscription() {
		if (this.mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
			this.mCompositeSubscription.unsubscribe();
		}
	}

	private SystemBarTintManager mTintManager;

	// 设置沉浸式状态栏
	public void setStatusBarTranslucent(int color, int alpha) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		mTintManager = new SystemBarTintManager(this);
		mTintManager.setStatusBarTintEnabled(true);
		mTintManager.setStatusBarAlpha(alpha);
		mTintManager.setStatusBarTintColor(color);
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		if(Build.VERSION.SDK_INT<Build.VERSION_CODES.KITKAT){
			return;
		}
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	protected void immersee(){
		if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
			return;
		}
		getWindow().setStatusBarColor(0);
		View decorView=getWindow().getDecorView();
		int flags=decorView.getSystemUiVisibility();
		decorView.setSystemUiVisibility(flags|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	protected void immerse(boolean dark){
		if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
			return;
		}
		getWindow().setStatusBarColor(0);
		View decorView=getWindow().getDecorView();
		int flags=decorView.getSystemUiVisibility();
		decorView.setSystemUiVisibility(flags|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		StatusBarUtil.setAndroidNativeLightStatusBar(this,dark);
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	protected void immerse(int color){
		if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
			return;
		}
		getWindow().setStatusBarColor(color);
		View decorView=getWindow().getDecorView();
		int flags=decorView.getSystemUiVisibility();
		decorView.setSystemUiVisibility(flags|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	protected void immerse(int color,boolean dark){
		if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
			return;
		}
		getWindow().setStatusBarColor(color);
		View decorView=getWindow().getDecorView();
		int flags=decorView.getSystemUiVisibility();
		decorView.setSystemUiVisibility(flags|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		StatusBarUtil.setAndroidNativeLightStatusBar(this,dark);
	}
}
