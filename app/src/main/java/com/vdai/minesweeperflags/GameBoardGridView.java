package com.vdai.minesweeperflags;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Vivian on 2016-05-22.
 */
public class GameBoardGridView extends GridView{

    public GameBoardGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameBoardGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public GameBoardGridView(Context context) {
        super(context);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
