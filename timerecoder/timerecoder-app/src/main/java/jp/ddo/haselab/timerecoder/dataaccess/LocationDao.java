package jp.ddo.haselab.timerecoder.dataaccess;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import jp.ddo.haselab.timerecoder.util.MyLog;

public final class LocationDao {

    static final String CREATE_TABLE_SQL = "create table location "
            + "( _id integer not null primary key," + " latitude   real, "
            + " longitude  real, " + " altitude   real, "
            + " accuracy   real, " + " speed      real, "
            + " bearing    real)";

    static final String DROP_TABLE_SQL = "drop table if exists location";

    private static final String TABLE_NAME = "locale";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_ALTITUDE = "altitude";
    private static final String COLUMN_ACCURACY = "accuracy";
    private static final String COLUMN_SPEED = "speed";
    private static final String COLUMN_BEARING = "bearing";

    private SQLiteDatabase db;

    public LocationDao(SQLiteDatabase db) {
        this.db = db;
    }

    public long insert(final MyLocation loc) {

        MyLog.getInstance().writeDatabase("insertVal[" + loc + "]");

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, loc.getId());
        values.put(COLUMN_LATITUDE, loc.getLatitude());
        values.put(COLUMN_LONGITUDE, loc.getLongitude());
        values.put(COLUMN_ALTITUDE, loc.getAltitude());
        values.put(COLUMN_ACCURACY, loc.getAccuracy());
        values.put(COLUMN_SPEED, loc.getSpeed());
        values.put(COLUMN_BEARING, loc.getBearing());

        long res = db.insert(TABLE_NAME, null, values);
        MyLog.getInstance().writeDatabase(
                "result insert key id[" + res + "]");
        return res;
    }

    public int deleteById(final MyLocation loc) {
        return deleteById(loc.getId());
    }

    public int deleteById(final int id) {
        MyLog.getInstance().writeDatabase("delete categoryId[" + id + "]");
        int res = db.delete(TABLE_NAME, COLUMN_ID + "=" + id, null);
        MyLog.getInstance().writeDatabase("result count[" + res + "]");
        return res;
    }

    public MyLocation findById(final int id) {
        MyLog.getInstance().readDatabase("categoryId[" + id + "]");
        String[] columns = { COLUMN_ID, COLUMN_LATITUDE, COLUMN_LONGITUDE,
                COLUMN_ALTITUDE, COLUMN_ACCURACY, COLUMN_SPEED,
                COLUMN_BEARING };

        Cursor c = db.query(TABLE_NAME, columns, COLUMN_ID + " = " + id,
        // selection
                null, // selectionArgs
                null, // groupBy
                null, // having
                null, // order by
                null // limit
                );

        int count = c.getCount();
        MyLog.getInstance().readDatabase("result count[" + count + "]");

        c.moveToFirst();
        MyLocation loc = new MyLocation(c.getInt(c
                .getColumnIndex(COLUMN_ID)), c.getDouble(c
                .getColumnIndex(COLUMN_LATITUDE)), c.getDouble(c
                .getColumnIndex(COLUMN_LONGITUDE)), c.getDouble(c
                .getColumnIndex(COLUMN_ALTITUDE)), c.getDouble(c
                .getColumnIndex(COLUMN_ACCURACY)), c.getDouble(c
                .getColumnIndex(COLUMN_SPEED)), c.getDouble(c
                .getColumnIndex(COLUMN_BEARING)));
        c.close();
        return loc;
    }
}
