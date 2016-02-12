package me.chunyu.spike.wcl_visualizer_demo.visualizers;

import android.graphics.Canvas;

/**
 * 音频波纹渲染接口
 * <p>
 * Created by wangchenlong on 16/2/11.
 */
public interface WaveformRenderer {
    void render(Canvas canvas, byte[] waveform);
}
