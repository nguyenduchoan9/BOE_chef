package com.example.bipain.boe_restaurantapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import com.example.bipain.boe_restaurantapp.R;
import com.example.bipain.boe_restaurantapp.utils.EndpointManager;

public class MainActivity extends AppCompatActivity {
    EndpointManager endpointManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        endpointManager = new EndpointManager(this);
        EditText edtIP = (EditText) findViewById(R.id.edtIp);
        edtIP.setText(endpointManager.getEndpoint());
    }

    public void clickToSetIP(View view) {
        EditText edtIP = (EditText) findViewById(R.id.edtIp);
        String ip = edtIP.getText().toString();
        if (ip != null && !ip.isEmpty()) {
            endpointManager.setEndpoint(ip);
            startActivity(LoginActivity.newInstance(this));
        } else {
            edtIP.setFocusable(true);
        }
    }
}
