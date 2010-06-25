
package jp.ddo.haselab.timerecoder.dataaccess;

import java.util.ArrayList;
import java.util.List;

import jp.ddo.haselab.timerecoder.util.MyLog;
import jp.ddo.haselab.timerecoder.util.RecodeDateTime;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * recode dao.
 * 
 * @author hasegawa
 * 
 */
public final class RecodeDao {

    static final String         CREATE_TABLE_SQL   = "create table recode " + "( _id integer not null primary key,"
                                                           + " categoryid integer not null ,"
                                                           + " datetime   integer not null, "
                                                           + " eventid    integer not null, "
                                                           + " memo       text)";

    static final String         DROP_TABLE_SQL     = "drop table if exists recode";

    /**
     * 外部キーがはられているため　デフォルト可視性
     */
    static final String         TABLE_NAME         = "recode";

    /**
     * 外部キーがはられているため　デフォルト可視性
     */
    static final String         COLUMN_ID          = "_id";

    private static final String COLUMN_CATEGORY_ID = "categoryid";

    private static final String COLUMN_DATE_TIME   = "datetime";

    private static final String COLUMN_EVENT_ID    = "eventid";

    private static final String COLUMN_MEMO        = "memo";

    private SQLiteDatabase      db;

    /**
     * constractor.
     * 
     * @param db1
     */
    public RecodeDao(final SQLiteDatabase db1) {

        this.db = db1;
    }

    /**
     * constractor.
     * 
     * @param argCategoryId
     * @return delete count.
     */
    public int deleteByCategoryId(final int argCategoryId) {

        MyLog.getInstance()
                .writeDatabase("delete categoryId[" + argCategoryId + "]");
        int res = this.db.delete(TABLE_NAME, COLUMN_CATEGORY_ID + "="
                + argCategoryId, null);
        MyLog.getInstance().writeDatabase("result count[" + res + "]");
        return res;
    }

    /**
     * find by category id.
     * 
     * @param argCategoryId
     * @return list.
     */
    public List<Recode> findByCategoryId(final int argCategoryId) {

        MyLog.getInstance().readDatabase("categoryId[" + argCategoryId
                + "]");
        String[] columns = {
                COLUMN_ID, COLUMN_DATE_TIME, COLUMN_EVENT_ID, COLUMN_MEMO
        };

        Cursor c = this.db.query(TABLE_NAME,
                columns,
                COLUMN_CATEGORY_ID + " = " + argCategoryId,
                // selection
                null, // selectionArgs
                null, // groupBy
                null, // having
                COLUMN_DATE_TIME, // order by
                null // limit
        );

        int count = c.getCount();
        MyLog.getInstance().readDatabase("result count[" + count + "]");

        List<Recode> result = new ArrayList<Recode>(count);
        c.moveToFirst();
        for (int i = 0; i < count; i++) {
            long rowId = c.getLong(c.getColumnIndex(COLUMN_ID));

            int eventId = c.getInt(c.getColumnIndex(COLUMN_EVENT_ID));
            Recode.EventId ev = Recode.EventId.getValueFromDBValue(eventId);

            Recode data = new Recode(rowId,
                    argCategoryId,
                    new RecodeDateTime(c.getLong(c.getColumnIndex(COLUMN_DATE_TIME))),
                    ev,
                    c.getString(c.getColumnIndex(COLUMN_MEMO)));
            result.add(data);
            c.moveToNext();
        }
        c.close();
        return result;
    }

    /**
     * insert. rec.key is undefined. after called seted.
     * 
     * @param rec
     * @return key.
     */
    @SuppressWarnings("boxing")
    public long insert(final Recode rec) {

        MyLog.getInstance().writeDatabase("insertVal[" + rec + "]");
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_ID, rec.getCategoryId());
        values.put(COLUMN_DATE_TIME, rec.getDateTime().toMilliSecond());
        values.put(COLUMN_EVENT_ID, rec.getEventIdToDBValue());
        values.put(COLUMN_MEMO, rec.getMemo());

        long res = this.db.insert(TABLE_NAME, null, values);
        MyLog.getInstance().writeDatabase("result insert key id[" + res
                + "]");
        rec.setRowId(res);
        return res;
    }

}
