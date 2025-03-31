package com.example.group8_bartertrader.notification;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group8_bartertrader.R;

public class ViewPushNotificationActivity extends AppCompatActivity {

    private TextView titleTV;
    private TextView bodyTV;
    private TextView jobIdTV;
    private TextView jobLocationTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_push_notification);
        init();
        setData();
    }

    private void init() {
        //binding the views with the variables
        titleTV = findViewById(R.id.titleTV);
        bodyTV = findViewById(R.id.bodyTV);
        jobIdTV = findViewById(R.id.jobIdTV);
        jobLocationTV = findViewById(R.id.jobLocationTV);
    }

    private void setData() {
        //whatever data is received in the push notification, the variables are being set to that
        final Bundle extras = getIntent().getExtras();
        final String title = extras.getString("title");
        final String body = extras.getString("body");
        final String jobId = extras.getString("job_id");
        final String jobLocation = extras.getString("jobLocation");

        titleTV.setText(title);
        bodyTV.setText(body);
        jobIdTV.setText(jobId);
        jobLocationTV.setText(jobLocation);
    }
}
