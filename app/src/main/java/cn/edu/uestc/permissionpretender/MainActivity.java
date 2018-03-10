package cn.edu.uestc.permissionpretender;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseIntArray;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public static ArrayList<ApplicationInfo> getAppInfo(Context ctx, int type) {
        ArrayList<ApplicationInfo> appList = new ArrayList<ApplicationInfo>();
        SparseIntArray siArray = new SparseIntArray();
        PackageManager pm = ctx.getPackageManager();
        List<ApplicationInfo> installList = pm.getInstalledApplications(PackageManager.PERMISSION_GRANTED);
        for (int i = 0; i < installList.size(); i++){
            ApplicationInfo item = installList.get(i);
            if (siArray.indexOfKey(item.uid) >= 0){ //去掉重复的应用信息
                continue;
            }
            siArray.put(item.uid, 1);
            try{
                String[] permissions = pm.getPackageInfo(item.processName, PackageManager.GET_PERMISSIONS).requestedPermissions;
                if (permissions == null){
                    continue;
                }
                boolean bNet = false;
                for (String permission : permissions){
                    if (permission.equals("android.permission.INTERNET")){
                        bNet = true;
                        break;
                    }
                }
                if (type == 0 || (type == 1 && bNet)){
                    ApplicationInfo app = new ApplicationInfo();
                    app.uid = item.uid;
                    //TODO
                    app.label = item.loadLabel(pm).toString();
                    app.package_name = item.packageName;
                    appList.add(app);
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        return appList;
    }
}
