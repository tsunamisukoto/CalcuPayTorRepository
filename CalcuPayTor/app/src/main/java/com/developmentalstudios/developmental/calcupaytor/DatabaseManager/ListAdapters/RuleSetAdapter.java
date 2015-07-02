package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.ListAdapters;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.DatabaseHelper;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.RuleSet;
import com.developmentalstudios.developmental.calcupaytor.R;

import java.util.ArrayList;

/**
 * Created by Scott on 19/06/2015.
 */
public class RuleSetAdapter extends ArrayAdapter<RuleSet> {
    int layout;
    ArrayList<RuleSet> records;
    Context mContext;
    public RuleSetAdapter(Context context, int resource, ArrayList<RuleSet> objects) {
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
        RuleSet record=records.get(position);
        TextView lblName=(TextView)convertView.findViewById(R.id.txtSetName);
        lblName.setText(record.Name);
        TextView lblBasePay=(TextView)convertView.findViewById(R.id.txtbasepay);
        lblBasePay.setText(record.BasePayRate+"");

        TextView lblDateCreated=(TextView)convertView.findViewById(R.id.txtDateCreated);
        lblDateCreated.setText(DatabaseHelper.inputFormat.format(record.DateCreated));


        return convertView;
    }
}
