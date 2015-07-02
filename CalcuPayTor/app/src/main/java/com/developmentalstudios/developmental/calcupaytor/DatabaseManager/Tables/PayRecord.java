package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.DatabaseHelper;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Scott on 19/06/2015.
 */
public class PayRecord implements Parcelable {

    public static String TableName="PayRecord";

    public static   String colID="ID";
    public static   String colStartDate="StartDate";
    public static   String colEndDate="EndDate";
    public static String colRuleSetID="RuleSet";

    public static  String RulesetForeignKeyName= "fk_Ruleset";

public  static String CreateTable()
{
    return  "CREATE TABLE "+ TableName+ "("+colID + " Integer PRIMARY KEY AUTOINCREMENT, "+colStartDate+" TEXT ,"+colEndDate+" TEXT, "+colRuleSetID+" Integer)";
}
    public static String GenerateForeignKeyRuleSet()
    {
        return "CREATE TRIGGER "+ RulesetForeignKeyName+
                " BEFORE INSERT ON "+ TableName+" FOR EACH ROW BEGIN "+
                "SELECT CASE WHEN ((SELECT "+ RuleSet.colID + " FROM " + RuleSet.TableName+" WHERE "+ RuleSet.colID+"=new."+colRuleSetID+") IS NULL)"+
                "THEN RAISE (ABORT, 'Foreign Key Violation " +RulesetForeignKeyName+"') END;"+
                "END;";
    }
    public static float NumberOfHoursWorked(Date _start, Date _end)
    {

        long result1 = ((_end.getTime()/60000) - (_start.getTime()/60000))/15;

        float result=result1/4f;
        return  result;
    }
    public Date StartDate;
    public Date EndDate;
    public int ID;
    public int RuleSetID;
    public PayRecord(int _rules, Date _start, Date _end)
    {
        RuleSetID=_rules;
        StartDate=_start;
        EndDate=_end;
    }




    public static final Parcelable.Creator<PayRecord> CREATOR = new Parcelable.Creator<PayRecord>() {
        public PayRecord createFromParcel(Parcel in) {
            return new PayRecord(in);
        }

        public PayRecord[] newArray(int size) {
            return new PayRecord[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeInt(RuleSetID);
        dest.writeString(DatabaseHelper.inputFormat.format(StartDate));
        dest.writeString(DatabaseHelper.inputFormat.format(EndDate));
    }
    private PayRecord(Parcel in)
    {
        ID=in.readInt();
        RuleSetID=in.readInt();
        try {
            StartDate=DatabaseHelper.inputFormat.parse(in.readString());
            EndDate=DatabaseHelper.inputFormat.parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
