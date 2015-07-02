package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.Modifiers;

/**
 * Created by Scott on 20/06/2015.
 */
public class SessionOvertimeModifier extends Modifier {

    public SessionOvertimeModifier(int type, float Value,int _r, float Maxhours) {
        super(type, Value,_r, 0);
        ExtraValue=Maxhours;
        Type    =modTypeSessionOvertime;
    }
    @Override
    public  String DisplayString(float extraValue)
    {
        return "Pay Period OT Modifier";
    }
}
