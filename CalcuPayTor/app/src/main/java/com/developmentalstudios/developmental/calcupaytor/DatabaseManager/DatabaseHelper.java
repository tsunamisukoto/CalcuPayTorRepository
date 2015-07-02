package com.developmentalstudios.developmental.calcupaytor.DatabaseManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.Modifiers.Modifier;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.Modifiers.SessionOvertimeModifier;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.Modifiers.ShiftOvertimeModifier;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.Modifiers.ShiftStartTimeModifier;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.Modifiers.WeekdayModifier;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.PayRecord;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.PersonalConfig;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.RuleSet;
import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.TaxBracket;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Scott on 19/06/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String dbName="CalcuPayTerDatabase";


    public DatabaseHelper(Context context) {
        super(context, dbName, null,5);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS "+ RuleSet.TableName);
        db.execSQL("DROP TABLE IF EXISTS "+ PayRecord.TableName);
        db.execSQL("DROP TABLE IF EXISTS "+ WeekdayModifier.TableName);
        db.execSQL("DROP TABLE IF EXISTS "+ TaxBracket.TableName);
        db.execSQL("DROP TABLE IF EXISTS "+ PersonalConfig.TableName);

        db.execSQL(RuleSet.CreateTable());
        db.execSQL(PayRecord.CreateTable());
        db.execSQL(PayRecord.GenerateForeignKeyRuleSet());
        db.execSQL(WeekdayModifier.CreateTable());
        db.execSQL(TaxBracket.CreateTable());
        db.execSQL(PersonalConfig.CreateTable());
        InsertPersonalConfig(new PersonalConfig(2,3,1),db);
        InsertRuleSet(new RuleSet("Job", new Date(), 20), db);
        InsertTaxBracket(new TaxBracket(1,0,18200f),db);
        InsertTaxBracket(new TaxBracket(1,19,37000f),db);
        InsertTaxBracket(new TaxBracket(1,32.5f,80000f),db);
        InsertTaxBracket(new TaxBracket(1,37f,180000f),db);
        InsertTaxBracket(new TaxBracket(1,47f,500000f),db);

        InsertModifier(new WeekdayModifier(2, 29f, 1, 1), db);

        for(int i=0; i<5;i++)
        {
            InsertModifier(new WeekdayModifier(1, 1.0f, 1, i + 2), db);

        }
        InsertModifier(new WeekdayModifier(2, 25f, 1, 7), db);

        Calendar calendar= Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY,5);
        calendar.set(Calendar.MINUTE,30);

        InsertModifier(new ShiftOvertimeModifier(2, 0.5f, 1, 4,ShiftOvertimeModifier.subTypeNone), db);

        InsertModifier(new ShiftStartTimeModifier(2, 3, 1, calendar.getTime(), ShiftStartTimeModifier.subtypeNone), db);

        InsertModifier(new ShiftStartTimeModifier(2, 3, 1, calendar.getTime(), ShiftStartTimeModifier.subtypeNone), db);

        InsertModifier(new SessionOvertimeModifier(2, 3, 1, 30), db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static DateFormat timeFormat = new SimpleDateFormat("HH:mm");
    public static DateFormat dateFormat = new SimpleDateFormat("EEE dd/MM");
    public static DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   public ArrayList<PayRecord> getAllPayRecords(ArrayList<PayRecord> list)
    {
        list.clear();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur=db.rawQuery("SELECT "+PayRecord.colID+" , "+PayRecord.colStartDate+", "+PayRecord.colEndDate+", "+PayRecord.colRuleSetID+" from "+PayRecord.TableName+" ORDER BY " + PayRecord.colStartDate,new String [] {});

        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {

            try {
                PayRecord p = new PayRecord(cur.getInt(cur.getColumnIndex(PayRecord.colRuleSetID)),inputFormat.parse(cur.getString(cur.getColumnIndex(PayRecord.colStartDate))),inputFormat.parse(cur.getString(cur.getColumnIndex(PayRecord.colEndDate))));
                p.ID=cur.getInt(cur.getColumnIndex(PayRecord.colID));
                list.add(p);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cur.close();
        return list ;
    }

    public ArrayList<PayRecord> getAllPayRecordsForPeriod(ArrayList<PayRecord> list,Date start, Date end)
    {
        list.clear();
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cur=db.rawQuery("SELECT "+PayRecord.colID+" , "+PayRecord.colStartDate+", "+PayRecord.colEndDate+", "+PayRecord.colRuleSetID+" from "+PayRecord.TableName + " Where datetime(" + PayRecord.colStartDate + ") BETWEEN datetime(?) AND datetime(?)", new String[]{inputFormat.format(start) + "", inputFormat.format(end) + ""});

        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {


            try {
                PayRecord p = new PayRecord(cur.getInt(cur.getColumnIndex(PayRecord.colRuleSetID)),inputFormat.parse(cur.getString(cur.getColumnIndex(PayRecord.colStartDate))),inputFormat.parse(cur.getString(cur.getColumnIndex(PayRecord.colEndDate))));
                p.ID=cur.getInt(cur.getColumnIndex(PayRecord.colID));
                list.add(p);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cur.close();
        return list ;
    }


    public ArrayList<Modifier> getAllModifiersOfType(ArrayList<Modifier> list, int type, int ruleSetID, SQLiteDatabase db) {
        list.clear();
       if(db==null)
           db = this.getReadableDatabase();

        Cursor cur = db.rawQuery("SELECT " + Modifier.colID + " , " + Modifier.colOperationType + ", " + Modifier.colRuleSetID + ", " + Modifier.colType + ", " + Modifier.colModifierString + "," + Modifier.colValue + ", " + Modifier.colExtraValue +","+Modifier.colSubModifierType+ " from " + Modifier.TableName + " Where " + Modifier.colRuleSetID + "=? AND " + Modifier.colType + "=?", new String[]{ruleSetID + "", type + ""});
        try {
            for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {

                switch (type) {

                    case Modifier.modTypeWeekday:
                        WeekdayModifier m = new WeekdayModifier(cur.getInt(cur.getColumnIndex(Modifier.colOperationType)), cur.getFloat(cur.getColumnIndex(Modifier.colValue)), cur.getInt(cur.getColumnIndex(Modifier.colRuleSetID)), cur.getInt(cur.getColumnIndex(Modifier.colExtraValue)));
                        m.ID = cur.getInt(cur.getColumnIndex(Modifier.colID));
                        list.add(m);

                        break;

                    case Modifier.modTypeShiftOvertime:
                        ShiftOvertimeModifier ff = new ShiftOvertimeModifier(cur.getInt(cur.getColumnIndex(Modifier.colOperationType)), cur.getFloat(cur.getColumnIndex(Modifier.colValue)), cur.getInt(cur.getColumnIndex(Modifier.colRuleSetID)), cur.getInt(cur.getColumnIndex(Modifier.colExtraValue)),cur.getInt(cur.getColumnIndex(Modifier.colSubModifierType)));
                        ff.ID = cur.getInt(cur.getColumnIndex(Modifier.colID));
                        list.add(ff);

                        break;
                    case Modifier.modTypeSessionOvertime:
                        SessionOvertimeModifier ww = new SessionOvertimeModifier(cur.getInt(cur.getColumnIndex(Modifier.colOperationType)), cur.getFloat(cur.getColumnIndex(Modifier.colValue)), cur.getInt(cur.getColumnIndex(Modifier.colRuleSetID)), cur.getInt(cur.getColumnIndex(Modifier.colExtraValue)));
                        ww.ID = cur.getInt(cur.getColumnIndex(Modifier.colID));
                        list.add(ww);

                        break;
                    case Modifier.modTypeStartTime:
                        ShiftStartTimeModifier ssa = new ShiftStartTimeModifier(cur.getInt(cur.getColumnIndex(Modifier.colOperationType)), cur.getFloat(cur.getColumnIndex(Modifier.colValue)), cur.getInt(cur.getColumnIndex(Modifier.colRuleSetID)), timeFormat.parse(cur.getString(cur.getColumnIndex(Modifier.colModifierString))), cur.getInt(cur.getColumnIndex(Modifier.colSubModifierType)));
                        ssa.ID = cur.getInt(cur.getColumnIndex(Modifier.colID));
                        list.add(ssa);

                        break;


                }

            }
        } catch (Exception e)
        {

        }

        cur.close();
        return list ;
    }
    public long InsertPayRecord(PayRecord p,SQLiteDatabase db)
    {
if(db==null )
         db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(p.colRuleSetID, p.RuleSetID);
        cv.put(p.colStartDate,inputFormat.format( p.StartDate));
        cv.put(p.colEndDate,inputFormat.format( p.EndDate));
      return db.insert(PayRecord.TableName,PayRecord.colID,cv);

    }
    public long DeletePayRecord(PayRecord p,SQLiteDatabase db)
    {
        if(db==null )
            db=this.getWritableDatabase();

        return db.delete(PayRecord.TableName, PayRecord.colID + "=?", new String[]{String.valueOf(p.ID)});

    }
    public long InsertTaxBracket(TaxBracket p,SQLiteDatabase db)
    {
        if(db==null )
            db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(p.colBracketMax, p.BracketMax);
        cv.put(p.colRuleSetID, p.RuleSet);
        cv.put(p.colPercentTax, p.PercentTax);
        if(p.ID==-1)
        return db.insert(TaxBracket.TableName, TaxBracket.colID, cv);
        else
            return    db.update(TaxBracket.TableName,cv,  TaxBracket.colID+"=?",new String[]{String.valueOf(p.ID)});


    }
    public long EditTaxBracket(TaxBracket p,SQLiteDatabase db)
    {
        if(db==null )
            db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(p.colBracketMax, p.BracketMax);
        cv.put(p.colRuleSetID, p.RuleSet);
        cv.put(p.colPercentTax, p.PercentTax);

     return    db.update(TaxBracket.TableName, cv, TaxBracket.colID+"=?",new String[]{String.valueOf(p.ID)});


    }
    public ArrayList<TaxBracket>  getTaxBracketsForRuleSet(ArrayList<TaxBracket> brackets, int id)
    {brackets.clear();


        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur=db.rawQuery("SELECT "+TaxBracket.colID+", "+ TaxBracket.colRuleSetID+", " + TaxBracket.colPercentTax+", " + TaxBracket.colBracketMax+"  from "+TaxBracket.TableName +" Where "+ TaxBracket.colRuleSetID+ "=?  ORDER BY "+TaxBracket.colBracketMax ,new String [] {id+""});
        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {


            TaxBracket t = new TaxBracket(cur.getInt(cur.getColumnIndex(TaxBracket.colRuleSetID)), cur.getFloat(cur.getColumnIndex(TaxBracket.colPercentTax)), cur.getFloat(cur.getColumnIndex(TaxBracket.colBracketMax)));
            t.ID= cur.getInt(cur.getColumnIndex(TaxBracket.colID));
            brackets.add(t);
        }
      return brackets;
    }

    public long InsertPersonalConfig(PersonalConfig p,SQLiteDatabase db)
    {
        if(db==null )
            db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(p.colStartDay, p.StartDay);
        cv.put(p.colWeeksAtATime,inputFormat.format( p.WeeksAtATime));
        cv.put(p.colWeekStart,inputFormat.format( p.WeekStart));
        return db.insert(PersonalConfig.TableName,PersonalConfig.colID,cv);

    }
    public PersonalConfig  getPersonalConfig()
    {

        SQLiteDatabase db=this.getReadableDatabase();
    Cursor cur = db.rawQuery("SELECT "+ PersonalConfig.colID+", "+ PersonalConfig.colWeekStart+", " +PersonalConfig.colStartDay+", "+ PersonalConfig.colWeeksAtATime+" FROM "+ PersonalConfig.TableName,new String[]{});
        if(cur.moveToFirst())
        {
            PersonalConfig p  = new PersonalConfig(cur.getInt(cur.getColumnIndex(PersonalConfig.colWeeksAtATime)),cur.getInt(cur.getColumnIndex(PersonalConfig.colStartDay)),cur.getInt(cur.getColumnIndex(PersonalConfig.colWeekStart)));
            p.ID=cur.getInt(cur.getColumnIndex(PersonalConfig.colID));
            return p;
        }
        return null;
    }

    public void EditPayRecord(PayRecord p,SQLiteDatabase db)
    {
        if(db==null )
            db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(p.colRuleSetID, p.RuleSetID);
        cv.put(p.colStartDate,inputFormat.format( p.StartDate));
        cv.put(p.colEndDate,inputFormat.format( p.EndDate));
        db.update(PayRecord.TableName, cv, PayRecord.colID+"=?",new String[]{String.valueOf(p.ID)});
    }
    public int   EditRuleSet(RuleSet p,SQLiteDatabase db)
    {
        if(db==null )
            db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(p.colDateCreated,inputFormat.format( p.DateCreated));
        cv.put(p.colName,p.Name);
        cv.put(p.colBasePayRate,p.BasePayRate);
         db.update(RuleSet.TableName, cv, RuleSet.colID+"=?",new String[]{String.valueOf(p.ID)});
        return p.ID;

    }

    public float  getPayRateForRuleSet(int id)
    {

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur=db.rawQuery("SELECT "+RuleSet.colBasePayRate+" from "+RuleSet.TableName +" Where "+ RuleSet.colID+ "=? ",new String [] {id+""});
        if(cur.moveToFirst())
        {
            return cur.getFloat(cur.getColumnIndex(RuleSet.colBasePayRate));
        }
        return 0;
    }

    public String  getNameForRuleSet(int id)
    {

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur=db.rawQuery("SELECT "+RuleSet.colName+" from "+RuleSet.TableName +" Where "+ RuleSet.colID+ "=? ",new String [] {id+""});
        if(cur.moveToFirst())
        {
            return cur.getString(cur.getColumnIndex(RuleSet.colName));
        }
        return "No Name";
    }
    public ArrayList<RuleSet> getAllRuleSets(ArrayList<RuleSet> list)
    {
        list.clear();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur=db.rawQuery("SELECT "+RuleSet.colID+" , "+RuleSet.colName+", "+RuleSet.colDateCreated+", "+RuleSet.colBasePayRate+" from "+RuleSet.TableName,new String [] {});


        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {

            try {
                RuleSet p = new RuleSet(cur.getString(cur.getColumnIndex(RuleSet.colName)), inputFormat.parse(cur.getString(cur.getColumnIndex(RuleSet.colDateCreated))),cur.getFloat(cur.getColumnIndex(RuleSet.colBasePayRate)));
                p.ID=cur.getInt(cur.getColumnIndex(RuleSet.colID));
                list.add(p);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cur.close();
        return list ;
    }
    public long InsertRuleSet(RuleSet p,SQLiteDatabase db)
    {

        if(db==null )
         db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(p.colDateCreated,inputFormat.format(new Date()));
        cv.put(p.colName,p.Name);
        cv.put(p.colBasePayRate,p.BasePayRate);
       return db.insert(RuleSet.TableName,RuleSet.colID,cv);
    }
    
    public long InsertModifier(Modifier p,SQLiteDatabase db)
    {

        if(db==null )
         db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(p.colRuleSetID,p.RuleSetID);
        cv.put(p.colOperationType,p.ModifierOperationType);
        cv.put(p.colType,p.Type);
        cv.put(p.colValue,p.Value);
        cv.put(p.colExtraValue,p.ExtraValue);
        cv.put(p.colModifierString,p.ModifierString);
        cv.put(p.colSubModifierType,p.SubType);

            ArrayList<Modifier>m= getAllModifiersOfType(new ArrayList<Modifier>(),p.Type,p.RuleSetID, db);
        if(p.Type== Modifier.modTypeWeekday)
        {
            boolean found = false;
            for(Modifier mod:m)
            {
                if(mod.ExtraValue==p.ExtraValue)
                    found=true;
            }
            if(found)
            {
             return    db.update(Modifier.TableName,cv, Modifier.colType+"=? AND "+ Modifier.colRuleSetID+"=? AND "+ Modifier.colExtraValue+"=?",new String[]{String.valueOf(p.Type), String.valueOf(p.RuleSetID), String.valueOf(p.ExtraValue)});
            }

        }
        else
        {

            if(m.size()>0)
            {
                String mstring = Modifier.colID+"=?";
                if(p.ID!=-1)
                return    db.update(Modifier.TableName,cv, mstring,new String[]{String.valueOf(p.ID)});

            }


        }
        return db.insert(Modifier.TableName,Modifier.colID,cv);


    }



}
