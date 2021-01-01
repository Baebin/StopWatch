package org.techtown.mystopwatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    public static Context mContext;
    private boolean Stop = false;

    LinearLayout container;
    LinearLayout scroll;
    ScrollView scrollView;

    TextView textView_digital_H;
    TextView textView_digital_M;
    TextView textView_digital_S;
    TextView textView_digital_MS;

    StartButton button_start;
    StopButton button_stop;
    ResetButton button_reset;
    ResumeButton button_resume;

    Handler handler = new Handler();
    MediaPlayer mp = new MediaPlayer();

    int lastH = -1, lastM = -1, lastS = -1, lastMS = -1, num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        container = findViewById(R.id.container);
        scroll = findViewById(R.id.Scroll);

        scrollView = findViewById(R.id.scrollView);

        textView_digital_H = findViewById(R.id.textView_digital_H);
        textView_digital_M = findViewById(R.id.textView_digital_M);
        textView_digital_S = findViewById(R.id.textView_digital_S);
        textView_digital_MS = findViewById(R.id.textView_digital_MS);

        button_start = findViewById(R.id.button_start);
        button_stop = findViewById(R.id.button_stop);
        button_reset = findViewById(R.id.button_reset);
        button_resume = findViewById(R.id.button_resume);
    }

    public void setVisibilities(int position) {
        if (position == 1) { // ResetButton
            if (button_reset.getText().toString() == "초기화") {
                container.setVisibility(View.INVISIBLE);
                button_start.setVisibility(View.VISIBLE);
                button_stop.setVisibility(View.INVISIBLE);
                button_resume.setVisibility(View.INVISIBLE);
                button_reset.setVisibility(View.INVISIBLE);
                button_reset.setText("초기화");

                ClearStopWatch();

                Stop = true;

            } else { // StartButton

                float textSize = getResources().getDimension(R.dimen.log_size);

                num ++;
                TextView Number = new TextView(this);
                Number.setText(Integer.toString(num));
                Number.setTextColor(Color.BLACK);
                Number.setTextSize(textSize);

                TextView Save = new TextView(this);
                Save.setTextColor(Color.BLACK);
                Save.setTextSize(textSize);
                if (lastH == -1) {
                    Save.setText(textView_digital_M.getText().toString() + ":" +
                            textView_digital_S.getText().toString() + "." +
                            textView_digital_MS.getText().toString());
                } else {
                    int lastTotalMS = (lastH*3600000) + (lastM * 60000) + (lastS * 1000) + (lastMS * 10);
                    int TotalMS = (Integer.parseInt(textView_digital_H.getText().toString()) * 3600000) +
                            (Integer.parseInt(textView_digital_M.getText().toString()) * 60000) +
                            (Integer.parseInt(textView_digital_S.getText().toString()) * 1000) +
                            (Integer.parseInt(textView_digital_MS.getText().toString()) * 10);
                    TotalMS -= lastTotalMS;
                    int TotalH = 0, TotalM = 0, TotalS = 0;

                    while (TotalMS >= 1000) {
                        TotalMS -= 1000;
                        TotalS ++;
                    }
                    while (TotalS >= 60) {
                        TotalS -= 60;
                        TotalM ++;
                    }
                    while (TotalM >= 60) {
                        TotalM -= 60;
                        TotalH ++;
                    }

                    Save.setText(GetTime(TotalM, false) + ":" +
                            GetTime(TotalS, false) + "." +
                            GetTime(TotalMS, true));
                }

                TextView Time = new TextView(this);
                Time.setText(textView_digital_M.getText().toString() + ":" +
                        textView_digital_S.getText().toString() + "." +
                        textView_digital_MS.getText().toString());
                Time.setTextColor(Color.BLACK);
                Time.setTextSize(textSize);

                lastH = Integer.parseInt(textView_digital_H.getText().toString());
                lastM = Integer.parseInt(textView_digital_M.getText().toString());
                lastS = Integer.parseInt(textView_digital_S.getText().toString());
                lastMS = Integer.parseInt(textView_digital_MS.getText().toString());

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.CENTER;
                layoutParams.setMargins(0, 0, 0, 5);
                layoutParams.weight = 1;

                LinearLayout log = new LinearLayout(this);
                log.setLayoutParams(layoutParams);
                log.setOrientation(LinearLayout.HORIZONTAL);

                log.addView(Number);
                log.addView(Save);
                log.addView(Time);

                Number.setLayoutParams(layoutParams);
                Save.setLayoutParams(layoutParams);
                Time.setLayoutParams(layoutParams);

                Number.setGravity(Gravity.CENTER);
                Save.setGravity(Gravity.CENTER);
                Time.setGravity(Gravity.CENTER);

                if (num%2 == 1) { // BackGroundColor
                    Resources resources = getResources();
                    int LogBackGround = resources.getColor(R.color.LogBackGround);
                    log.setBackgroundColor(LogBackGround);
                }

                log.setPadding(30, 30, 0, 0);

                scroll.addView(log, 0);
            }
        } else if (position == 2){ // StartButton, ResumeButton
            container.setVisibility(View.VISIBLE);
            button_stop.setVisibility(View.VISIBLE);
            button_reset.setVisibility(View.VISIBLE);
            button_reset.setText("구간 기록");
            button_resume.setVisibility(View.INVISIBLE);

            if (button_start.getVisibility() == View.VISIBLE) { // clear
                ClearStopWatch();
            }
            button_start.setVisibility(View.INVISIBLE);

            Stop = false;
            StopWatch thread = new StopWatch();
            thread.start();
        } else if (position == 3) { // StopButton
            button_stop.setVisibility(View.INVISIBLE);
            button_resume.setVisibility(View.VISIBLE);
            button_reset.setText("초기화");

            Stop = true;
        }
    }

    public void ClearStopWatch() {
        scroll.removeAllViews();
        num = 0;
        lastH = -1;
        textView_digital_H.setText("00");
        textView_digital_M.setText("00");
        textView_digital_S.setText("00");
        textView_digital_MS.setText("00");
    }

    public String GetTime(int time, boolean type) {
        if (type) { // true: MS
            time /= 10;
        }

        String text = Integer.toString(time);

        if (time < 10) {
            text = "0" + text;
        } else if (time >= 100) {
            return "00";
        }

        return text;
    }

    class StopWatch extends Thread {
        int h, m, s, ms = 0;

        public void run() {
            ms = (Integer.parseInt(textView_digital_MS.getText().toString()) * 10);
            while(!Stop) {
                try {
                    Thread.sleep(10);

                    ms += 10;
                    h = Integer.parseInt(textView_digital_H.getText().toString());
                    m = Integer.parseInt(textView_digital_M.getText().toString());
                    s = Integer.parseInt(textView_digital_S.getText().toString());

                    Log.d("StopWatch", "value : " + ms);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            if (ms >=1000) {
                                ms -= 1000;
                                s++;
                            }

                            textView_digital_MS.setText(GetTime(ms, true));

                            if (s >=60) {
                                s -= 60;
                                m++;
                            }

                            if (m >=60) {
                                m -= 60;
                                h++;
                            }

                            textView_digital_S.setText(GetTime(s, false));
                            textView_digital_M.setText(GetTime(m, false));
                            textView_digital_H.setText(GetTime(h, false));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}