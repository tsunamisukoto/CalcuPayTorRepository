package com.developmentalstudios.developmental.calcupaytor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.DatabaseHelper;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.ListAdapters.PayRecordAdapter;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.Modifiers.Modifier;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.PayRecord;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.PersonalConfig;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.RuleSet;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Views.AddEditPayRecord;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Views.CalculateFinalPay;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Views.Disclaimer;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Views.ViewAllRuleSets;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ViewHours extends ActionBarActivity  {
    public static final ArrayList<PayRecord> payrecords = new ArrayList<PayRecord>();
   public static final ArrayList<Modifier> weeklymodifiers= new ArrayList<Modifier>();
    public static final ArrayList<RuleSet> rulesets=new ArrayList<>();
    public static PersonalConfig personalConfig;
    PayRecordAdapter payRecordAdapter ;
    DatabaseHelper dbHelper;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hours);
        dbHelper = new DatabaseHelper(getApplicationContext());
        personalConfig=dbHelper.getPersonalConfig();
        final ListView listView = ((ListView) findViewById(R.id.list_pay_records));
        final Button btnNew = (Button) findViewById(R.id.btn_add_pay);
        final Button btnCalc = (Button) findViewById(R.id.btn_Calculate);

        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewHours.this, CalculateFinalPay.class);
                startActivity(i);

            }
        });
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewHours.this, AddEditPayRecord.class);
                startActivityForResult(i, ResultCodeNewRecord);
            }
        });


        dbHelper.getAllModifiersOfType(weeklymodifiers, Modifier.modTypeWeekday,1, null);
        dbHelper.getAllRuleSets(rulesets);

  GetallPayRecords();

        payRecordAdapter = new PayRecordAdapter(this, R.layout.adapter_pay_record_layout, payrecords);
        listView.setAdapter(payRecordAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ViewHours.this, AddEditPayRecord.class);
                i.putExtra("PayRecordParcel", payrecords.get(position));
                startActivityForResult(i, ResultCodeEditRecord);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(ViewHours.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirm Delete Shift")
                        .setMessage("Do You Really want to Delete this shift?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            dbHelper.DeletePayRecord(payrecords.get(position),null  );
                                GetallPayRecords();
                                payRecordAdapter.notifyDataSetChanged();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });

    }
    void GetallPayRecords()
    {
//        Calendar ca = Calendar.getInstance();
//        ca.setTime(new Date());
//        ca.add(Calendar.DATE,personalConfig.WeeksAtATime*7);
//        ca.set(Calendar.DAY_OF_WEEK,personalConfig.StartDay);
//        dbHelper.getAllPayRecordsForPeriod(payrecords,new Date(), ca.getTime());
        dbHelper.getAllPayRecords(payrecords);
    }
    int ResultCodeNewRecord =1124;
    int ResultCodeEditRecord =1125;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_hours, menu);
        return true;
    }
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == ResultCodeNewRecord) {
            if (resultCode == RESULT_OK) {
           PayRecord r = data.getParcelableExtra("PayRecordParcel");

                dbHelper.InsertPayRecord(r,null);
                GetallPayRecords();
                payRecordAdapter.notifyDataSetChanged();
            }
        }
        if(requestCode==ResultCodeEditRecord)
        {
            if(resultCode==RESULT_OK)
            {
                PayRecord r = data.getParcelableExtra("PayRecordParcel");
               dbHelper.EditPayRecord(r,null    );

                GetallPayRecords();
                payRecordAdapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(ViewHours.this, ViewAllRuleSets.class);
            startActivity(i);

        }
        if (id == R.id.disclaimer) {
            Intent i = new Intent(ViewHours.this, Disclaimer.class);
            startActivity(i);

        }


        return super.onOptionsItemSelected(item);
    }
}
