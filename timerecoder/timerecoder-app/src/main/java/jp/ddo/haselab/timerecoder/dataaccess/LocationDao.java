
package jp.ddo.haselab.timerecoder.dataaccess;

import jp.ddo.haselab.timerecoder.util.MyLog;
import jp.ddo.haselab.timerecoder.util.RecodeDateTime;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * LocationDataacess.
 * 
 * @author hasegawa
 * 
 */
public final class LocationDao {

    static final String         CREATE_TABLE_SQL = "create table location ( " + "_id integer not null "
                                                         + "primary key "
                                                         + "REFERENCES "
                                                         + RecodeDao.TABLE_NAME
                                                         + " ( "
                                                         + RecodeDao.COLUMN_ID
                                                         + " ),"
                                                         + " latitude   real, "
                                                         + " longitude  real, "
                                                         + " altitude   real, "
                                                         + " accuracy   real, "
                                                         + " speed      real, "
                                                         + " bearing    real"
                                                         + " )";

    static final String         DROP_TABLE_SQL   = "drop table if exists location";

    private static final String TABLE_NAME       = "location";

    private static final String COLUMN_ID        = "_id";

    private static final String COLUMN_LATITUDE  = "latitude";

    private static final String COLUMN_LONGITUDE = "longitude";

    private static final String COLUMN_ALTITUDE  = "altitude";

    private static final String COLUMN_ACCURACY  = "accuracy";

    private static final String COLUMN_SPEED     = "speed";

    private static final String COLUMN_BEARING   = "bearing";

    private SQLiteDatabase      db;

    /**
     * constractor.
     * 
     * @param db1
     *            database.
     */
    public LocationDao(SQLiteDatabase db1) {

        this.db = db1;
    }

    /**
     * delete by id.
     * 
     * @param id
     *            id
     * @return deleted count.
     */
    public int deleteById(final long id) {

        MyLog.getInstance().writeDatabase("delete categoryId[" + id + "]");
        int res = this.db.delete(TABLE_NAME, COLUMN_ID + "=" + id, null);
        MyLog.getInstance().writeDatabase("result count[" + res + "]");
        return res;
    }

    /**
     * delete by ID in Location. call deleteById(int).
     * 
     * @param loc
     * @return delete count.
     */
    public int deleteById(final MyLocation loc) {

        return deleteById(loc.getId());
    }

    /**
     * delete by category ID in Location.
     * 
     * @param categoryId
     * @return delete count.
     */
    public void deleteByCategoryId(final int categoryId) {

        MyLog.getInstance()
                .writeDatabase("delete location [categoryId=" + categoryId
                        + "]");

        String where = " where " +COLUMN_ID + " in ( "
                + " select "
                + RecodeDao.COLUMN_ID
                + " from "
                + RecodeDao.TABLE_NAME
                + " where "
                + RecodeDao.COLUMN_CATEGORY_ID
                + " = "
                + categoryId
                + " ) ";
        String delete = "delete from " + TABLE_NAME  +" ";

        this.db.execSQL(delete + where);
        return;
    }

    /**
     * find by ID.
     * 
     * @param id
     * @return delete count.
     */
    public MyLocation findById(final long id) {

        MyLog.getInstance().readDatabase("id[" + id + "]");
        String[] columns = {
                COLUMN_ID,
                COLUMN_LATITUDE,
                COLUMN_LONGITUDE,
                COLUMN_ALTITUDE,
                COLUMN_ACCURACY,
                COLUMN_SPEED,
                COLUMN_BEARING
        };

        Cursor c = this.db.query(TABLE_NAME, columns, COLUMN_ID + " = "
                + id, // selection
                null, // selectionArgs
                null, // groupBy
                null, // having
                null, // order by
                null // limit
        );

        int count = c.getCount();
        MyLog.getInstance().readDatabase("result count[" + count + "]");

        c.moveToFirst();
        MyLocation loc = new MyLocation(c.getInt(c.getColumnIndex(COLUMN_ID)),
                c.getDouble(c.getColumnIndex(COLUMN_LATITUDE)),
                c.getDouble(c.getColumnIndex(COLUMN_LONGITUDE)),
                c.getDouble(c.getColumnIndex(COLUMN_ALTITUDE)),
                c.getDouble(c.getColumnIndex(COLUMN_ACCURACY)),
                c.getDouble(c.getColumnIndex(COLUMN_SPEED)),
                c.getDouble(c.getColumnIndex(COLUMN_BEARING)));
        c.close();
        return loc;
    }

    /**
     * insert location.
     * 
     * @param loc
     *            location
     */
    @SuppressWarnings("boxing")
    public void insert(final MyLocation loc) {

        MyLog.getInstance().writeDatabase("insertVal[" + loc + "]");

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, loc.getId());
        values.put(COLUMN_LATITUDE, loc.getLatitude());
        values.put(COLUMN_LONGITUDE, loc.getLongitude());
        values.put(COLUMN_ALTITUDE, loc.getAltitude());
        values.put(COLUMN_ACCURACY, loc.getAccuracy());
        values.put(COLUMN_SPEED, loc.getSpeed());
        values.put(COLUMN_BEARING, loc.getBearing());

        long res = this.db.insert(TABLE_NAME, null, values);
        MyLog.getInstance().writeDatabase("result insert key id[" + res
                + "]");
        return;
    }
}
