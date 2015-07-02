package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.ListAdapters;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.Modifiers.Modifier;
import com.developmentalstudios.developmental.calcupaytor.R;

import java.util.ArrayList;

/**
 * Created by Scott on 19/06/2015.
 */
public class ModifierAdapter extends ArrayAdapter<Modifier> {
    int layout;
    ArrayList<Modifier> records;
    Context mContext;
    public ModifierAdapter(Context context, int resource, ArrayList<Modifier> objects) {
        super(context, resource, objects);
        layout=resource;
        records=objects;
        mContext=context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        if(convertView==null)
        {
            LayoutInflater inflater=((ActionBarActivity)mContext).getLayoutInflater();
            convertView= inflater.inflate(layout,parent, false);
        }
        Modifier record=records.get(position);
        final TextView lblName=(TextView)convertView.findViewById(R.id.txtdayName);
        lblName.setText(record.DisplayString((int) record.ExtraValue));
       final  TextView Type=(TextView)convertView.findViewById(R.id.txttype);
        Type.setText(record.TypeString(record.ModifierOperationType));
       final  TextView Modifier=(TextView)convertView.findViewById(R.id.txtmodifier);
        Modifier.setText(record.Value+"");


        return convertView;
    }
}
