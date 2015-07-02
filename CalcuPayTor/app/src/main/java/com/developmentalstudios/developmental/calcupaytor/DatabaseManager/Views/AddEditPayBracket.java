package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Views;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.TaxBracket;
import com.developmentalstudios.developmental.calcupaytor.R;

public class AddEditPayBracket extends ActionBarActivity {
TaxBracket record;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_pay_bracket);
        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        final EditText txtTax = (EditText)findViewById(R.id.txtTaxPercent);
        final EditText txtMax = (EditText)findViewById(R.id.txtMax);
        record=new TaxBracket(-1,20,20000);
        if(extras!=null) {
            if (extras.containsKey("Brack")) {
                record = intent.getParcelableExtra("Brack");



            }
        }
        txtTax.setText(record.PercentTax+"");



        txtMax.setText( record.BracketMax+"");

        Button btnSave =(Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =getIntent();

                record.PercentTax=Float.parseFloat(txtTax.getText().toString());
                record.BracketMax=Float.parseFloat(txtMax.getText().toString());

                i.putExtra("Brack",record);
                setResult(RESULT_OK, i);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_edit_pay_bracket, menu);
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
