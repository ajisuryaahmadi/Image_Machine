package com.wordpress.senidigital.imagemachine.Boundary;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wordpress.senidigital.imagemachine.Controller.Data;
import com.wordpress.senidigital.imagemachine.Entity.DbHelper;
import com.wordpress.senidigital.imagemachine.R;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.crypto.Mac;

public class Machine_Data extends AppCompatActivity {
    Button save, prev, next, get_image;
    String stat_page, machine_code = "";
    TextView selected_image, lbl_serial;
    EditText name, spec, serial, last_update;
    int size_pic = 0;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    DbHelper SQLite = new DbHelper(this);
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
    private List<String> AllImagePath = null;
    private List<byte[]> byteImage = null;
    private String selectedImagePath = null;
    int img_now = 0;
    // Currently displayed user selected image index in userSelectedImageUriList.
    private int currentDisplayedUserSelectImageIndex = 0;
    private static final int MAX_PIC = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_machine);
        dateFormatter = new SimpleDateFormat("dd/mm/yyyy", Locale.getDefault());
        selectedPictureImageView = (ImageView)findViewById(R.id.et_im_image_machine);
        selected_image = (TextView)findViewById(R.id.lbl_dm_selected_image);
        save = (Button)findViewById(R.id.btn_im_save_img);
        prev = (Button)findViewById(R.id.btn_im_prev_img);
        next = (Button)findViewById(R.id.btn_im_next_img);
        get_image = (Button)findViewById(R.id.btn_im_get_img);
        get_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int readExternalStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                if(readExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
                {
                    String requirePermission[] = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(Machine_Data.this, requirePermission, REQUEST_PERMISSION_READ_EXTERNAL);
                }else {
                    openPictureGallery();
                }
            }
        });
        name = (EditText)findViewById(R.id.et_im_name);
        lbl_serial = (TextView)findViewById(R.id.txt_im_serial);
        serial = (EditText)findViewById(R.id.et_im_serial);
        spec = (EditText)findViewById(R.id.et_im_spec_information);
        last_update = (EditText)findViewById(R.id.et_im_last_update);
        stat_page = getIntent().getStringExtra("status");
        machine_code = getIntent().getStringExtra("serial");
        if(stat_page.equals("insert")){
            lbl_serial.setVisibility(View.VISIBLE);
            serial.setVisibility(View.VISIBLE);
            save.setText("SAVE");
            get_image.setText("Add Image");
        }
        else if(stat_page.equals("edit")){
            lbl_serial.setVisibility(View.GONE);
            serial.setVisibility(View.GONE);
            getDataMachine(machine_code);
            save.setText("UPDATE");
            get_image.setText("Change Image");
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSave();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stat_page.equals("insert")){
                    setNextInsert();
                }
                else if(stat_page.equals("edit")){
                    setNextUpdate();
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stat_page.equals("insert")){
                    setPrevInsert();
                }
                else if(stat_page.equals("edit")){
                    setPrevUpdate();
                }
            }
        });
        last_update.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                showDateDialog();
            }
        });
        last_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
    }

    private void showDateDialog(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                last_update.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

    /* Invoke android os system file browser to select images. */
    private void openPictureGallery()
    {
        // Create an intent.
        Intent openAlbumIntent = new Intent();

        // Only show images in the content chooser.
        // If you want to select all type data then openAlbumIntent.setType("*/*");
        // Must set type for the intent, otherwise there will throw android.content.ActivityNotFoundException: No Activity found to handle Intent { act=android.intent.action.GET_CONTENT }
        openAlbumIntent.setType("image/*");

        // Set action, this action will invoke android os browse content app.
        openAlbumIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Start the activity.
        startActivityForResult(openAlbumIntent, REQUEST_CODE_BROWSE_PICTURE);
    }

    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    void setSave(){
        if(stat_page.equals("insert")){
            insert_data_machine(serial.getText().toString(),name.getText().toString(),spec.getText().toString(),last_update.getText().toString(),userSelectedImageUriList.size());
            for(int i = 0; i < userSelectedImageUriList.size(); i++){
                selectedImagePath = getPath(getApplicationContext(),userSelectedImageUriList.get(i));
                AllImagePath.add(selectedImagePath);
                update_image_machine(serial.getText().toString(), AllImagePath.get(i), i + 1);
            }
        }
        else{
            update_data_machine(serial.getText().toString(),name.getText().toString(),spec.getText().toString(), last_update.getText().toString());
            for(int i = 0; i < byteImage.size(); i++){
                update_image_machine(serial.getText().toString(), AllImagePath.get(i), i + 1);
            }
        }
    }

    void setPrevInsert(){
        if( userSelectedImageUriList != null )
        {
            // Get current display image file uri.
            Uri currentUri = userSelectedImageUriList.get(currentDisplayedUserSelectImageIndex);
            System.out.println("ALAMAT: " + currentUri);
            ContentResolver contentResolver = getContentResolver();

            try {

                selected_image.setText((currentDisplayedUserSelectImageIndex+1)+"/"+(size_pic));
                // User content resolver to get uri input stream.
                InputStream inputStream = contentResolver.openInputStream(currentUri);

                // Get the bitmap.
                Bitmap imgBitmap = BitmapFactory.decodeStream(inputStream);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(imgBitmap, 200, 200, true);
                // Show image bitmap in imageview object.
                selectedPictureImageView.setImageBitmap(bMapScaled);

            }catch(FileNotFoundException ex)
            {
                Log.e(TAG_BROWSE_PICTURE, ex.getMessage(), ex);
            }

            // Get total user selected image count.
            size_pic = userSelectedImageUriList.size();

            if(currentDisplayedUserSelectImageIndex <= 0)
            {
                currentDisplayedUserSelectImageIndex=1;
                prev.setEnabled(false);
                next.setEnabled(true);
            }else {
                prev.setEnabled(true);
                next.setEnabled(true);
                currentDisplayedUserSelectImageIndex--;
            }
        }
    }
    void setNextInsert(){
        if( userSelectedImageUriList != null )
        {
            // Get current display image file uri.
            Uri currentUri = userSelectedImageUriList.get(currentDisplayedUserSelectImageIndex);
            System.out.println("ALAMAT: " + currentUri);
            ContentResolver contentResolver = getContentResolver();
            try {

                selected_image.setText((currentDisplayedUserSelectImageIndex+1)+"/"+(size_pic));
                // User content resolver to get uri input stream.
                InputStream inputStream = contentResolver.openInputStream(currentUri);

                // Get the bitmap.
                Bitmap imgBitmap = BitmapFactory.decodeStream(inputStream);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(imgBitmap, 200, 200, true);
                // Show image bitmap in imageview object.
                selectedPictureImageView.setImageBitmap(bMapScaled);

            }catch(FileNotFoundException ex)
            {
                Log.e(TAG_BROWSE_PICTURE, ex.getMessage(), ex);
            }

            // Get total user selected image count.
            size_pic = userSelectedImageUriList.size();

            if(currentDisplayedUserSelectImageIndex >= (size_pic - 1) )
            {
                currentDisplayedUserSelectImageIndex=size_pic-2;
                next.setEnabled(false);
                prev.setEnabled(true);
            }else {
                prev.setEnabled(false);
                next.setEnabled(true);
                currentDisplayedUserSelectImageIndex++;
            }
        }
    }
    void setPrevUpdate(){

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
    void setNextUpdate(){

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

    void insert_data_machine(String serial, String name, String spesification, String last_main,int img_count){
        SQLite.insert(serial, name, spesification, last_main,img_count);
        Toast.makeText(Machine_Data.this, "Inserting Data : " + serial, Toast.LENGTH_LONG).show();
        Intent cek_data = new Intent(Machine_Data.this, View_Machine.class);
        cek_data.putExtra("serial", serial);
        startActivity(cek_data);
        finish();
    }
    void update_image_machine(String serial, String path, int n){
        SQLite.upload_image(serial, path, n);
        Toast.makeText(Machine_Data.this, "Uploading Data : " + serial, Toast.LENGTH_LONG).show();
        Intent cek_data = new Intent(Machine_Data.this, View_Machine.class);
        cek_data.putExtra("serial", serial);
        startActivity(cek_data);
        finish();
    }
    void update_data_machine(String serial, String name, String spesification, String last_main){
        SQLite.update(serial, name, spesification, last_main);
        Intent cek_data = new Intent(Machine_Data.this, View_Machine.class);
        cek_data.putExtra("serial", serial);
        startActivity(cek_data);
        finish();
        Toast.makeText(Machine_Data.this, "Updating Data : " + serial, Toast.LENGTH_LONG).show();
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
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteImage.get(img_sum-1), 0,
                byteImage.get(img_sum-1).length);
        Bitmap bMapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        selectedPictureImageView.setImageBitmap(bMapScaled);
        selected_image.setText((img_now+1)+"/"+(byteImage.size()));
    }
    @Override
    public void onBackPressed() {
        Intent akun = new Intent(Machine_Data.this, List_Machine.class);
        startActivity(akun);
        finish();
    }
    /* When the action Intent.ACTION_GET_CONTENT invoked app return, this method will be executed. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE_BROWSE_PICTURE)
        {
            if(resultCode==RESULT_OK)
            {
                // Get return image uri. If select the image from camera the uri like file:///storage/41B7-12F1/DCIM/Camera/IMG_20180211_095139.jpg
                // If select the image from gallery the uri like content://media/external/images/media/1316970.
                Uri fileUri = data.getData();
                selectedImagePath = getPath(getApplicationContext(),fileUri);
                if(selectedImagePath!="Not found"){
                    // Save user choose image file uri in list.
                    if(userSelectedImageUriList == null)
                    {
                        userSelectedImageUriList = new ArrayList<Uri>();

                    }
                    if(AllImagePath == null){
                        AllImagePath = new ArrayList<String>();
                    }
                    if(stat_page.equals("insert")){
                        userSelectedImageUriList.add(fileUri);


                        //getUriRealPath(getApplicationContext(), fileUri);

                        // Create content resolver.
                        ContentResolver contentResolver = getContentResolver();

                        try {
                            // Open the file input stream by the uri.
                            InputStream inputStream = contentResolver.openInputStream(fileUri);

                            // Get the bitmap.
                            Bitmap imgBitmap = BitmapFactory.decodeStream(inputStream);
                            Bitmap bMapScaled = Bitmap.createScaledBitmap(imgBitmap, 200, 200, true);
                            // Show image bitmap in imageview object.
                            selectedPictureImageView.setImageBitmap(bMapScaled);
                            size_pic = userSelectedImageUriList.size();
                            if(stat_page.equals("insert")){
                                if(size_pic <= 1){
                                    currentDisplayedUserSelectImageIndex = size_pic-1;
                                    selected_image.setText((currentDisplayedUserSelectImageIndex+1)+"/"+(size_pic));
                                    next.setEnabled(false);
                                    prev.setEnabled(false);
                                }
                                else{
                                    currentDisplayedUserSelectImageIndex = size_pic-2;
                                    selected_image.setText((currentDisplayedUserSelectImageIndex+2)+"/"+(size_pic));
                                    next.setEnabled(false);
                                    prev.setEnabled(true);
                                }
                            }
                            inputStream.close();
                        }catch(FileNotFoundException ex)
                        {
                            Log.e(TAG_BROWSE_PICTURE, ex.getMessage(), ex);
                        }catch(IOException ex)
                        {
                            Log.e(TAG_BROWSE_PICTURE, ex.getMessage(), ex);
                        }
                    }
                    else{
                        //update
                        AllImagePath.set(img_now, selectedImagePath);
                        try{
                            FileInputStream instream = new FileInputStream(selectedImagePath);
                            BufferedInputStream bif = new BufferedInputStream(instream);
                            byteImage.remove(img_now);
                            byteImage.add(img_now,new byte[bif.available()]);
                            bif.read(byteImage.get(img_now));
                            selectedPictureImageView.setImageBitmap(BitmapFactory.decodeByteArray(byteImage.get(img_now), 0,
                                    byteImage.get(img_now).length));
                            selected_image.setText((img_now+1)+"/"+(byteImage.size()));
                        }catch (IOException e){
                            System.out.println("Error Exception : " + e.getMessage());
                        }
                    }
                }
                else{
                    openPictureGallery();
                }
            }
        }
    }
    /* After user choose grant read external storage permission or not. */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_PERMISSION_READ_EXTERNAL)
        {
            if(grantResults.length > 0)
            {
                int grantResult = grantResults[0];
                if(grantResult == PackageManager.PERMISSION_GRANTED)
                {
                    // If user grant the permission then open choose image popup dialog.
                    openPictureGallery();
                }else
                {
                    Toast.makeText(getApplicationContext(), "You denied read external storage permission.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
