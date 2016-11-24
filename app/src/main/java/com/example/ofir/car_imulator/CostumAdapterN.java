package com.example.ofir.car_imulator;

import android.content.Context;
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
public class CostumAdapterN extends BaseAdapter {
    DBHelper db;
    Context context;
    String[] names;
    private static LayoutInflater inflater=null;

    public CostumAdapterN(Context context, String[] names) {
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
                Note myN=db.FindNoteByName(0);
                String mNames ="";
                if(myN!=null) {
                    if (myN.getName().length<=1){
                        db.DeleteNote(0);
                    }
                 else {
                        for (int i = 0; i < myN.getName().length; i++) {
                            if (myN.getName()[i].equals(names[position]) == false) {
                                mNames += myN.getName()[i] + ",";
                            }
                        }
                        Note note = new Note(0, mNames);
                        db.UpdateNote(note);
                    }
                }
            }
        });
        return vi;
    }

}
