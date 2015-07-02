package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Views;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.DatabaseHelper;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.ListAdapters.ModifierAdapter;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.ListAdapters.TaxBracketAdapter;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.Modifiers.Modifier;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.Modifiers.SessionOvertimeModifier;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.Modifiers.ShiftOvertimeModifier;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.Modifiers.ShiftStartTimeModifier;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.Modifiers.WeekdayModifier;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.RuleSet;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.TaxBracket;
import com.developmentalstudios.developmental.calcupaytor.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddEditRuleSet extends ActionBarActivity {
    RuleSet record;
    DatabaseHelper dbHelper;
    private int ResultCodeEditModifier =1182;
    ModifierAdapter adapter;
    TaxBracketAdapter bracketAdapter;
    final ArrayList<Modifier> weeklyModifiers=new ArrayList<>();
    private int ResultCodeEditBracket=1244;
    final ArrayList<TaxBracket> taxBrackets=new ArrayList<>();
    private int ResultCodeAddTaxBracket=1243;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_rule_set);
        Intent intent = getIntent();
        dbHelper=new DatabaseHelper( getApplicationContext() );
        Bundle extras = intent.getExtras();
        final EditText inputName=(EditText)findViewById(R.id.inputJobName);
        final EditText inputPay=(EditText)findViewById(R.id.inputPayRate);
        final ListView listViewModifiers=(ListView) findViewById(R.id.lvmodifiers);
        final ListView listViewBrackets=(ListView) findViewById(R.id.lvTaxBrackets);
        weeklyModifiers.add(new WeekdayModifier(1, 2.0f, 1, 1));

        for(int i=0; i<5;i++)
        {
            weeklyModifiers.add(new WeekdayModifier(1, 1.0f, 1, i + 2));

        }
        weeklyModifiers.add(new WeekdayModifier(1, 1.5f, 1, 7));
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY,5);
        calendar.set(Calendar.MINUTE,30);
        weeklyModifiers.add(new ShiftStartTimeModifier(2, 3, 1, calendar.getTime(), ShiftStartTimeModifier.subtypeAfter));
        weeklyModifiers.add(new ShiftStartTimeModifier(2, 0, 1, calendar.getTime(), ShiftStartTimeModifier.subtypeBefore));
        weeklyModifiers.add((new ShiftOvertimeModifier(1, 0.5f, 1, 4, ShiftOvertimeModifier.subtypeHourly)));
        weeklyModifiers.add((new SessionOvertimeModifier(2, 3, 1, 30)));
        record=new RuleSet("Name", new Date(),17.5f);

        if(extras!=null) {
            if (extras.containsKey("RuleSet")) {
                record = (RuleSet) intent.getParcelableExtra("RuleSet");

                dbHelper.getAllModifiersOfType(weeklyModifiers,Modifier.modTypeWeekday,record.ID, null);
                ArrayList<Modifier> mods=new ArrayList<>();
                weeklyModifiers.add(dbHelper.getAllModifiersOfType(mods, Modifier.modTypeShiftOvertime, record.ID, null).get(0));
                weeklyModifiers.add(dbHelper.getAllModifiersOfType(mods,Modifier.modTypeSessionOvertime,record.ID, null).get(0));
                dbHelper.getAllModifiersOfType(mods,Modifier.modTypeStartTime,record.ID, null);
                weeklyModifiers.add(mods.get(0));
                weeklyModifiers.add(mods.get(1));
               dbHelper.getTaxBracketsForRuleSet(taxBrackets,record.ID);
            }
        }
        bracketAdapter=new TaxBracketAdapter(this,R.layout.adapter_tax_brackets,taxBrackets);
        listViewBrackets.setAdapter(bracketAdapter);
        listViewBrackets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(AddEditRuleSet.this, AddEditPayBracket.class);
                i.putExtra("Brack",taxBrackets.get(position));
                startActivityForResult(i, ResultCodeEditBracket);
            }
        });

        adapter=new ModifierAdapter(this,R.layout.adapter_weekly_modifiers,weeklyModifiers);
        listViewModifiers.setAdapter(adapter);
        listViewModifiers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(AddEditRuleSet.this, AddEditModifier.class);
                i.putExtra("Mod",weeklyModifiers.get(position));
                startActivityForResult(i, ResultCodeEditModifier);
            }
        });
        inputName.setText(record.Name);
        inputPay.setText(record.BasePayRate+"");
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent();

                record.BasePayRate=Float.parseFloat((inputPay.getText().toString()));
                record.Name=inputName.getText().toString();

                long id;
                if(record.ID==-1)
                {
                    id=dbHelper.InsertRuleSet(record,null);
                }
                else
                {

                   id= dbHelper.EditRuleSet(record,null);
                }



                for(Modifier m:weeklyModifiers)
                {
                    m.RuleSetID=(int)id;
                    dbHelper.InsertModifier(m,null);
                }
                for(TaxBracket t:taxBrackets)
                {
                    t.RuleSet=(int)id;

                    dbHelper.InsertTaxBracket(t, null);
                }
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent();


                setResult(Activity.RESULT_CANCELED, i);
                finish();
            }
        });

        findViewById(R.id.btnAddTax).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AddEditRuleSet.this, AddEditPayBracket.class);
                startActivityForResult(i, ResultCodeAddTaxBracket);


            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == ResultCodeEditModifier) {
            if (resultCode == RESULT_OK) {
                Modifier r = data.getParcelableExtra("Mod");
                for(int i =0; i<weeklyModifiers.size(); i++)
                {
                    if(r.Type==Modifier.modTypeWeekday) {
                        if (weeklyModifiers.get(i).ExtraValue == r.ExtraValue) {
                            weeklyModifiers.set(i, r);
                        }
                    }
                    else
                    {
                        if(weeklyModifiers.get(i).Type==r.Type)
                        {
                            weeklyModifiers.set(i, r);
                        }
                    }

                }
                adapter.notifyDataSetChanged();

            }
        }

        if (requestCode == ResultCodeEditBracket) {
            if (resultCode == RESULT_OK) {
                TaxBracket r = data.getParcelableExtra("Brack");
                for(int i=0; i<taxBrackets.size();i++)
                {
                    TaxBracket t=taxBrackets.get(i);
                    if(t.ID==r.ID)
                    {
                        taxBrackets.set(i,r);
                    }

                }
                bracketAdapter.notifyDataSetChanged();

            }
        }

        if (requestCode == ResultCodeAddTaxBracket) {
            if (resultCode == RESULT_OK) {
                TaxBracket r = data.getParcelableExtra("Brack");

                        taxBrackets.add(r);



                bracketAdapter.notifyDataSetChanged();

            }
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_edit_rule_set, menu);
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
