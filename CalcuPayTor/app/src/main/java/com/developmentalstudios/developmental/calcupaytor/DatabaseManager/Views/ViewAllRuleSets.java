package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Views;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.DatabaseHelper;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.ListAdapters.RuleSetAdapter;
import com.developmentalstudios.developmental.calcupaytor.R;
import com.developmentalstudios.developmental.calcupaytor.ViewHours;

public class ViewAllRuleSets extends ActionBarActivity {
     RuleSetAdapter adapter;
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_rule_sets);
        final ListView listView=(ListView) findViewById(R.id.lvruleSets);
        dbHelper=new DatabaseHelper( getApplicationContext() );

        dbHelper.getAllRuleSets(ViewHours.rulesets);
       adapter=new RuleSetAdapter(this,R.layout.adapter_rule_sets, ViewHours.rulesets);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ViewAllRuleSets.this,AddEditRuleSet.class);
                i.putExtra("RuleSet", ViewHours.rulesets.get(position));
                startActivityForResult(i,ResultCodeEditRecord);
            }
        });
        findViewById(R.id.btnAddJob).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewAllRuleSets.this,AddEditRuleSet.class);

                startActivityForResult(i,ResultCodeNewRecord);
            }
        });

    }
    int ResultCodeNewRecord=14;
    int ResultCodeEditRecord=15;
    int i=0;
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == ResultCodeNewRecord) {
            if (resultCode == RESULT_OK) {



                dbHelper.getAllRuleSets(ViewHours.rulesets);
                adapter.notifyDataSetChanged();
            }
        }
        if(requestCode==ResultCodeEditRecord)
        {
            if(resultCode==RESULT_OK)
            {
              dbHelper.getAllRuleSets(ViewHours.rulesets);



                adapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weekly_pay_modifiers, menu);
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
