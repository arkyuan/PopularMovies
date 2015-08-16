package app.com.ark.android.popularmovies;

import android.app.Application;

/**
 * Created by ark on 8/12/2015.
 */
public class MovieApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        Stetho.initialize(
//                Stetho.newInitializerBuilder(this)
//                        .enableDumpapp(
//                                Stetho.defaultDumperPluginsProvider(this))
//                        .enableWebKitInspector(
//                                Stetho.defaultInspectorModulesProvider(this))
//                        .build());
        // Your normal application code here.  See SampleDebugApplication for Stetho initialization.
    }
}
