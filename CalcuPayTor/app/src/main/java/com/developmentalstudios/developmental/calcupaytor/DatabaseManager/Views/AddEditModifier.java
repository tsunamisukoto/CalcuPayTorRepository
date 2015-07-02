package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Views;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.DatabaseHelper;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Listeners.ModifierTimePickerDialogListener;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.Modifiers.Modifier;
import com.developmentalstudios.developmental.calcupaytor.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class AddEditModifier extends ActionBarActivity {
Modifier record ;
    Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_modifier);
        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        ArrayList<String> types= new ArrayList<>();
        for(int i=1; i<4; i++)
        {
            types.add(Modifier.TypeString(i));
        }
//        final Spinner spinnerDay=(Spinner)findViewById(R.id.spinnerday);
        TextView txtName = (TextView)findViewById(R.id.lblName);
        final EditText txtValue = (EditText)findViewById(R.id.txtValue);
        final Spinner spinnerType=(Spinner)findViewById(R.id.spinnermodtype);
        final View divMaxHours=(View)findViewById(R.id.divMaxHours);
        final View divDate=(View)findViewById(R.id.divDate);
        final TextView txtStartDate = (TextView)findViewById(R.id.txtStartTime);
        final TextView txtMaxHours = (TextView)findViewById(R.id.txtHours);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        if(extras!=null) {
            if (extras.containsKey("Mod")) {
                record = intent.getParcelableExtra("Mod");
                txtValue.setText(record.Value+"");
                spinnerType.setSelection(record.ModifierOperationType-1);
                if(record.Type==Modifier.modTypeStartTime)
                {
                    try {
                        if(date==null)
                        date=DatabaseHelper.timeFormat.parse(record.ModifierString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    txtStartDate.setText((record.ModifierString ));
                    txtStartDate.setOnClickListener(new ModifierTimePickerDialogListener(this,txtStartDate,date));
                    divDate.setVisibility(View.VISIBLE);
                }
                if(record.Type==Modifier.modTypeSessionOvertime||record.Type==Modifier.modTypeShiftOvertime)
                {
                    txtMaxHours.setText(record.ExtraValue+"");
                    divMaxHours.setVisibility(View.VISIBLE);
                }

                    txtName.setText( record.DisplayString((int) record.ExtraValue));

            }
        }



        final Spinner spinnerSubType=(Spinner)findViewById(R.id.spinnerSubType);

        ArrayAdapter<String> subTypeAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,record.listSubTypes);
        subTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubType.setAdapter(subTypeAdapter);
        spinnerSubType.setSelection(record.SubType);



        Button btnSave =(Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =getIntent();
                record.ModifierOperationType=spinnerType.getSelectedItemPosition()+1;
                record.Value=Float.parseFloat(txtValue.getText().toString());
                record.SubType=spinnerSubType.getSelectedItemPosition();

                if(record.Type==Modifier.modTypeStartTime)
                {

                    record.ModifierString=txtStartDate.getText().toString();
                }

                if(record.Type==Modifier.modTypeSessionOvertime||record.Type==Modifier.modTypeShiftOvertime)
                {
                 record.ExtraValue=   Float.parseFloat(txtMaxHours.getText().toString());
                }
                i.putExtra("Mod",record);
             setResult(RESULT_OK, i);
                finish();
            }
        });

        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent();


                setResult(Activity.RESULT_CANCELED, i);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_edit_modifier, menu);
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
