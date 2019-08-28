package com.wordpress.senidigital.imagemachine.Boundary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wordpress.senidigital.imagemachine.Entity.DbHelper;
import com.wordpress.senidigital.imagemachine.R;

public class Home extends AppCompatActivity implements View.OnClickListener{

    DbHelper SQLite = new DbHelper(this);
    Button machine_data, scan_code;
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        qrScan = new IntentIntegrator(this);
        machine_data = (Button)findViewById(R.id.btn_home_machine_data);
        scan_code = (Button)findViewById(R.id.btn_home_scan_code);
        machine_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_Machine_data();
            }
        });
        scan_code.setOnClickListener(this);
    }

    void set_Machine_data(){
        Intent goto_machine_data = new Intent(Home.this, List_Machine.class);
        startActivity(goto_machine_data);
        finish();
    }
    void set_scan_code(){
        Toast.makeText(Home.this, "Scan Code Starting!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        qrScan.initiateScan();
    }
    //scan kodeQR
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qraji                          code has nothing in it
            String[] separated = result.getContents().toString().split(";");
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(Home.this, "Code: " + result.getContents(), Toast.LENGTH_LONG).show();
                search_machine(String.valueOf(result.getContents()));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    void search_machine(String serial){
        Intent cek_data = new Intent(Home.this, View_Machine.class);
        cek_data.putExtra("serial", serial);
        startActivity(cek_data);
        finish();
    }
}
