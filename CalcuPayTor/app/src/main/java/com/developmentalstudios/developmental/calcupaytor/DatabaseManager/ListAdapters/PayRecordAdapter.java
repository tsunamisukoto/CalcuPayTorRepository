package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.ListAdapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.DatabaseHelper;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.PayRecord;
import com.developmentalstudios.developmental.calcupaytor.R;

import java.util.ArrayList;

/**
 * Created by Scott on 19/06/2015.
 */
public class PayRecordAdapter extends ArrayAdapter<PayRecord> {
    int layout;
    ArrayList<PayRecord> records;
    Context mContext;
    public PayRecordAdapter(Context context, int resource, ArrayList<PayRecord> objects) {
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

        PayRecord record=records.get(position);
        TextView lblName=(TextView)convertView.findViewById(R.id.txt_name);
        lblName.setText( new DatabaseHelper(mContext).getNameForRuleSet(record.RuleSetID));

        TextView txtStartDate=(TextView)convertView.findViewById(R.id.txtStartDate);
        txtStartDate.setText(DatabaseHelper.dateFormat.format(record.StartDate));

        TextView lblStartDate=(TextView)convertView.findViewById(R.id.txt_startdate);
        lblStartDate.setText(DatabaseHelper.timeFormat.format(record.StartDate));
        TextView lblEndDate=(TextView)convertView.findViewById(R.id.txt_enddate);
        lblEndDate.setText(DatabaseHelper.timeFormat.format(record.EndDate));
        TextView lblNumHours=(TextView)convertView.findViewById(R.id.txt_num_hours);
        lblNumHours.setText(String.format("%.2f", PayRecord.NumberOfHoursWorked(record.StartDate, record.EndDate)));


        return convertView;
    }
}
