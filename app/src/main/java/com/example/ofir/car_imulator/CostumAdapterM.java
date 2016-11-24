package com.example.ofir.car_imulator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by ofir on 23/02/2016.
 */
public class CostumAdapterM extends BaseAdapter {
    DBHelper db;
    Context context;
    String[] names;
    private static LayoutInflater inflater=null;

    public CostumAdapterM(Context context, String[] names) {
        this.context = context;
        this.names = names;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
            return names[position];
        }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        db = new DBHelper(context);
        View vi=convertView;
        if(vi==null)
            vi=inflater.inflate(R.layout.custom_row_m,null);
        final TextView name=(TextView)vi.findViewById(R.id.textViewCustomRowM);
        name.setText(names[position]);
        name.setTextColor(Color.BLACK);
        ImageButton imageButton=(ImageButton)vi.findViewById(R.id.buttonCustomRowM);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Media mediaO = db.FindMediaByName(0);
                String mNames = "";
                String dirs="";
                if (mediaO != null) {
                    if (mediaO.getName().length<=1){
                        db.DeleteMedia(0);
                    }
                     else {
                        for (int i = 0; i < mediaO.getName().length; i++) {
                            if (mediaO.getName()[i].equals(names[position]) == false) {
                                mNames += mediaO.getName()[i] + ",";
                            }
                        }
                        Media media = new Media(0, mNames);
                        db.UpdateMedia(media);
                    }
                }
            }
        });
        return vi;
    }
}
