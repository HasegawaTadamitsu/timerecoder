package jp.ddo.haselab.timerecoder.dataaccess;

import android.content.Context;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "data";

    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_RECODE_TABLE_SQL =
	"create table recode "
	+ "(rowid  integer primary key autoincrement, "
	+ " date    text    not null, "
	+ " eventid integer not null, "
	+ " memo    text    not null)";

    private static final String DROP_RECODE_TABLE_SQL =
	"drop table if exists recode";

    public DatabaseHelper(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
	public void onCreate(SQLiteDatabase db) {
	Log.v(LOG_TAG,"start onCreate.create table");
	db.execSQL(CREATE_RECODE_TABLE_SQL);
    }

    @Override
	public void onUpgrade(SQLiteDatabase db,
			      int oldVersion,
			      int newVersion) {
	Log.v(LOG_TAG,"start onUpgrade.drop table");
	db.execSQL(DROP_RECODE_TABLE_SQL);
	onCreate(db);
    }
}
