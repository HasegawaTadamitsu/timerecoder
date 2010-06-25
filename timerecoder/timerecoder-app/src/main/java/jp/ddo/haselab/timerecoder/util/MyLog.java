
package jp.ddo.haselab.timerecoder.util;

import java.util.regex.Pattern;

import android.util.Log;

/**
 * My Logger. auto tag and transaction/database level append.
 * 
 * @author hasegawa
 * 
 */
public final class MyLog {

    /**
     * Logger interface
     * 
     * @author hasegawa
     * 
     */
    public static interface Logger {

        /**
         * startTransaction
         * 
         * <pre>
         * MyLog.getInstance().startTransaction(&quot;recode[&quot; + rec + &quot;]&quot;);
         * try {
         *     dao.insert(rec);
         *     this.mDb.setTransactionSuccessful();
         *     MyLog.getInstance().endTransaction(&quot;success.recode[&quot; + rec + &quot;]&quot;);
         * } finally {
         *     this.mDb.endTransaction();
         * }
         * </pre>
         * 
         * @param arg
         *            write message.
         */
        public void startTransaction(final String arg);

        /**
         * end Transaction.
         * 
         * @see startTransaction
         * @param arg
         *            message
         */
        public void endTransaction(final String arg);

        /**
         * write database.
         * 
         * <pre>
         * MyLog.getInstance().writeDatabase(&quot;insertVal[&quot; + loc + &quot;]&quot;);
         * 
         * ContentValues values = new ContentValues();
         * ...
         * long res = this.db.insert(TABLE_NAME, null, values);
         * MyLog.getInstance().writeDatabase(&quot;result insert key id[&quot; + res + &quot;]&quot;);
         *</pre>
         * 
         * @param arg
         *            message
         */
        public void writeDatabase(final String arg);

        /**
         * read database.
         * 
         * <pre>
         * MyLog.getInstance().readDatabase(&quot;categoryId[&quot; + id + &quot;]&quot;);
         * 
         * Cursor c = this.db.query(TABLE_NAME, columns, COLUMN_ID + &quot; = &quot; + id, // selection
         *         null, // selectionArgs
         * 
         * int count = c.getCount();
         *  MyLog.getInstance().readDatabase("result count[" + count + "]");
         *</pre>
         * 
         * @param arg
         *            message
         */
        public void readDatabase(final String arg);

        /**
         * level verbose
         * 
         * @param arg
         *            message
         */
        public void verbose(final String arg);

        /**
         * level warning
         * 
         * @param arg
         *            message
         */
        public void warning(final String arg);

        /**
         * level error.
         * 
         * @param arg
         *            message
         */
        public void error(final String arg);

        /**
         * level error with exception.
         * 
         * @param arg
         *            message
         * @param e
         */
        public void error(final String arg,
                final Exception e);
    }

    /**
     * getInstance.
     * 
     * @return instance
     */
    public static Logger getInstance() {

        final StackTraceElement trace = Thread.currentThread()
                .getStackTrace()[3];

        final String cla = trace.getClassName();

        Pattern pattern = Pattern.compile("[\\.]+");
        final String[] splitedStr = pattern.split(cla);
        final String simpleClass = splitedStr[splitedStr.length - 1];

        final String mthd = trace.getMethodName();
        final int line = trace.getLineNumber();
        final String tag = simpleClass + "#" + mthd + ":" + line;
        return new Logger() {

            @Override
            public void endTransaction(final String arg) {

                Log.v(tag, "[end TR]" + arg);
                return;
            }

            @Override
            public void error(final String arg) {

                Log.e(tag, arg);
                return;
            }

            @Override
            public void error(final String arg,
                    final Exception e) {

                Log.e(tag, arg, e);
                return;
            }

            @Override
            public void readDatabase(final String arg) {

                Log.v(tag, "[READ DB]" + arg);
                return;
            }

            @Override
            public void startTransaction(final String arg) {

                Log.v(tag, "[START TR]" + arg);
                return;
            }

            @Override
            public void verbose(final String arg) {

                Log.v(tag, arg);
                return;
            }

            @Override
            public void warning(final String arg) {

                Log.w(tag, arg);
                return;
            }

            @Override
            public void writeDatabase(final String arg) {

                Log.v(tag, "[WRITE DB]" + arg);
                return;
            }
        };
    }

    private MyLog() {

        // nothing
    }
}
