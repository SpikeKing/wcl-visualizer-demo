package me.chunyu.spike.wcl_visualizer_demo.permissions;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import me.chunyu.spike.wcl_visualizer_demo.R;

/**
 * 处理权限的类
 * <p>
 * Created by wangchenlong on 16/2/11.
 */
public class PermissionsActivity extends AppCompatActivity {
    public static final int PERMISSIONS_GRANTED = 0;
    public static final int PERMISSIONS_DENIED = 1;

    private static final int PERMISSION_REQUEST_CODE = 0;
    private static final String EXTRA_PERMISSIONS = "me.chunyu.clwang.permissions.extra_permissions";
    private static final String PACKAGE_URL_SCHEME = "package:";

    private PermissionsChecker mChecker; // 检查权限
    private boolean mRequiresCheck; // 是否检查

    // 启动当前的Activity
    public static void startActivityForResult(Activity activity, int requestCode, String... permissions) {
        Intent intent = new Intent(activity, PermissionsActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSIONS)) {
            throw new RuntimeException("当前Activity需要使用静态方法startActivityForResult启动");
        }

        setContentView(R.layout.activity_permissions);

        mChecker = new PermissionsChecker(this);
        mRequiresCheck = true;
    }

    @Override protected void onResume() {
        super.onResume();

        if (mRequiresCheck) {
            String[] permissions = getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);

            if (mChecker.lakesPermissions(permissions)) {
                requestPermissions(permissions);
            } else {
                allPermissionsGranted();
            }
        } else {
            mRequiresCheck = true;
        }
    }

    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    private void allPermissionsGranted() {
        setResult(PERMISSIONS_GRANTED);
        finish();
    }

    // 权限管理的返回
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            mRequiresCheck = true;
            allPermissionsGranted();
        } else {
            mRequiresCheck = false;
            showMissingPermissionDialog();
        }

    }

    // 判断是否拥有所有权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PermissionsActivity.this);
        builder.setTitle(R.string.help).setMessage(R.string.string_help_text);
        builder.setNegativeButton(R.string.quit, ((dialog, which) -> {
            setResult(PERMISSIONS_DENIED);
            finish();
        }));
        builder.setPositiveButton(R.string.settings, (dialog, which) -> {
            startAppSettings();
        });
        builder.show();
    }

    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }
}


