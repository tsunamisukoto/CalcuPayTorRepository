package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Listeners;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.DatabaseHelper;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.PayRecord;
import com.developmentalstudios.developmental.calcupaytor.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Scott on 20/06/2015.
 */
public class ModifierTimePickerDialogListener implements View.OnClickListener {

    public TextView Resource;
    Context context;
 Date record;

    public ModifierTimePickerDialogListener(Context c, TextView _r, Date _rec)
    {
        Resource=_r;
        context=c;
      record=_rec;

    }
    @Override
    public void onClick(View v) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.mytimepicker);

        dialog.show();

        final TimePicker tp = (TimePicker) dialog.findViewById(R.id.timePicker1);
        Calendar calendar=Calendar.getInstance();

            calendar.setTime(record);

        tp.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        tp.setCurrentMinute(calendar.get(Calendar.MINUTE));

        Button btnclose = (Button) dialog.findViewById(R.id.btn_close);
        btnclose.setBackgroundResource(R.drawable.buttonshape2);

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();

                    calendar.setTime(record);
                    calendar.set(Calendar.HOUR_OF_DAY, tp.getCurrentHour());
                    calendar.set(Calendar.MINUTE, tp.getCurrentMinute());
                    record= calendar.getTime();

                Log.e("CalcuPaytor", "Sweet Sweet VICTORY!" + DatabaseHelper.timeFormat.format(record));
                (Resource).setText(DatabaseHelper.timeFormat.format(calendar.getTime()));
                dialog.dismiss();
            }
        });

    }
}

