package jp.ddo.haselab.timerecoder.dataaccess;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RecodeDao {

    private static final String LOG_TAG = "RecodeDao";
    private static final String TABLE_NAME      = "recode";
    private static final String COLUMN_ID       = "rowid";
    private static final String COLUMN_DATE     = "date";
    private static final String COLUMN_EVENT_ID = "eventid";
    private static final String COLUMN_MEMO     = "memo";
    
    private SQLiteDatabase db;
    
    public RecodeDao(SQLiteDatabase db) {
	this.db = db;
    }
    
    public long insert(Recode rec) {
	Log.v(LOG_TAG,"start insert[" + rec + "]" );
	ContentValues values = new ContentValues();
	values.put(COLUMN_DATE,     rec.getDate());
	values.put(COLUMN_EVENT_ID, rec.getEventId());
	values.put(COLUMN_MEMO,     rec.getMemo());
	long res =  db.insert(TABLE_NAME, null, values);
	Log.v(LOG_TAG,"result key [" + res + "]" );
	return res;
    }

    public long count() {
	Log.v(LOG_TAG,"start count");
	Cursor c = db.rawQuery(
			       "select count(*) as co from " + TABLE_NAME,
			       null);
	// Log.v(LOG_TAG,"getColumnCount [" + c.getColumnCount() + "]" );
	// Log.v(LOG_TAG,"getCount [" + c.getCount() + "]" );
	// String a[] = c.getColumnNames();
	// for ( String x:a) {
	//    Log.v(LOG_TAG,"getColumnName [" + x + "]/index[" +
	//	  c.getColumnIndex(x) + "]");
	// }
	c.moveToFirst();
	long res = c.getLong(0);
	Log.v(LOG_TAG,"result count [" + res + "]" );
	c.close();
	return res;
    }
}
