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
import java.util.Date;

/**
 * Created by Scott on 20/06/2015.
 */
public class CalculationDatePickerDialogListener implements View.OnClickListener {

    public TextView Resource;
    Context context;
   Date record;
    public CalculationDatePickerDialogListener(Context c, TextView _r, Date _rec, boolean start) {
        Resource = _r;
        context = c;
    record=_rec;
    }

    @Override
    public void onClick(View v) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.mydatepicker);

        dialog.show();

        final DatePicker tp = (DatePicker) dialog.findViewById(R.id.datePicker1);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(record);
        tp.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        Button btnclose = (Button) dialog.findViewById(R.id.btn_close);
        btnclose.setBackgroundResource(R.drawable.buttonshape2);

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                calendar.set(tp.getYear(),tp.getMonth(),tp.getDayOfMonth());
                (Resource).setText(DatabaseHelper.dateFormat.format(calendar.getTime()));


                    calendar.setTime(record);
                    calendar.set(Calendar.DAY_OF_MONTH, tp.getDayOfMonth());
                    calendar.set(Calendar.MONTH,tp.getMonth());
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    record= calendar.getTime();

                dialog.dismiss();

            }
        });
    }
}