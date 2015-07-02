package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Listeners;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.DatabaseHelper;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.PayRecord;
import com.developmentalstudios.developmental.calcupaytor.R;

import java.util.Calendar;

/**
 * Created by Scott on 20/06/2015.
 */
public class ShiftDatePickerDialogListener implements View.OnClickListener {

    public TextView Resource;
    Context context;
   PayRecord record;
    boolean Start;
    public ShiftDatePickerDialogListener(Context c, TextView _r, PayRecord _rec, boolean start) {
        Resource = _r;
        context = c;
    record=_rec;
        Start=start;
    }

    @Override
    public void onClick(View v) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.mydatepicker);

        dialog.show();

        final DatePicker tp = (DatePicker) dialog.findViewById(R.id.datePicker1);

        Button btnclose = (Button) dialog.findViewById(R.id.btn_close);
       btnclose.setBackgroundResource(R.drawable.buttonshape2);

        Calendar calendar=Calendar.getInstance();
        if(Start)
        {

            calendar.setTime(record.StartDate);
        }
        else
        {

            calendar.setTime(record.EndDate);
        }
        tp.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                calendar.set(tp.getYear(),tp.getMonth(),tp.getDayOfMonth());
                (Resource).setText(DatabaseHelper.dateFormat.format(calendar.getTime()));
                if(Start)
                {

                    calendar.setTime(record.StartDate);
                    calendar.set(Calendar.DAY_OF_MONTH, tp.getDayOfMonth());
                    calendar.set(Calendar.MONTH,tp.getMonth());
                    record.StartDate= calendar.getTime();
                }
              else
                {

                    calendar.setTime(record.EndDate);
                    calendar.set(Calendar.DAY_OF_MONTH, tp.getDayOfMonth());
                    calendar.set(Calendar.MONTH,tp.getMonth());
                    record.EndDate= calendar.getTime();
                }
                dialog.dismiss();

            }
        });
    }
}