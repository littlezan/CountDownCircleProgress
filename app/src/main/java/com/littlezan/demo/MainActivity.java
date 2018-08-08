package com.littlezan.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.littlezan.demo.countdowncircleprogress.BaseCountDownCircleProgressView;
import com.littlezan.demo.countdowncircleprogress.CountDownCircleProgressView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    static final int TIME_DOWN = 1;

    /**
     * Hello World!
     */
    private CountDownCircleProgressView countDownCircleProgressView;
    private TextView tvTime;
    private Button btnStart;
    private Button btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        tvTime = findViewById(R.id.tvTime);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        countDownCircleProgressView = findViewById(R.id.countDownCircleProgressView);
        tvTime.setText(formatDuration(TimeUnit.MINUTES.toSeconds(TIME_DOWN)));
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCountDown();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownCircleProgressView.stopCountDown();
            }
        });
    }

    private void startCountDown() {
        tvTime.setText(formatDuration(TimeUnit.MINUTES.toSeconds(TIME_DOWN)));
        countDownCircleProgressView.startCountDown(TimeUnit.MINUTES.toMillis(TIME_DOWN), new BaseCountDownCircleProgressView.OnCountDownListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTime.setText(formatDuration(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)));
            }

            @Override
            public void onFinish() {
                tvTime.setText(formatDuration(0));
            }
        });
    }

    public static String formatDuration(long seconds) {
        return String.format(Locale.getDefault(),
                "%02d:%02d:%02d",
                seconds / 3600,
                (seconds % 3600) / 60,
                seconds % 60);
    }
}
