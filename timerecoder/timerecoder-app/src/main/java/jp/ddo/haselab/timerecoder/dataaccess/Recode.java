
package jp.ddo.haselab.timerecoder.dataaccess;

import jp.ddo.haselab.timerecoder.util.RecodeDateTime;

/**
 * Data trans object.
 * 
 * @author hasegawa
 * 
 */
public final class Recode {

    /**
     * event ID
     * 
     * @author hasegawa
     * 
     */
    public enum EventId {
        /**
         * start button
         */
        START(0),
        /**
         * end button
         */
        END(1),

        /**
         * etc button
         */
        ETC(2), ;

        static EventId getValueFromDBValue(final int argVal) {

            EventId[] arr = EventId.values();
            for (EventId e : arr) {
                if (e.toDBValue() == argVal) {
                    return e;
                }
            }
            throw new IllegalArgumentException("unknown value.val[" + argVal
                    + "]");
        }

        private int dbValue;

        private EventId(final int arg) {

            this.dbValue = arg;
        }

        /**
         * get for data base.
         * 
         * @return data base value.
         */
        public int toDBValue() {

            return this.dbValue;
        }

        @Override
        public String toString() {

            return name();
        }
    }

    private long                 rowId;

    private final int            categoryId;

    private final RecodeDateTime dateTime;

    private final EventId        eventId;

    private final String         memo;

    /**
     * constractor.
     * 
     * @param argCategoryId
     *            category id
     * @param argRecodeDateTime
     *            recodedatetime
     * @param argEventId
     *            eventId
     * @param argMemo
     *            memo
     */
    public Recode(final int argCategoryId,
            final RecodeDateTime argRecodeDateTime,
            final EventId argEventId,
            final String argMemo) {

        this(0L, argCategoryId, argRecodeDateTime, argEventId, argMemo);
    }

    /**
     * constractor with rowId
     * @param argRowId
     * @param argCategoryId
     * @param argRecodeDateTime
     * @param argEventId
     * @param argMemo
     */
    public Recode(final long argRowId,
            final int argCategoryId,
            final RecodeDateTime argRecodeDateTime,
            final EventId argEventId,
            final String argMemo) {

        this.rowId = argRowId;
        this.categoryId = argCategoryId;
        this.dateTime = argRecodeDateTime;
        this.eventId = argEventId;
        this.memo = argMemo;
    }

    /**
     * get category id.
     * @return category id.
     */
    public int getCategoryId() {

        return this.categoryId;
    }

    /**
     * get date time.
     * @return dateTime.
     */
    public RecodeDateTime getDateTime() {

        return this.dateTime;
    }

    /**
     * event Id for db value 
     * @return eventId
     */
    public int getEventIdToDBValue() {

        return this.eventId.toDBValue();
    }

    /**
     * eventId to String.
     * @return String eventId.
     */
    public String getEventToString() {

        return this.eventId.toString();
    }

    /**
     * get key ar Database.
     * @return getRowID.
     */
    public long getKey() {

        return getRowId();
    }

    /**
     * get Memo.
     * @return memo.
     */
    public String getMemo() {

        return this.memo;
    }

    /**
     * get RowId.
     * @return rowid.
     */
    public long getRowId() {

        return this.rowId;
    }

    /**
     * setting row id. 
     * because this value decide when isert to database.
     * @param arg
     */
    public void setRowId(final long arg) {

        this.rowId = arg;
    }

    @Override
    public String toString() {

        return "rowId[" + this.rowId
                + "]"
                + "categoryId["
                + this.categoryId
                + "]"
                + "dateTime["
                + this.dateTime
                + "]"
                + "eventId["
                + this.eventId
                + "]"
                + "memo["
                + this.memo
                + "]";
    }
}
