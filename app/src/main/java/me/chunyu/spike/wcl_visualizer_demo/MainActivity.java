package me.chunyu.spike.wcl_visualizer_demo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.chunyu.spike.wcl_visualizer_demo.permissions.PermissionsActivity;
import me.chunyu.spike.wcl_visualizer_demo.permissions.PermissionsChecker;
import me.chunyu.spike.wcl_visualizer_demo.visualizers.RendererFactory;
import me.chunyu.spike.wcl_visualizer_demo.visualizers.WaveformView;

public class MainActivity extends AppCompatActivity {

    private static final int CAPTURE_SIZE = 256; // 获取这些数据, 用于显示
    private static final int REQUEST_CODE = 0;

    // 权限
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };

    @Bind(R.id.main_wv_waveform) WaveformView mWvWaveform; // 波纹视图

    private Visualizer mVisualizer; // 音频可视化类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        RendererFactory rendererFactory = new RendererFactory();
        mWvWaveform.setRenderer(rendererFactory.createSimpleWaveformRender(ContextCompat.getColor(this, R.color.colorPrimary), Color.WHITE));
    }

    @Override protected void onResume() {
        super.onResume();

        PermissionsChecker checker = new PermissionsChecker(this);

        if (checker.lakesPermissions(PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
        } else {
            startVisualiser();
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }

    // 设置音频线
    private void startVisualiser() {
        mVisualizer = new Visualizer(0); // 初始化
        mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                if (mWvWaveform != null) {
                    mWvWaveform.setWaveform(waveform);
                }
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

            }
        }, Visualizer.getMaxCaptureRate(), true, false);
        mVisualizer.setCaptureSize(CAPTURE_SIZE);
        mVisualizer.setEnabled(true);
    }

    // 释放
    @Override protected void onPause() {
        if (mVisualizer != null) {
            mVisualizer.setEnabled(false);
            mVisualizer.release();
        }
        super.onPause();
    }
}
