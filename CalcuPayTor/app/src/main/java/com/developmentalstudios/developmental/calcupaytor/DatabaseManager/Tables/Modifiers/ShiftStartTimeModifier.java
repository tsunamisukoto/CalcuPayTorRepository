package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.Modifiers;

import android.util.Log;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.DatabaseHelper;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.PayRecord;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Scott on 20/06/2015.
 */
public class ShiftStartTimeModifier extends Modifier {
    public static final int subtypeNone=0;
    public static final int subtypeAfter=1;
    public static final int subtypeBefore=2;

    public ShiftStartTimeModifier(int type, float Value, int _r, Date s, int _subType) {
        super(type, Value,_r, _subType);
      ModifierString=DatabaseHelper.timeFormat.format(s);
        Type=modTypeStartTime;
        listSubTypes=new ArrayList<>();
        listSubTypes.add("None");
        listSubTypes.add("AfterTime");
        listSubTypes.add("BeforeTime");
    }
@Override
    protected float  TimeThatMeetsMeetsConditions(PayRecord record)
{
    switch (SubType)
    {
        case subtypeAfter:
            return After(record);
        case subtypeBefore:
            return Before(record);
    }
    return 0;




    }

    private float Before(PayRecord record) {


        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        try {
            calendar1.setTime(DatabaseHelper.timeFormat.parse(DatabaseHelper.timeFormat.format(record.StartDate)));

            calendar2.setTime(DatabaseHelper.timeFormat.parse(ModifierString));

            if(calendar1.before(calendar2))
            {
                return PayRecord.NumberOfHoursWorked(record.StartDate, record.EndDate);
            }
        } catch (ParseException e) {

        }

        return 0;



    }

    private float After(PayRecord record)
    {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        try {
            calendar1.setTime(DatabaseHelper.timeFormat.parse(DatabaseHelper.timeFormat.format(record.StartDate)));


            calendar2.setTime(DatabaseHelper.timeFormat.parse(ModifierString));

            if(calendar1.after(calendar2))
            {
                return PayRecord.NumberOfHoursWorked(record.StartDate, record.EndDate);
            }
        } catch (ParseException e) {

        }
return 0;
    }
    @Override
    public  String DisplayString(float extraValue)
    {
        return "Shift Start Time Modifier"+(SubType==subtypeAfter?"(After)":"(Before)");
    }
}
