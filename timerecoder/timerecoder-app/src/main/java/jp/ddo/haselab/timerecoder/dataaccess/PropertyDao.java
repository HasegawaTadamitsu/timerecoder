package jp.ddo.haselab.timerecoder.dataaccess;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import jp.ddo.haselab.timerecoder.util.MyLog;

public final class PropertyDao {

    private static final String TABLE_NAME = "property";
    private static final String COLUMN_GROUP_ID = "groupid";
    private static final String COLUMN_KEY = "key";
    private static final String COLUMN_VALUE = "value";
    private static final String COLUMN_COMMENT = "comment";

    static final String CREATE_TABLE_SQL = "create table " + TABLE_NAME
            + "( groupid   integer not null,"
            + "  key       text    not null,"
            + "  value     text    not null, " + "  comment   text,  "
            + " primary key(groupid,key) )";

    static final String DROP_TABLE_SQL = "drop table if exists "
            + TABLE_NAME;

    private SQLiteDatabase db;

    public PropertyDao(SQLiteDatabase db) {
        this.db = db;
    }

    void insertDefaultData() {
        MyLog.getInstance().writeDatabase("start");

        ContentValues values = new ContentValues();
        values.put(COLUMN_GROUP_ID, 1000);
        values.put(COLUMN_KEY, "1");
        values.put(COLUMN_VALUE, "START");
        values.put(COLUMN_COMMENT, "[default]START button pushed eventID");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_GROUP_ID, 1000);
        values.put(COLUMN_KEY, "2");
        values.put(COLUMN_VALUE, "END");
        values.put(COLUMN_COMMENT, "[default]END button pushed eventID");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_GROUP_ID, 1000);
        values.put(COLUMN_KEY, "3");
        values.put(COLUMN_VALUE, "ETC");
        values.put(COLUMN_COMMENT, "[default]ETC button pushed eventID");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_GROUP_ID, 1001);
        values.put(COLUMN_KEY, "0");
        values.put(COLUMN_VALUE, "CATEGORY 0 name");
        values.put(COLUMN_COMMENT, "[default]CategoryID 0");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_GROUP_ID, 1001);
        values.put(COLUMN_KEY, "1");
        values.put(COLUMN_VALUE, "CATEGORY 1 name");
        values.put(COLUMN_COMMENT, "[default]CategoryID 1");
        db.insert(TABLE_NAME, null, values);
        return;
    }

    public Map<String, String> findByGroupId(final int argGroupId) {
        MyLog.getInstance().readDatabase("groupId[" + argGroupId + "]");
        String[] columns = { COLUMN_GROUP_ID, COLUMN_KEY, COLUMN_VALUE };

        Cursor c = db.query(TABLE_NAME, columns, COLUMN_GROUP_ID + " = "
                + argGroupId,
        // selection
                null, // selectionArgs
                null, // groupBy
                null, // having
                COLUMN_KEY, // order by
                null // limit
                );

        int count = c.getCount();
        MyLog.getInstance().readDatabase("result count[" + count + "]");

        Map<String, String> result = new HashMap<String, String>(count);
        c.moveToFirst();
        for (int i = 0; i < count; i++) {
            String key = c.getString(c.getColumnIndex(COLUMN_KEY));
            String val = c.getString(c.getColumnIndex(COLUMN_VALUE));

            result.put(key, val);
            c.moveToNext();
        }
        c.close();
        return result;
    }

    public List<Integer> findAllGroupId() {
        MyLog.getInstance().readDatabase("start");
        String[] columns = { COLUMN_GROUP_ID, };

        Cursor c = db.query(TABLE_NAME, columns, null, // selection
                null, // selectionArgs
                COLUMN_GROUP_ID, // groupBy
                null, // having
                COLUMN_GROUP_ID, // order by
                null // limit
                );

        int count = c.getCount();
        MyLog.getInstance().readDatabase("result count[" + count + "]");

        List<Integer> result = new ArrayList<Integer>(count);
        c.moveToFirst();
        for (int i = 0; i < count; i++) {
            int id = c.getInt(c.getColumnIndex(COLUMN_GROUP_ID));
            result.add(id);
            c.moveToNext();
        }
        c.close();
        return result;
    }

}
