package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Views.PayCalculationEntities;

/**
 * Created by Scott on 27/06/2015.
 */
public class PaySummary {
public  String Name;
    public float Pay;
    public float Additions;
    public float HoursWorked;
    public PaySummary(String JobName,float _hours, float _pay, float _add)
    {
        Name=JobName;
        Pay=_pay;
        Additions=_add;
        HoursWorked=_hours;
    }

}
