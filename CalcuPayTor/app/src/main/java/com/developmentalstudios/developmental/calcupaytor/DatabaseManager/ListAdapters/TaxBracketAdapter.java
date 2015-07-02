package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.ListAdapters;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.TaxBracket;
import com.developmentalstudios.developmental.calcupaytor.R;

import java.util.ArrayList;

/**
 * Created by Scott on 19/06/2015.
 */
public class TaxBracketAdapter extends ArrayAdapter<TaxBracket> {
    int layout;
    ArrayList<TaxBracket> records;
    Context mContext;
    public TaxBracketAdapter(Context context, int resource, ArrayList<TaxBracket> objects) {
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
        TaxBracket record=records.get(position);
        final TextView lblName=(TextView)convertView.findViewById(R.id.txtMax);
        lblName.setText("$ "+ record.BracketMax);
       final  TextView Type=(TextView)convertView.findViewById(R.id.txtPercent);
        Type.setText(record.PercentTax+" %");

        return convertView;
    }
}
