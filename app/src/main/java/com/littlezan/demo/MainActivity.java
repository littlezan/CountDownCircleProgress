package com.littlezan.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.littlezan.demo.countdowncircleprogress.BaseCountDownCircleProgressView;
import com.littlezan.demo.countdowncircleprogress.CountDownCircleProgressView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    /**
     * Hello World!
     */
    private CountDownCircleProgressView countDownCircleProgressView;
    private TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        tvTime = findViewById(R.id.tvTime);
        countDownCircleProgressView = findViewById(R.id.countDownCircleProgressView);
        countDownCircleProgressView.startCountDown(TimeUnit.MINUTES.toMillis(3), new BaseCountDownCircleProgressView.OnCountDownListener() {
            @Override
            public void onOneSecondTick(long millisUntilFinished) {
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
