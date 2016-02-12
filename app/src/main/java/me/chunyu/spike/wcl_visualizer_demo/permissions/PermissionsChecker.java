package me.chunyu.spike.wcl_visualizer_demo.permissions;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * 权限检测
 * <p>
 * Created by wangchenlong on 16/2/11.
 */
public class PermissionsChecker {
    private final Context mContext;

    public PermissionsChecker(Context context) {
        mContext = context.getApplicationContext();
    }

    // 判断是否缺少权限
    public boolean lakesPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lakesPermission(permission))
                return true;
        }
        return false;
    }

    private boolean lakesPermission(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_DENIED;
    }
}
