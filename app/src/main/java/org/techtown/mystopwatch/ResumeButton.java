package org.techtown.mystopwatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

public class ResumeButton extends AppCompatButton {
    public ResumeButton(@NonNull Context context) {
        super(context);

        init(context);
    }

    public ResumeButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public void init(Context context) {
        setBackground(ContextCompat.getDrawable(context, R.drawable.button_resume));
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
        Log.d("ResumeButton", "onTouchEvent 호출됨");

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
