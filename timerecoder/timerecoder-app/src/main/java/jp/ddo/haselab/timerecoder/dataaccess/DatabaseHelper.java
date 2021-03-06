
package jp.ddo.haselab.timerecoder.dataaccess;

import jp.ddo.haselab.timerecoder.util.MyLog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Data helper.
 * 
 * @author hasegawa
 * 
 */
public final class DatabaseHelper extends
        SQLiteOpenHelper {

    private static final String DATABASE_NAME    = "data";

    private static final int    DATABASE_VERSION = 1;

    /**
     * constracer.called SQLLiteOpenHelper. super this.
     * 
     * @param context
     */
    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private void dropTables(SQLiteDatabase db) {

        MyLog.getInstance().writeDatabase("drop tables");
        db.execSQL(RecodeDao.DROP_TABLE_SQL);
        db.execSQL(PropertyDao.DROP_TABLE_SQL);
        db.execSQL(LocationDao.DROP_TABLE_SQL);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        MyLog.getInstance().writeDatabase("start init database");

        dropTables(db);

        MyLog.getInstance().writeDatabase("create tables");
        db.execSQL(RecodeDao.CREATE_TABLE_SQL);
        db.execSQL(LocationDao.CREATE_TABLE_SQL);
        db.execSQL(PropertyDao.CREATE_TABLE_SQL);

        MyLog.getInstance().writeDatabase("insert default");

        PropertyDao dao = new PropertyDao(db);

        db.beginTransaction();
        MyLog.getInstance().startTransaction("insert init value");
        try {
            dao.insertDefaultData();
            db.setTransactionSuccessful();
            MyLog.getInstance().endTransaction("success.");
        } finally {
            db.endTransaction();
        }
        MyLog.getInstance().writeDatabase("end init database");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        onCreate(db);
    }
}
