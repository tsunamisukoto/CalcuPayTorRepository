package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.DatabaseHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Scott on 19/06/2015.
 */
public class RuleSet implements Parcelable,Comparable<RuleSet> {

    public static String TableName="RuleSet";
    public static   String colID="ID";
    public static   String colName="Name";

    public static   String colDateCreated="DateCreated";
    public static String colBasePayRate="BasePayRate";
public  static String CreateTable()
{
    return  "CREATE TABLE "+ TableName+ "("+colID + " Integer PRIMARY KEY AUTOINCREMENT, "+colName+" TEXT, "+ colDateCreated+" TEXT, "+colBasePayRate+" REAL)";
}





    public String Name;
    public Date DateCreated;
    public float BasePayRate;
    public int ID=-1;

public RuleSet(String name,Date _datecreated, float basePayRate)
{

    Name=name;
    DateCreated=_datecreated;
    BasePayRate=basePayRate;

}
    public float OverTimeHoursRunningTotal=0;
    public float OverTimePayRunningTotal=0;
    public float HoursRunningTotal=0;
    public float PayRunningTotal=0;


    public static final Parcelable.Creator<RuleSet> CREATOR = new Parcelable.Creator<RuleSet>() {
        public RuleSet createFromParcel(Parcel in) {
            return new RuleSet(in);
        }

        public RuleSet[] newArray(int size) {
            return new RuleSet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(DatabaseHelper.inputFormat.format(DateCreated));
        dest.writeFloat(BasePayRate);
        dest.writeString(Name);
    }
    private RuleSet(Parcel in)
    {
        ID=in.readInt();

        try {
            DateCreated=DatabaseHelper.inputFormat.parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BasePayRate=in.readFloat();
        Name=in.readString();
    }

    @Override
    public String toString() {
        return Name;
    }

    public float CalculateTax(DatabaseHelper dbHelper, long daysInPeriod) {
        ArrayList<TaxBracket> taxBrackets= new ArrayList<>();
     dbHelper.getTaxBracketsForRuleSet(taxBrackets,ID);
        float totalPay= PayRunningTotal+OverTimePayRunningTotal;

        float Previous=0;
        float TotalTax=0;
        float remainingMoneyToBeTaxed=totalPay/daysInPeriod*365.25f;
        for(TaxBracket tax : taxBrackets)
        {


            float difference = (tax.BracketMax-Previous);


            boolean last = remainingMoneyToBeTaxed-difference<0;

            if(last)
            {
                difference=remainingMoneyToBeTaxed;
            }
            float AmountAffected=difference ;

            float subTotalTax=AmountAffected*tax.PercentTax/100;
            remainingMoneyToBeTaxed-=difference;

            TotalTax+=subTotalTax;


            Previous=tax.BracketMax;
            if(last)
            {
               break;
            }
        }


        return  TotalTax*daysInPeriod/365.25f;
    }

    @Override
    public int compareTo(RuleSet another) {
        return Float.compare(another.PayRunningTotal, PayRunningTotal);

    }
}
