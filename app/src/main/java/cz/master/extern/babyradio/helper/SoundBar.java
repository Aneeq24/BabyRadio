package cz.master.extern.babyradio.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatSeekBar;

/**
 * Created by Yasir Iqbal on 7/29/2016.
 */
public class SoundBar extends AppCompatSeekBar   {
    public SoundBar(Context context) {
        super(context);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}//end of SoundBar
