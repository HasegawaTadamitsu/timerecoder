package jp.ddo.haselab.timerecoder.dataaccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import jp.ddo.haselab.timerecoder.util.RecodeDateTime;
import jp.ddo.haselab.timerecoder.util.MyLog;

public final class RecodeDao {

    private static final int MAX_COUNT = 1000;

    private static final String TABLE_NAME      = "recode";
    public static final String COLUMN_ID          = "_id";
    public static final String COLUMN_CATEGORY_ID = "categoryid";
    public static final String COLUMN_DATE_TIME   = "datetime";
    public static final String COLUMN_EVENT_ID    = "eventid";
    public static final String COLUMN_MEMO        = "memo";
    
    private SQLiteDatabase db;
    
    public RecodeDao(SQLiteDatabase db) {
	this.db = db;
    }
    
    public long insert(final Recode rec) {
	MyLog.getInstance().writeDatabase("insertVal["+ rec + "]");
	ContentValues values = new ContentValues();
	values.put(COLUMN_CATEGORY_ID, rec.getCategoryId());
	values.put(COLUMN_DATE_TIME,   rec.getDateTime().toMilliSecond());
	values.put(COLUMN_EVENT_ID,    rec.getEventId());
	values.put(COLUMN_MEMO,        rec.getMemo());
	long res =  db.insert(TABLE_NAME, null, values);
	MyLog.getInstance().writeDatabase("result _id["+ res + "]");
	rec.setRowId(res);
	return res;
    }

    public int deleteByCategoryId(final int argCategoryId){
	MyLog.getInstance().writeDatabase("delete categoryId[" 
					  + argCategoryId + "]");
	int res =  db.delete(TABLE_NAME, 
			     COLUMN_CATEGORY_ID + "=" + argCategoryId,
			     null);
	MyLog.getInstance().writeDatabase("result count["+ res + "]");
	return res;
    }

    public  static final String[] FIND_BY_CATEGORY_COLUMN= {
	COLUMN_ID,
	COLUMN_DATE_TIME,
	COLUMN_EVENT_ID,
	COLUMN_MEMO
    };

    public Cursor findByCategoryId(final int argCategoryId ) {
	MyLog.getInstance().readDatabase("findByCategoryId " +
					 "categoryId[" + argCategoryId + "]");
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
	MyLog.getInstance().readDatabase("findByCategoryId " +
					 "result[" + count + "]");
	return c;
    }
}
