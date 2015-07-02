package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Listeners;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.DatabaseHelper;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.PayRecord;
import com.developmentalstudios.developmental.calcupaytor.R;

import java.sql.Statement;
import java.util.Calendar;

/**
 * Created by Scott on 20/06/2015.
 */
public class ShiftTimePickerDialogListener implements View.OnClickListener {

    public TextView Resource;
    Context context;
 PayRecord record;

    boolean Start;
    public ShiftTimePickerDialogListener(Context c, TextView _r, PayRecord _rec, boolean start)
    {
        Resource=_r;
        context=c;
      record=_rec;
        Start=start;
    }
    @Override
    public void onClick(View v) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.mytimepicker);

        dialog.show();

        final TimePicker tp = (TimePicker) dialog.findViewById(R.id.timePicker1);
        Calendar calendar=Calendar.getInstance();
        if(Start)
        {
            calendar.setTime(record.StartDate);
        }
        else
        {
            calendar.setTime(record.EndDate);
        }
        tp.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        tp.setCurrentMinute(calendar.get(Calendar.MINUTE));

        Button btnclose = (Button) dialog.findViewById(R.id.btn_close);
        btnclose.setBackgroundResource(R.drawable.buttonshape2);

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                if(Start)
                {

                    calendar.setTime(record.StartDate);
                    calendar.set(Calendar.HOUR_OF_DAY, tp.getCurrentHour());
                    calendar.set(Calendar.MINUTE,tp.getCurrentMinute());
                    record.StartDate= calendar.getTime();
                }
                else
                {

                    calendar.setTime(record.EndDate);
                    calendar.set(Calendar.HOUR_OF_DAY, tp.getCurrentHour());
                    calendar.set(Calendar.MINUTE,tp.getCurrentMinute());
                    while(calendar.getTime().before(record.StartDate))
                    {
                        calendar.add(Calendar.DATE,1);
                    }



                    record.EndDate= calendar.getTime();



                }
                (Resource).setText(DatabaseHelper.timeFormat.format(calendar.getTime()));
                dialog.dismiss();
            }
        });

    }
}

