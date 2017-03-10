package rocks.codethat.ghostpassword;

import android.app.Application;
import android.content.Context;

/**
 * Created by Sparrow on 3/2/2017.
 */

public class GhostPassword extends Application {
    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}
