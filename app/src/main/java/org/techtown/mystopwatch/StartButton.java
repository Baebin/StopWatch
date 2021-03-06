package org.techtown.mystopwatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

public class StartButton extends AppCompatButton {
    public StartButton(@NonNull Context context) {
        super(context);

        init(context);
    }

    public StartButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public void init(Context context) {
        setBackground(ContextCompat.getDrawable(context, R.drawable.button_start));
        setTextColor(Color.BLACK);

        float textsize = getResources().getDimension(R.dimen.text_size);
        setTextSize(textsize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("StartButton", "onTouchEvent 호출됨");

        int action = event.getAction();
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                ((MainActivity)MainActivity.mContext).setVisibilities(2);
                break;
        }

        invalidate();

        return true;
    }
}
