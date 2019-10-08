package com.hazemelmamlouk.workmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public static final String MESSAGE_STATUS = "MainActivity";
    Button btnEnqueueWork,Constraints;
    Button btnCancelWork;
    TextView tvWorkStatus;
    PeriodicWorkRequest workRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnEnqueueWork = findViewById(R.id.buttonEnqueueWork);
        btnCancelWork = findViewById(R.id.buttonCancelWork);
        tvWorkStatus = findViewById(R.id.textViewWorkStatus);
        Constraints=findViewById(R.id.next);
        final WorkManager mWorkManager = WorkManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            workRequest = new PeriodicWorkRequest.Builder(MyWorker.class, 5, TimeUnit.MINUTES).build();
        }
        btnEnqueueWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWorkManager.enqueue(workRequest);
            }
        });
        btnCancelWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkManager.getInstance().cancelWorkById(workRequest.getId());
            }
        });
        mWorkManager.getWorkInfoByIdLiveData(workRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    tvWorkStatus.append(state.toString() + "\n");

                }
            }
        });
        Constraints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        });
    }
}
