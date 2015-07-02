package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Scott on 19/06/2015.
 */
public class TaxBracket implements Parcelable {

    public static String TableName="TaxBrackets";
    public static   String colID="ID";
    public static   String colBracketMax="BracketMax";

    public static   String colPercentTax="PercentTax";
    public static String colRuleSetID="RuleSetID";

public  static String CreateTable()
{
    return  "CREATE TABLE "+ TableName+ "("+colID + " Integer PRIMARY KEY AUTOINCREMENT, "+colBracketMax+" REAL, "+ colPercentTax+" REAL, "+colRuleSetID+" INTEGER)";
}






    public int ID;
    public int RuleSet;
    public float PercentTax;
  public  float BracketMax;
public TaxBracket(int _r, float _percent, float bracketMax)
{

    RuleSet=_r;
    PercentTax=_percent;
     BracketMax=bracketMax;
    ID=-1;
}



    public static final Creator<TaxBracket> CREATOR = new Creator<TaxBracket>() {
        public TaxBracket createFromParcel(Parcel in) {
            return new TaxBracket(in);
        }

        public TaxBracket[] newArray(int size) {
            return new TaxBracket[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeInt(RuleSet);
        dest.writeFloat(BracketMax);
        dest.writeFloat(PercentTax);
    }
    private TaxBracket(Parcel in)
    {
        ID=in.readInt();

        RuleSet=in.readInt();
        BracketMax=in.readFloat();
        PercentTax=in.readFloat();
    }

}
