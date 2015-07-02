package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables;

/**
 * Created by Scott on 26/06/2015.
 */
public class PersonalConfig {


    /**
     * Created by Scott on 19/06/2015.
     */


        public static String TableName="PersonalConfigs";
        public static   String colID="ID";
        public static   String colWeeksAtATime="WeeksAtATime";
        public static String colWeekStart ="WeekStart";
        public static String colStartDay="StartDay";

        public  static String CreateTable()
        {
            return  "CREATE TABLE "+ TableName+ "("+colID + " Integer PRIMARY KEY AUTOINCREMENT, "+colWeeksAtATime+" INTEGER, "+colStartDay+" INTEGER, " +colWeekStart+" INTEGER)";
        }






        public int ID;
     public  int WeeksAtATime;
    public int StartDay;
    public int WeekStart;
        public PersonalConfig( int _w, int _s, int _ws)
        {


            WeeksAtATime=_w;
            StartDay    =_s;
            WeekStart=_ws;
        }




    }




