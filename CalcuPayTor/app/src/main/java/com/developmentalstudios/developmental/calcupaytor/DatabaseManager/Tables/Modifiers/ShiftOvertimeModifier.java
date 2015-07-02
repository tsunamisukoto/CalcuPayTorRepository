package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.Modifiers;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.PayRecord;

import java.util.ArrayList;

/**
 * Created by Scott on 20/06/2015.
 */
public class ShiftOvertimeModifier extends Modifier {
    public static final  int subTypeNone=0;
    public static final  int subtypeHourly=1;
    public static final  int subtypePeriod=2;
    public ShiftOvertimeModifier(int type, float Value, int _r, float MaxHours,int _subtype ) {
        super(type, Value,_r, _subtype);
      ExtraValue=MaxHours;
        Type= modTypeShiftOvertime;
        SubType=_subtype;
        listSubTypes=new ArrayList<>();
        listSubTypes.add("None");
        listSubTypes.add("Addition Calculated Hourly");
        listSubTypes.add("Addition Calculated Each time reaches threshold");
    }
@Override
    protected float  TimeThatMeetsMeetsConditions(PayRecord record)
{

    switch (SubType)
    {
        case subtypeHourly:

            return (PayRecord.NumberOfHoursWorked(record.StartDate, record.EndDate)-ExtraValue);

        case subtypePeriod:

            return (PayRecord.NumberOfHoursWorked(record.StartDate, record.EndDate)/ExtraValue);

    }


    return 0;

    }

    public  String DisplayString(float extraValue)
    {
        return "Shift OT Modifier";
    }
}
