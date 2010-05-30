package jp.ddo.haselab.timerecoder.dataaccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import android.util.Log;
import android.content.ContentValues;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;

import jp.ddo.haselab.timerecoder.util.RecodeDateTime;


public class RecodeDao {

    private static final int MAX_COUNT = 1000;

    private static final String LOG_TAG = "RecodeDao";
    private static final String TABLE_NAME      = "recode";
    public static final String COLUMN_ID       = "_id";
    public static final String COLUMN_DATE_TIME = "datetime";
    public static final String COLUMN_EVENT_ID  = "eventid";
    public static final String COLUMN_MEMO      = "memo";
    
    private SQLiteDatabase db;
    
    public RecodeDao(SQLiteDatabase db) {
	this.db = db;
    }
    
    public long insert(Recode rec) {
	Log.v(LOG_TAG,"start insert[" + rec + "]" );
	ContentValues values = new ContentValues();
	values.put(COLUMN_DATE_TIME,rec.getDateTime().toMilliSecond());
	values.put(COLUMN_EVENT_ID, rec.getEventId());
	values.put(COLUMN_MEMO,     rec.getMemo());
	long res =  db.insert(TABLE_NAME, null, values);
	Log.v(LOG_TAG,"result key [" + res + "]" );
	rec.setRowId(res);
	return res;
    }

    public int deleteAll(){
	Log.v(LOG_TAG,"start deleteAll" );
	int res =  db.delete(TABLE_NAME, null, null);
	Log.v(LOG_TAG,"result  [" + res + "]" );
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

/*
    public Map<Long, Recode> findByCategory() {
	Log.v(LOG_TAG,"start findByCategory");

	String[] columns = { 
	    COLUMN_ID,
	    COLUMN_DATE_TIME,
	    COLUMN_EVENT_ID,
	    COLUMN_MEMO};

	Cursor c = db.query(TABLE_NAME,
			    columns,
			    null, // selection
			    null, // selectionArgs
			    null, // groupBy
			    null, // having
			    COLUMN_DATE_TIME, // order by
			    null  // limit
			    );

	long count = c.getCount();
	Log.v(LOG_TAG,"count [" + count + "]" );
	int initHashSize = (count<MAX_COUNT)? (int)count : MAX_COUNT;

	HashMap<Long, Recode> result =
	    new HashMap<Long, Recode>(  initHashSize );

	c.moveToFirst();
	for (int i = 0; i < count ; i++) {
	    long rowId = c.getLong(c.getColumnIndex(COLUMN_ID));
	    Recode data = new Recode(rowId,
				     new RecodeDateTime(
						c.getLong(c.getColumnIndex(
						COLUMN_DATE_TIME))),
				     c.getInt(c.getColumnIndex(
					       COLUMN_EVENT_ID)),
				     c.getString(c.getColumnIndex(
						  COLUMN_EVENT_ID))
				     );
	    result.put(rowId, data);
	    c.moveToNext();
	}
	c.close();
	return result;
    }
*/
    public  static final String[] FIND_BY_CATEGORY_COLUMN= {
	COLUMN_ID,
	COLUMN_DATE_TIME,
	COLUMN_EVENT_ID,
	COLUMN_MEMO
    };

    public Cursor findByCategory() {
	Log.v(LOG_TAG,"start findByCategory");

	Cursor c = db.query(TABLE_NAME,
			    FIND_BY_CATEGORY_COLUMN,
			    null, // selection
			    null, // selectionArgs
			    null, // groupBy
			    null, // having
			    COLUMN_DATE_TIME, // order by
			    null  // limit
			    );
	long count = c.getCount();
	Log.v(LOG_TAG,"count [" + count + "]" );

	return c;
    }
}
