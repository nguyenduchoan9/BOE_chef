package com.example.bipain.boe_restaurantapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.utils.ToastUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

public class ObserActivity extends AppCompatActivity {
    private PublishSubject<Integer> scheduler;

    private PublishSubject<ObserActivity> endPoint = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obser);
        scheduler = PublishSubject.create();
        scheduler
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(endPoint)
                .subscribe(id -> ToastUtils.toastShortMassage(ObserActivity.this, "dmmmmmm"));
        Button btn = (Button) findViewById(R.id.click);
        btn.setOnClickListener(v -> {
            scheduler.onNext(1);
        });
    }

    @Override
    protected void onDestroy() {
        endPoint.onNext(this);
        super.onDestroy();
    }
}
