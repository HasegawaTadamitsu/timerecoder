package jp.ddo.haselab.timerecoder.dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "data";

    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_RECODER_TABLE_SQL =
	"create table recoder "
	+ "(rowid integer primary key autoincrement, "
	+ "date text not null, "
	+ "importance integer default 1)";

    private static final String DROP_RECODER_TABLE_SQL =
	"drop table if exists recoder";

    public DatabaseHelper(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
	public void onCreate(SQLiteDatabase db) {
	db.execSQL(CREATE_SCHEDULE_TABLE_SQL);
    }

    @Override
	public void onUpgrade(SQLiteDatabase db,
			      int oldVersion,
			      int newVersion) {
	db.execSQL(DROP_SCHEDULE_TABLE_SQL);
	onCreate(db);
    }
}
