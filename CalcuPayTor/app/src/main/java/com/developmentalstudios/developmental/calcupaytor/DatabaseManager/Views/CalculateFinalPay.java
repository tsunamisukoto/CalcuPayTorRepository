package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Views;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.DatabaseHelper;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.ListAdapters.PayCalculationAdapter;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Listeners.CalculationDatePickerDialogListener;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Listeners.ShiftDatePickerDialogListener;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.Modifiers.Modifier;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.Modifiers.WeekdayModifier;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.PayRecord;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.RuleSet;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Views.PayCalculationEntities.PayPacket;
import com.developmentalstudios.developmental.calcupaytor.R;
import com.developmentalstudios.developmental.calcupaytor.ViewHours;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CalculateFinalPay extends ActionBarActivity {
    TextView txtCalc;
    DatabaseHelper dbHelper;
    ArrayList<RuleSet> jobs;
    Date StartDate;
    Date EndDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_final_pay);
        txtCalc=(TextView) findViewById(R.id.txt_paycalculate);
        dbHelper=new DatabaseHelper(getApplicationContext());


        final TextView txtDateStart=(TextView)findViewById(R.id.txtStartDate);
        final TextView txtEndDate=(TextView)findViewById(R.id.txtEndDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE,59);
        EndDate=cal.getTime();
        txtEndDate.setOnClickListener(new CalculationDatePickerDialogListener(this,(TextView)findViewById(R.id.txtEndDate),EndDate,true));
        txtEndDate.setText(dbHelper.dateFormat.format(EndDate));
        cal.add(Calendar.DATE,-7);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);

        StartDate=cal.getTime();
        txtDateStart.setOnClickListener(new CalculationDatePickerDialogListener(this,(TextView)findViewById(R.id.txtStartDate),StartDate,true));
        txtDateStart.setText(dbHelper.dateFormat.format(StartDate));
        findViewById(R.id.btnNextWeek).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(EndDate);
                cal.add(Calendar.DATE,7);
                EndDate=cal.getTime();
                cal.setTime(StartDate);
                cal.add(Calendar.DATE,7);
                StartDate=cal.getTime();
                txtDateStart.setText(dbHelper.dateFormat.format(StartDate));
                txtEndDate.setText(dbHelper.dateFormat.format(EndDate));
                Calculate();
            }
        });

        findViewById(R.id.btnLastWeek).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(EndDate);
                cal.add(Calendar.DATE,-7);
                EndDate=cal.getTime();
                cal.setTime(StartDate);
                cal.add(Calendar.DATE,-7);
                StartDate=cal.getTime();
                txtDateStart.setText(dbHelper.dateFormat.format(StartDate));
                txtEndDate.setText(dbHelper.dateFormat.format(EndDate));
                Calculate();
            }
        });
        Calculate();
    }

    RuleSet FindRuleSet(int i)
    {
        for(RuleSet r: jobs)
        {
            if(r.ID==i)
            return r;
        }
        return null;
    }
    public static long DaysInPeriod;
    private void Calculate() {
        String output="";
        HashMap<Integer,ArrayList<PayPacket>> shifts=new HashMap<>();
        long diff = EndDate.getTime() - StartDate.getTime();
     DaysInPeriod = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        float payTotal =0;
        float subTotal=0;
        int count = 0;
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.elvOutput);

        ArrayList<Modifier> listOfMods =new ArrayList<>();
        jobs=new ArrayList<>();
        jobs=dbHelper.getAllRuleSets(jobs);
        for(RuleSet ru : jobs)
        {
            shifts.put(ru.ID,new ArrayList<PayPacket>());
        }
        float totalSessionOvertime=0;
        float totalSessionOvertimePay=0;
        ArrayList<PayRecord> payRecords=new ArrayList<>();
        dbHelper.getAllPayRecordsForPeriod(payRecords,StartDate,EndDate);

        for (PayRecord r: payRecords)
        {
            float shiftTotal=0;
            float shiftOvertimePay=0;
            float shiftOvertimeHours=0;

            RuleSet ruleset = FindRuleSet(r.RuleSetID);

            Modifier SessionOverTimeModifier= dbHelper.getAllModifiersOfType(listOfMods, Modifier.modTypeSessionOvertime, r.RuleSetID, null).get(0);
            dbHelper.getAllModifiersOfType(listOfMods, Modifier.modTypeStartTime, r.RuleSetID, null);
            Modifier ShiftStartModifierAfter=listOfMods.get(0);
            Modifier ShiftStartModifierBefore=listOfMods.get(1);
            shiftOvertimePay+=ShiftStartModifierAfter.CalculateAddition(r,ruleset.ID);
            shiftOvertimePay+=ShiftStartModifierBefore.CalculateAddition(r,ruleset.ID);
            ruleset.OverTimePayRunningTotal+= ShiftStartModifierAfter.CalculateAddition(r,ruleset.ID);
            ruleset.OverTimePayRunningTotal+= ShiftStartModifierBefore.CalculateAddition(r,ruleset.ID);

            float payRate =ruleset.BasePayRate;

            float numhoursTotal=PayRecord.NumberOfHoursWorked(r.StartDate, r.EndDate);
             if(ruleset.HoursRunningTotal+numhoursTotal>SessionOverTimeModifier.ExtraValue)
            {
                float numberOfHoursAtRate=ruleset.HoursRunningTotal+numhoursTotal-SessionOverTimeModifier.ExtraValue;
                shiftOvertimePay+= SessionOverTimeModifier.ModifyWage(payRate,numberOfHoursAtRate);
                ruleset.OverTimePayRunningTotal+=SessionOverTimeModifier.ModifyWage(payRate,numberOfHoursAtRate);
              //  output+="\r\nOT PAY"+ numberOfHoursAtRate+" addition "+  SessionOverTimeModifier.ModifyWage(payRate,numberOfHoursAtRate);
                shiftOvertimeHours+=numberOfHoursAtRate;
                ruleset.OverTimeHoursRunningTotal+=numberOfHoursAtRate;
            }
            ruleset.HoursRunningTotal+=numhoursTotal;
            ArrayList<Modifier> listOfWeeklyMods =new ArrayList<>();
            dbHelper.getAllModifiersOfType(listOfWeeklyMods,Modifier.modTypeWeekday,r.RuleSetID, null);
            Modifier ShiftOverTimeModifier= dbHelper.getAllModifiersOfType(listOfMods, Modifier.modTypeShiftOvertime, r.RuleSetID, null).get(0);
            float overTimeHours=ShiftOverTimeModifier.ExtraValue;



            for(Modifier j :listOfWeeklyMods)
            {

                WeekdayModifier m = (WeekdayModifier) j;

                float numModified=m.HoursAtPayRate(r.StartDate, r.EndDate);

                numhoursTotal-=numModified;

                if(numModified>overTimeHours)
                {


                    shiftOvertimeHours+=(numModified-overTimeHours);
                    shiftOvertimePay+=ShiftOverTimeModifier.ModifyWage(payRate,(numModified-overTimeHours));
                    ruleset.OverTimePayRunningTotal+=ShiftOverTimeModifier.ModifyWage(payRate,(numModified-overTimeHours));
                    ruleset.OverTimeHoursRunningTotal+=(numModified-overTimeHours);
                    overTimeHours=0;

                }
                else
                {

                    overTimeHours-=numModified;
                }

              subTotal=m.ModifyWage(payRate,numModified);
                shiftTotal+=subTotal;



            }

            if(numhoursTotal>0) {
                shiftTotal += numhoursTotal * payRate;

            }
            shifts.get(ruleset.ID).add(new PayPacket(r,shiftTotal,shiftOvertimePay));
            totalSessionOvertimePay +=shiftOvertimePay;
            totalSessionOvertime+=shiftOvertimeHours;
            payTotal+=shiftTotal+shiftOvertimePay;
            ruleset.PayRunningTotal+=shiftTotal;

        }

        PayCalculationAdapter adapter=new PayCalculationAdapter(this,jobs,shifts);
        expandableListView.setAdapter(adapter);
        output+="\r\nOvertime Pay: " + totalSessionOvertimePay+ " Hours: "+ totalSessionOvertime;
        output+="\r\nGross Total: "+payTotal;
        float taxTotal=0;
        for(RuleSet r :jobs)
        {
            float tax=  r.CalculateTax(dbHelper,DaysInPeriod);
            taxTotal+=tax;

        }
        output+="\r\nTax Total: "+(taxTotal);

        output+="\r\nNet Total: "+(payTotal-taxTotal);
       txtCalc.setText(output);
    }

    private float CalcPayRate(int id) {
        float payRate = dbHelper.getPayRateForRuleSet(id);

        return payRate;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calculate_final_pay, menu);
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
