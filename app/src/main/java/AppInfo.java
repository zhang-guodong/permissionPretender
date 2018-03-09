import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * AppInfo类用来表示应用程序
 * Created by 国栋 on 2018/3/8.
 */

//用来表示应用程序
public class AppInfo {
    public CharSequence title;//程序名
    public CharSequence packageName;//程序包名
    Intent intent;//启动Intent
    public Drawable icon;//程序图标

    /*
     *设置自动该程序的Intent
     */
    final void setActivity(ComponentName className, int launchFlags) {
        intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(className);
        intent.setFlags(launchFlags);
    }
}
