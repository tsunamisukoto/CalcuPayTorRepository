package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.ListAdapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.DatabaseHelper;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.RuleSet;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Views.CalculateFinalPay;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Views.PayCalculationEntities.PayPacket;
import com.developmentalstudios.developmental.calcupaytor.R;

/**
 * Created by Scott on 27/06/2015.
 */

public class PayCalculationAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<RuleSet> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Integer, ArrayList<PayPacket>> _listDataChild;

    public PayCalculationAdapter(Context context, List<RuleSet> listDataHeader,
                                 HashMap<Integer, ArrayList<PayPacket>> listChildData) {
        this._context = context;
        Collections.sort(listDataHeader);
        this._listDataHeader =  listDataHeader;

        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).ID)
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        PayPacket record = ((PayPacket) getChild(groupPosition, childPosition));
        final String childText = DatabaseHelper.dateFormat.format(record.Record.StartDate)+"\r\n"+ DatabaseHelper.timeFormat.format(record.Record.StartDate)+" - " + DatabaseHelper.timeFormat.format(record.Record.EndDate);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.adapter_calc_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);


        TextView lblTotal = (TextView) convertView
                .findViewById(R.id.lblPay);

        lblTotal.setText("$" + record.Total);
        TextView lblOverTimeTotal = (TextView) convertView
                .findViewById(R.id.lblOverTimePay);

        lblOverTimeTotal.setText("$" + record.Additions);



        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).ID)
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        RuleSet ruleSet = (RuleSet) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.adapter_calc_head, null);
        }
float tax=ruleSet.CalculateTax(new DatabaseHelper(_context), CalculateFinalPay.DaysInPeriod);
    TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(ruleSet.Name );
        TextView lblTax = (TextView) convertView
                .findViewById(R.id.lblTaxTotal);
        lblTax.setText( "$"+String.format("%.2f",-tax));




        TextView lblOvertime = (TextView) convertView
                .findViewById(R.id.lblOverTimeHours);
lblOvertime.setText(String.format("%.2f",ruleSet.OverTimeHoursRunningTotal));
        TextView lblHours = (TextView) convertView
                .findViewById(R.id.lblHours);
        lblHours.setText(String.format("%.2f",ruleSet.HoursRunningTotal));
        TextView lblPay = (TextView) convertView
                .findViewById(R.id.lblPay);

        lblPay.setText("$"+String.format("%.2f",ruleSet.PayRunningTotal));
        TextView lblOvertimePay = (TextView) convertView
                .findViewById(R.id.lblOvertimePayhead);
        lblOvertimePay.setText("$"+String.format("%.2f",ruleSet.OverTimePayRunningTotal));
        TextView lblGross = (TextView) convertView
                .findViewById(R.id.lblGrossPay);
        lblGross.setText("$"+String.format("%.2f",ruleSet.OverTimePayRunningTotal+ruleSet.PayRunningTotal));
        TextView lblNet = (TextView) convertView
                .findViewById(R.id.lblNetPay);
        lblNet.setText("$"+String.format("%.2f",ruleSet.OverTimePayRunningTotal+ruleSet.PayRunningTotal-tax));

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}