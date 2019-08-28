package com.wordpress.senidigital.imagemachine.Controller;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wordpress.senidigital.imagemachine.R;

import java.util.List;

public class Machine_List_View extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Data> items;

    public Machine_List_View(Activity activity, List<Data> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.activity_machine_list_view, null);

        TextView id = (TextView) convertView.findViewById(R.id.txt_lv_serial);
        TextView name = (TextView) convertView.findViewById(R.id.txt_lv_name);
        TextView last_main = (TextView) convertView.findViewById(R.id.txt_lv_last_main);

        Data data = items.get(position);

        id.setText(data.getId());
        name.setText(data.getName());
        last_main.setText(data.getLast_Main());

        return convertView;
    }
}
