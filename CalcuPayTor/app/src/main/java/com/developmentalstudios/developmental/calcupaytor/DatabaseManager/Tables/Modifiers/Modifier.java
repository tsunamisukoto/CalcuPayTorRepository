package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.Modifiers;

import android.os.Parcel;
import android.os.Parcelable;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.DatabaseHelper;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.PayRecord;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Scott on 20/06/2015.
 */
public class Modifier implements Parcelable{
    public static String TableName="Modifiers";
    public static   String colID="ID";
    public static   String colType="Type";
    public static   String colOperationType="OPType";
    public static String colRuleSetID="RuleSetID";
    public static String colValue="Value";
    public static String colExtraValue="ExtraValue";
    public static String colModifierString="ModifierString";
    public static String colSubModifierType="SubModifierType";


    public static final  int modTypeNone=0;
    public static final  int modTypeWeekday=1;
    public static final  int modTypeShiftOvertime =2;

    public static final int modTypeStartTime=3;

    public static final int modTypeSessionOvertime=4;
    public String ModifierString="";
    public int maxSubTypes=1;
    public ArrayList<String> listSubTypes;

    public static String CreateTable() {

        return  "CREATE TABLE "+ TableName+ "("+colID + " Integer PRIMARY KEY AUTOINCREMENT, "+colType+" Integer ,"+colOperationType+" Integer, "+colValue+" REAL, " + colRuleSetID+" INTEGER,"+ colSubModifierType+" INTEGER, "+ colExtraValue+" INTEGER,"+colModifierString+")";
    }

    public float HoursAtPayRate(Date start, Date end) {
        return 0;
    }

public int SubType=0;
public  int ID;
    public int RuleSetID;
    public int ModifierOperationType;
    public float Value;
    public float ExtraValue;
    public int Type =modTypeNone;
    public Modifier(int type, float Value, int _ruleset, int subType)
    {
        this.Value=Value;
        this.RuleSetID=_ruleset;
        this.ModifierOperationType =type;
        SubType=subType;
        listSubTypes=new ArrayList<>();
        listSubTypes.add("None");
        ID=-1;
    }
    public float CalculateAddition(PayRecord record,float payrate)
    {
        float amountofHours=TimeThatMeetsMeetsConditions(record);

        float addition=0;
        if(amountofHours>0)
        {

          addition= ModifyWage(payrate,amountofHours);
        }
        return addition;
    }
    protected float TimeThatMeetsMeetsConditions(PayRecord record)
    {
       return 0;
    }
    public float ModifyWage(float inputWage,float hoursworked)
    {
        switch (ModifierOperationType)
        {
            case 1:
                return hoursworked* inputWage*Value;
            case 2:
                return hoursworked* Value;
            case 3:
                return (inputWage*hoursworked)+Value;
        }
        return 0;
    }
    public static String TypeString(int ModType)
    {
        switch (ModType)
        {
            case 1:
               return "Multiplier";
            case 2:
                return "Replacer";
            case 3:
                return "Bonus Pay";

        }
        return "No Name";
    }

    public  String DisplayString(float extraValue)
    {
        return "Unspecified Modifier";
    }
    public static final Parcelable.Creator<Modifier> CREATOR = new Parcelable.Creator<Modifier>() {
        public Modifier createFromParcel(Parcel in) {
            Modifier m = new Modifier(in);
            switch (m.Type)
            {
                case modTypeWeekday:
                    return new WeekdayModifier(m.ModifierOperationType,m.Value,m.RuleSetID,(int)m.ExtraValue);
                case modTypeSessionOvertime:
                return new SessionOvertimeModifier(m.ModifierOperationType,m.Value,m.RuleSetID,m.ExtraValue);
                case modTypeShiftOvertime:
                    return new ShiftOvertimeModifier(m.ModifierOperationType,m.Value,m.RuleSetID,m.ExtraValue,m.SubType);
                case modTypeStartTime:
                    try {
                        return new ShiftStartTimeModifier(m.ModifierOperationType,m.Value,m.RuleSetID, DatabaseHelper.timeFormat.parse(m.ModifierString),m.SubType);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


            }
            return m;
        }

        public Modifier[] newArray(int size) {
            return new Modifier[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeInt(Type);
        dest.writeInt(ModifierOperationType);
        dest.writeInt(RuleSetID);
        dest.writeFloat(Value);
        dest.writeFloat(ExtraValue);
        dest.writeInt(SubType);
        dest.writeString(ModifierString);
    }
    protected Modifier(Parcel in)
    {
        ID=in.readInt();
        Type=in.readInt();
        ModifierOperationType=in.readInt();
        RuleSetID= in.readInt();
        Value=in.readFloat();
        ExtraValue=in.readFloat();
        SubType=in.readInt();
        ModifierString=in.readString();
    }


}
