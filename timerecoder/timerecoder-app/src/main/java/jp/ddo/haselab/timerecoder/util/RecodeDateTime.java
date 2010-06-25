
package jp.ddo.haselab.timerecoder.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;

/**
 * date time utility.
 * 
 * @author hasegawa
 * 
 */
public final class RecodeDateTime {

    private final Date                  date;

    private static java.text.DateFormat dateFormat = null;

    private static java.text.DateFormat timeFormat = null;

    /**
     * set format.
     * 
     * @param context
     *            context.
     */
    public static void setFormat(final Context context) {

        dateFormat = android.text.format.DateFormat.getDateFormat(context);
        timeFormat = new SimpleDateFormat("HH:mm:ss");
    }

    /**
     * constractor.
     */
    public RecodeDateTime() {

        this.date = new Date();
    }

    /**
     * constractor. 引数はlong.
     * 
     * @param arg
     */
    public RecodeDateTime(final long arg) {

        this.date = new Date(arg);
    }

    private String toAndroidFormatString() {

        if (dateFormat == null || timeFormat == null) {
            dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            timeFormat = new SimpleDateFormat("HH:mm:ss");
            MyLog.getInstance()
                    .warning("setFromat was not called before this method." + "Use default format.");

        }
        return dateFormat.format(this.date) + " "
                + timeFormat.format(this.date);
    }

    /**
     * このクラスが保持しているミリ秒を返します.
     * 
     * @return ミリ秒.
     */
    public long toMilliSecond() {

        return this.date.getTime();
    }

    @Override
    public String toString() {

        return toAndroidFormatString();
    }

    /**
     * このクラスの保持している値を文字列で返します。
     * YYYYMMDDHHMMSSの形式で返します。
     * @return 文字列
     */
    public String toYYYYMMDDHHMMSS() {

        java.text.DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(this.date);
    }
}
