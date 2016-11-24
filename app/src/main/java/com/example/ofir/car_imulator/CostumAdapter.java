package com.example.ofir.car_imulator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by ofir on 15/02/2016.
 */
public class CostumAdapter extends BaseAdapter{
    Context context;
    String[] names;
    private static LayoutInflater inflater=null;

    public CostumAdapter(Context context, String[] names) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(vi==null)
            vi=inflater.inflate(R.layout.custom_row,null);
        TextView name=(TextView)vi.findViewById(R.id.custom_row_textView);
        name.setText(names[position]);
        return vi;
    }
}
