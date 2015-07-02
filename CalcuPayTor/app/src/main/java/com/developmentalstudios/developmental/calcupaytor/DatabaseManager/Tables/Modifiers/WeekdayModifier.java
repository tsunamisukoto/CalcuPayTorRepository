package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.Modifiers;

import android.util.Log;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.DatabaseHelper;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.PayRecord;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Scott on 20/06/2015.
 */
public class WeekdayModifier extends Modifier {




    public WeekdayModifier(int _type, float _value, int _r, int Day)
    {
        super(_type,_value,_r, 0);
        this.ExtraValue=Day;
        Type=modTypeWeekday;
    }
    public String DayName()
    {
       return WeekdayModifier.getDayMethod((int) ExtraValue);
    }
    @Override
    public  String DisplayString(float extraValue)
    {
       return getDayMethod((int)extraValue);
    }
    public static String getDayMethod(int Day) {
        switch (Day)
        {
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            case 1:
                return "Sunday";
        }
        return "Invalid Day";
    }

    public float HoursAtPayRate(Date start, Date end)
    {
        Calendar calendar1=Calendar.getInstance();
        calendar1.setTime(start);
        Calendar calendar2=Calendar.getInstance();
        calendar2.setTime(end);
        if(calendar1.get(Calendar.DAY_OF_WEEK)==ExtraValue)
        {
            if(calendar1.get(Calendar.DAY_OF_WEEK)==calendar2.get(Calendar.DAY_OF_WEEK))
            {
                return PayRecord.NumberOfHoursWorked(start, end);
            }
            else
            {
                calendar2.set(Calendar.HOUR_OF_DAY,0);
                calendar2.set(Calendar.MINUTE,0);

                    return PayRecord.NumberOfHoursWorked(calendar1.getTime(), calendar2.getTime());
            }
        }
        else
        {
            if(calendar2.get(Calendar.DAY_OF_WEEK)==ExtraValue)
            {

                calendar1.set(Calendar.HOUR_OF_DAY,0);
                calendar1.set(Calendar.MINUTE,0);
                calendar1.set(Calendar.DATE,calendar2.get(Calendar.DATE));

                return PayRecord.NumberOfHoursWorked(calendar1.getTime(), calendar2.getTime());
            }
        }
return 0;

    }



}
