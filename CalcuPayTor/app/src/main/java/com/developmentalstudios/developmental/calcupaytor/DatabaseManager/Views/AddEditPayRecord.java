package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Views;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.DatabaseHelper;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Listeners.ShiftDatePickerDialogListener;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Listeners.ShiftTimePickerDialogListener;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.PayRecord;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.RuleSet;
import com.developmentalstudios.developmental.calcupaytor.R;
import com.developmentalstudios.developmental.calcupaytor.ViewHours;

import java.util.Calendar;
import java.util.Date;

public class AddEditPayRecord extends ActionBarActivity {
    PayRecord record;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pay_record);
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        GenerateNewPayRecord();
        final TextView txtTitle=(TextView) findViewById(R.id.txt_title);
        final TextView txtDateStart=(TextView) findViewById(R.id.txt_date_start);
        final TextView txtTimeStart=(TextView) findViewById(R.id.txt_time_start);
        final TextView txtTimeEnd=(TextView) findViewById(R.id.txt_time_end);
        final Button btnSave=(Button) findViewById(R.id.btn_save);
        final Button btnCancel=(Button) findViewById(R.id.btn_cancel);
        final Spinner spinnerJob=(Spinner)findViewById(R.id.spinner_Jobs);

        ArrayAdapter<RuleSet> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, ViewHours.rulesets);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJob.setAdapter(adapter);
        spinnerJob.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                record.RuleSetID=ViewHours.rulesets.get(position).ID;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        if(extras!=null)
        {
        if(extras.containsKey("PayRecordParcel")) {
            record = (PayRecord) i.getParcelableExtra("PayRecordParcel");

        }


        }
        txtTimeStart.setText(DatabaseHelper.timeFormat.format(record.StartDate));
        txtTimeEnd.setText(DatabaseHelper.timeFormat.format(record.EndDate));
        txtDateStart.setText(DatabaseHelper.dateFormat.format(record.StartDate));


        txtDateStart.setOnClickListener(new ShiftDatePickerDialogListener(this,(TextView)findViewById(R.id.txt_date_start),record,true));

        txtTimeStart.setOnClickListener(new ShiftTimePickerDialogListener(this,(TextView)findViewById(R.id.txt_time_start),record,true));
        txtTimeEnd.setOnClickListener(new ShiftTimePickerDialogListener(this,(TextView)findViewById(R.id.txt_time_end),record,false));
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(record.EndDate);
                Calendar calendar2=Calendar.getInstance();
                calendar2.setTime(record.StartDate);

                while(calendar.before(calendar2))
                {
                    calendar.add(Calendar.DATE ,1 );

                }
                while(hoursDifference(calendar.getTime(),calendar2.getTime())>24)
                {
                    calendar.add(Calendar.DATE,-1);

                }
                record.EndDate=calendar.getTime();


                Intent i = new Intent();
                i.putExtra("PayRecordParcel",record);
                setResult(Activity.RESULT_OK,i);
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private static int hoursDifference(Date date1, Date date2) {

        final int MILLI_TO_HOUR = 1000 * 60 * 60;
        return (int) (date1.getTime() - date2.getTime()) / MILLI_TO_HOUR;
    }
    private void GenerateNewPayRecord() {
        Calendar cal=Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.MINUTE,0);

        Date start = cal.getTime();

        cal.add(Calendar.HOUR_OF_DAY,2);


        record=new PayRecord(1,start,cal.getTime() );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_pay_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
