package cn.edu.uestc.permissionpretender;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 参考于   http://blog.csdn.net/mp624183768/article/details/76360103
 */

public class MainActivity extends AppCompatActivity {

    /*
     * 应用程序集合
     */
    private ArrayList<ApplicationInfo> appInfos;
    private ListView lv_app;

    /*
     * 管理应用程序包，并通过它获取程序信息
     */
    private PackageManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.app_list);
        pm = getPackageManager();

        /*

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        */
    }

    private void initView(){
        lv_app = (ListView) findViewById(R.id.app_list_view);
        lv_app.setOnItemClickListener(new AppDetailLinster);
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            loadApplications();
            myHandler.obtainMessage().sendToTarget();
        }
    };

    /*
     * 加载程序列表
     */
    private void loadApplications(){
        PackageManager manager = this.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
        if (apps != null){
            final int count = apps.size();
            if (appInfos == null){
                appInfos = new ArrayList<ApplicationInfo>(count);
            }
            appInfos.clear();
            for (int i = 0; i < count; i++){
                ApplicationInfo application = new ApplicationInfo();
                ResolveInfo info = apps.get(i);
                application.
                application.title = info.loadLabel(manager);
                application.setActivity(new ComponentName(
                        info.activityInfo.applicationInfo.packageName,
                        info.activityInfo.name), Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                application.icon = info.activityInfo.loadIcon(manager);
                application.packageName = info.activityInfo.applicationInfo.packageName;
                appInfos.add(application);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
