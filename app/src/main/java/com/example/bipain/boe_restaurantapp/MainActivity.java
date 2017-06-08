package com.example.bipain.boe_restaurantapp;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickToSetIP(View view) {
        EditText edtIP = (EditText) findViewById(R.id.edtIp);
        String ip = edtIP.getText().toString();
        if (ip != null && !ip.isEmpty()) {
            Intent intent = new Intent(getBaseContext(), TabManagerActivity.class);
            startActivity(intent);
        } else {
            edtIP.setFocusable(true);
        }
    }
}
