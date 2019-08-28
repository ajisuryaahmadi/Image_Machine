package com.wordpress.senidigital.imagemachine.Boundary;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wordpress.senidigital.imagemachine.Entity.DbHelper;
import com.wordpress.senidigital.imagemachine.R;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class View_Machine extends AppCompatActivity {
    Button next,prev,edit,delete;
    DbHelper SQLite = new DbHelper(this);
    TextView serial, name, spec, last_update, selected_image;
    String machine_code = null;
    // Log tag that is used to distinguish log info.
    private final static String TAG_BROWSE_PICTURE = "BROWSE_PICTURE";

    // Used when request action Intent.ACTION_GET_CONTENT
    private final static int REQUEST_CODE_BROWSE_PICTURE = 1;

    // Used when request read external storage permission.
    private final static int REQUEST_PERMISSION_READ_EXTERNAL = 2;

    // The image view that used to display user selected image.
    private ImageView selectedPictureImageView;

    // Save user selected image uri list.
    private List<Uri> userSelectedImageUriList = null;
    int size_pic = 0;
    // Currently displayed user selected image index in userSelectedImageUriList.
    private int currentDisplayedUserSelectImageIndex = 0;
    private List<String> AllImagePath = null;
    private List<byte[]> byteImage = null;
    int img_now = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_machine);
        machine_code = getIntent().getStringExtra("serial");
        next = (Button)findViewById(R.id.btn_vm_next);
        prev = (Button)findViewById(R.id.btn_vm_prev);
        edit = (Button)findViewById(R.id.btn_vm_edit);
        delete = (Button)findViewById(R.id.btn_vm_delete);
        selected_image = (TextView)findViewById(R.id.lbl_vm_selected_image);
        selectedPictureImageView = (ImageView)findViewById(R.id.et_vm_image_machine);
        serial = (TextView)findViewById(R.id.txt_vm_serial);
        name = (TextView)findViewById(R.id.txt_vm_name);
        spec = (TextView)findViewById(R.id.txt_vm_spec);
        last_update = (TextView)findViewById(R.id.txt_vm_last_main);
        getDataMachine(machine_code);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_data(String.valueOf(serial.getText()));
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hapus_data(String.valueOf(serial.getText()));
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNext();
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPrev();
            }
        });
    }

    void setPrev(){

        // Get total user selected image count.
        int all_byte = byteImage.size();
        if(img_now <= 1)
        {
            img_now = 0;
            prev.setEnabled(false);
            next.setEnabled(true);
        }else {
            prev.setEnabled(true);
            next.setEnabled(true);
            img_now--;
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteImage.get(img_now), 0,
                byteImage.get(img_now).length);
        Bitmap bMapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        selectedPictureImageView.setImageBitmap(bMapScaled);
        selected_image.setText((img_now+1)+"/"+(byteImage.size()));
    }
    void setNext(){

        // Get total user selected image count.
        int all_byte = byteImage.size();

        if(img_now >= (all_byte - 2) )
        {
            img_now=all_byte-1;
            next.setEnabled(false);
            prev.setEnabled(true);
        }else {
            prev.setEnabled(false);
            next.setEnabled(true);
            img_now++;
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteImage.get(img_now), 0,
                byteImage.get(img_now).length);
        Bitmap bMapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        selectedPictureImageView.setImageBitmap(bMapScaled);
        selected_image.setText((img_now+1)+"/"+(byteImage.size()));
    }

    void edit_data(String serial){
        //Toast.makeText(View_Machine.this, "Serial: " + serial, Toast.LENGTH_LONG).show();
        Intent edit_data = new Intent(View_Machine.this, Machine_Data.class);
        edit_data.putExtra("status","edit");
        edit_data.putExtra("serial", serial);
        startActivity(edit_data);
        finish();
    }
    void hapus_data(String serial){
        Toast.makeText(View_Machine.this, "Deleting Data...", Toast.LENGTH_LONG).show();
        SQLite.delete(serial);
        Intent list_machine = new Intent(View_Machine.this, List_Machine.class);
        startActivity(list_machine);
        finish();
    }
    private void getDataMachine(String Serial) {
        ArrayList<HashMap<String, String>> row = SQLite.getDataMachine(Serial);
        if(AllImagePath == null){
            AllImagePath = new ArrayList<String>();
        }
        byteImage = new ArrayList<byte[]>();
        String id = row.get(0).get("serial_number");
        String name_m = row.get(0).get("name");
        String spek = row.get(0).get("spesification");
        String date_last = row.get(0).get("last_maintenance");
        int img_sum = Integer.parseInt(row.get(0).get("image_count"));
        for(int i = 0; i < img_sum; i++){
            try{
                String image = row.get(0).get("pic"+(i+1));
                AllImagePath.add(image);
                FileInputStream instream = new FileInputStream(image);
                BufferedInputStream bif = new BufferedInputStream(instream);
                byteImage.add(new byte[bif.available()]);
                bif.read(byteImage.get(i));
            }catch (IOException e){
                System.out.println("Error Exception : " + e.getMessage());
            }
        }
        name.setText(name_m);
        serial.setText(id);
        spec.setText(spek);
        last_update.setText(date_last);
        img_now = img_sum - 1;
        if(img_sum <= 1){
            next.setEnabled(false);
            prev.setEnabled(false);
        }
        else{
            next.setEnabled(false);
            prev.setEnabled(true);
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteImage.get(img_now), 0,
                byteImage.get(img_now).length);
        Bitmap bMapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        selectedPictureImageView.setImageBitmap(bMapScaled);
        selected_image.setText((img_now+1)+"/"+(byteImage.size()));
    }

    @Override
    public void onBackPressed() {
        Intent home = new Intent(View_Machine.this, Home.class);
        startActivity(home);
        finish();
    }
}