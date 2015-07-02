package com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Views.PayCalculationEntities;

import com.developmentalstudios.developmental.calcupaytor.DatabaseManager.Tables.PayRecord;

/**
 * Created by Scott on 27/06/2015.
 */
public class PayPacket {
public PayRecord Record;
    public float Total=0;
    public float Additions =0;
    public PayPacket(PayRecord _r, float _total, float _additions)
    {
        Record=_r;
        Total=_total;
        Additions =_additions;
    }
}
