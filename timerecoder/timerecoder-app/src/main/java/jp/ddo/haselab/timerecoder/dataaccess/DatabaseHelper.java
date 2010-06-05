package jp.ddo.haselab.timerecoder.dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import jp.ddo.haselab.timerecoder.util.MyLog;


public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "data";

    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_RECODE_TABLE_SQL =
	"create table recode "
	+ "( _id integer not null primary key,"
	+ " categoryid integer not null ,"
	+ " datetime   integer not null, "
	+ " eventid    integer not null, "
	+ " memo       text    not null)";

    private static final String CREATE_PROPERTY_TABLE_SQL =
	"create table property "
	+ "( groupid   integer not null,"
	+ "  key       integer not null,"
	+ "  value     text    not null, "
	+ "  comment   text,  "
	+ " primary key(groupid,key) )";

    private static final String INSERT_DEFAULT_PROPERTY_TABLE_SQL =
	"insert into property " 
	+ "(groupId,key,value,comment)" 
	+ "values (1000,1,'default','default value')";
	
    private static final String DROP_RECODE_TABLE_SQL =
	"drop table if exists recode";
    private static final String DROP_PROPERTY_TABLE_SQL =
	"drop table if exists property";

    public DatabaseHelper(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
	public void onCreate(SQLiteDatabase db) {
	MyLog.getInstance().verbose("start create table");
	db.execSQL(CREATE_RECODE_TABLE_SQL);
	db.execSQL(CREATE_PROPERTY_TABLE_SQL);
	MyLog.getInstance().verbose("start create default val");
	db.execSQL(INSERT_DEFAULT_PROPERTY_TABLE_SQL);
    }

    @Override
	public void onUpgrade(SQLiteDatabase db,
			      int oldVersion,
			      int newVersion) {
	MyLog.getInstance().verbose("start drop tables");
	db.execSQL(DROP_RECODE_TABLE_SQL);
	db.execSQL(DROP_PROPERTY_TABLE_SQL);
	onCreate(db);
    }
}
