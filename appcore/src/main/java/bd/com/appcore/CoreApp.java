package bd.com.appcore;

import android.app.Application;
import android.content.Context;


public abstract class CoreApp extends Application {

	public static Context context;
	private static  CoreApp instance;
	@Override
	public void onCreate() {
		super.onCreate();
		instance=this;
		context = getApplicationContext();
	}

	public static  Application getAppInstance(){
		return instance;
	}
}
