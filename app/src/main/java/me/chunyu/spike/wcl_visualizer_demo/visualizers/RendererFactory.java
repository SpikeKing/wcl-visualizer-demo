package me.chunyu.spike.wcl_visualizer_demo.visualizers;

import android.support.annotation.ColorInt;

/**
 * 工厂模式
 * <p>
 * Created by wangchenlong on 16/2/12.
 */
public class RendererFactory {
    public WaveformRenderer createSimpleWaveformRender(@ColorInt int foreground, @ColorInt int background) {
        return SimpleWaveformRenderer.newInstance(background, foreground);
    }
}
