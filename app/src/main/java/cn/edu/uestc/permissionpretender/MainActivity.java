public class MainActivity extends Activity {
    /* 
     * 应用程序集合 
     */
    private ArrayList<AppInfo> appInfos;
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
        initView();
        new Thread(runable).start();
    }

    private void initView(){
        lv_app = (ListView) findViewById(R.id.app_list_view);
        lv_app.setOnItemClickListener(new AppDetailLinster());
    }


    private final Runnable runable = new Runnable() {

        public void run() {
            loadApplications();
            myHandler.obtainMessage().sendToTarget();
        }

    };

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            lv_app.setAdapter(new ShowAppListAdapter(MainActivity.this,
                    appInfos, pm));


        }

    };

    /**
     * 加载应用列表 
     */
    private void loadApplications() {
        PackageManager manager = this.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<ResolveInfo> apps = manager.queryIntentActivities(
                mainIntent, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
        if (apps != null) {
            final int count = apps.size();
            if (appInfos == null) {
                appInfos = new ArrayList<AppInfo>(count);
            }
            appInfos.clear();
            for (int i = 0; i < count; i++) {
                AppInfo application = new AppInfo();
                ResolveInfo info = apps.get(i);
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


    /**
     * 列表监听类 
     * @author bill
     *
     */
    public final class AppDetailLinster implements OnItemClickListener {

        AlertDialog dialog;

        public void onItemClick(AdapterView<?> view, View arg1,
                                final int position, long arg3) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MainActivity.this);
            builder.setTitle("选项");
            builder.setItems(R.array.choice, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    final AppInfo appInfo = appInfos.get(position);
                    switch (which) {
                        case 0: // 启动程序
                            try {
                                startApp(appInfo);
                            } catch (Exception e) {

                            }
                            break;
                        case 1: // 详细信息
                            try {
                                showAppDetail(appInfo);
                            } catch (Exception e) {

                            }
                            break;

                    }
                    dialog.dismiss();
                }

                private void showAppDetail(AppInfo appInfo)
                        throws Exception {
                    final String packName = appInfo.packageName.toString();
                    final PackageInfo packInfo = getAppPackinfo(packName);
                    final String versionName = packInfo.versionName;
                    final String[] apppremissions = packInfo.requestedPermissions;
                    final String appName = appInfo.title.toString();
                    Intent showDetailIntent = new Intent(MainActivity.this,
                            ShowAppDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("packagename", packName);
                    bundle.putString("appversion", versionName);
                    bundle.putStringArray("apppremissions", apppremissions);
                    bundle.putString("appname", appName);
                    showDetailIntent.putExtras(bundle);
                    startActivity(showDetailIntent);

                }

                private void startApp(AppInfo appInfo)
                        throws Exception {
                    final String packName = appInfo.packageName.toString();
                    final String activityName = getActivityName(packName);
                    if (null == activityName) {
                        Toast.makeText(MainActivity.this, "程序无法启动",
                                Toast.LENGTH_SHORT);
                        return;
                    }
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(packName,
                            activityName));
                    startActivity(intent);
                }

            });
            dialog = builder.create();
            dialog.show();

        }

    }
    /**
     * 获取程序信息 
     * @param packName
     * @return
     * @throws Exception
     */
    public PackageInfo getAppPackinfo(String packName) throws Exception {
        return pm.getPackageInfo(packName, PackageManager.GET_ACTIVITIES
                | PackageManager.GET_PERMISSIONS);
    }

    /**
     * 获取启动相关程序的Activity 
     * @param packName
     * @return
     * @throws Exception
     */
    public String getActivityName(String packName) throws Exception {
        final PackageInfo packInfo = pm.getPackageInfo(packName,
                PackageManager.GET_ACTIVITIES);
        final ActivityInfo[] activitys = packInfo.activities;
        if (null == activitys || activitys.length <= 0) {
            return null;
        }
        return activitys[0].name;
    }
}  