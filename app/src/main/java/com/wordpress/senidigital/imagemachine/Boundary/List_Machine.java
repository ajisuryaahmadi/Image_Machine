package com.wordpress.senidigital.imagemachine.Boundary;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.wordpress.senidigital.imagemachine.Controller.Data;
import com.wordpress.senidigital.imagemachine.Controller.Machine_List_View;
import com.wordpress.senidigital.imagemachine.Entity.DbHelper;
import com.wordpress.senidigital.imagemachine.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class List_Machine extends AppCompatActivity {

    Button new_machine, sort_machine;
    int sort_set = 0;
    ListView listView;
    AlertDialog.Builder dialog;
    List<Data> itemList = new ArrayList<Data>();
    Machine_List_View adapter;
    DbHelper SQLite = new DbHelper(this);

    public static final String TAG_ID = "serial_number";
    public static final String TAG_NAME = "name";
    public static final String TAG_LAST_MAIN = "last_maintenance";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_machine);
        SQLite = new DbHelper(getApplicationContext());
        listView = (ListView)findViewById(R.id.lv_lm_list_machine);
        adapter = new Machine_List_View(List_Machine.this, itemList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view,
                                    final int position, long id) {
                final String idx = itemList.get(position).getId();
                Intent view_data = new Intent(List_Machine.this, View_Machine.class);
                view_data.putExtra("serial", idx);
                startActivity(view_data);
                finish();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,
                                           final int position, long id) {
                // TODO Auto-generated method stub
                final String idx = itemList.get(position).getId();

                final CharSequence[] dialogitem = {"Edit", "Delete"};
                dialog = new AlertDialog.Builder(List_Machine.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                Intent edit = new Intent(List_Machine.this, Machine_Data.class);
                                edit.putExtra("status","edit");
                                edit.putExtra("serial", idx);
                                startActivity(edit);
                                finish();
                                break;
                            case 1:
                                Toast.makeText(List_Machine.this, "Delete: " + idx, Toast.LENGTH_LONG).show();
                                SQLite.delete(idx);
                                itemList.clear();
                                getAllData(0);
                                break;
                        }
                    }
                }).show();
                return false;
            }
        });
        new_machine = (Button)findViewById(R.id.btn_lm_new_machine);
        new_machine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_new_machine();
            }
        });
        sort_machine = (Button)findViewById(R.id.btm_lm_new_machine);
        sort_machine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sort_set >= 3){
                    sort_set = 0;
                }
                else{
                    sort_set++;
                }
                getAllData(sort_set);
            }
        });
    }

    void set_new_machine(){
        Intent insert_data = new Intent(List_Machine.this,Machine_Data.class);
        insert_data.putExtra("status","insert");
        startActivity(insert_data);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent home = new Intent(List_Machine.this, Home.class);
        startActivity(home);
        finish();
    }

    private void getAllData(int type) {
        itemList.clear();
        if(type==0){
            ArrayList<HashMap<String, String>> row = SQLite.getAllData();
            for (int i = 0; i < row.size(); i++) {
                String id = row.get(i).get(TAG_ID);
                String poster = row.get(i).get(TAG_NAME);
                String title = row.get(i).get(TAG_LAST_MAIN);

                Data data = new Data();

                data.setId(id);
                data.setName(poster);
                data.setLast_main(title);

                itemList.add(data);
            }
            Toast.makeText(List_Machine.this, "ORDER By: Last Maintenance DESC", Toast.LENGTH_LONG).show();
            adapter.notifyDataSetChanged();
        }
        else if(type==1){
            ArrayList<HashMap<String, String>> row = SQLite.getAllData_sort_asc_serial();

            for (int i = 0; i < row.size(); i++) {
                String id = row.get(i).get(TAG_ID);
                String poster = row.get(i).get(TAG_NAME);
                String title = row.get(i).get(TAG_LAST_MAIN);

                Data data = new Data();

                data.setId(id);
                data.setName(poster);
                data.setLast_main(title);

                itemList.add(data);
            }
            Toast.makeText(List_Machine.this, "ORDER By: Last Serial ASC", Toast.LENGTH_LONG).show();
            adapter.notifyDataSetChanged();
        }
        else if(type==2){
            ArrayList<HashMap<String, String>> row = SQLite.getAllData_sort_desc_serial();

            for (int i = 0; i < row.size(); i++) {
                String id = row.get(i).get(TAG_ID);
                String poster = row.get(i).get(TAG_NAME);
                String title = row.get(i).get(TAG_LAST_MAIN);

                Data data = new Data();

                data.setId(id);
                data.setName(poster);
                data.setLast_main(title);

                itemList.add(data);
            }
            Toast.makeText(List_Machine.this, "ORDER By: Last Serial DESC", Toast.LENGTH_LONG).show();
            adapter.notifyDataSetChanged();
        }
        else{
            ArrayList<HashMap<String, String>> row = SQLite.getAllData_sort_asc_date();

            for (int i = 0; i < row.size(); i++) {
                String id = row.get(i).get(TAG_ID);
                String poster = row.get(i).get(TAG_NAME);
                String title = row.get(i).get(TAG_LAST_MAIN);

                Data data = new Data();

                data.setId(id);
                data.setName(poster);
                data.setLast_main(title);

                itemList.add(data);
            }
            Toast.makeText(List_Machine.this, "ORDER By: Last Maintenance ASC", Toast.LENGTH_LONG).show();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemList.clear();
        getAllData(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
