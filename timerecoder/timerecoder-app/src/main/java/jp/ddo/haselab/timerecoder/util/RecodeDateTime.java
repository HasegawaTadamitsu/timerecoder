package jp.ddo.haselab.timerecoder.util;

import java.util.Date;
import android.content.Context;

public final class RecodeDateTime {

    private final Date date;

    public RecodeDateTime(){
	date = new Date();
    }

    public RecodeDateTime(final long arg){
	date = new Date(arg);
    }

    public long toMilliSecond(){
	return date.getTime();
    }

    private static java.text.DateFormat dateFormat = null;
    private static java.text.DateFormat timeFormat = null;

    public static void setFormat( final Context context) {
	dateFormat = android.text.format.DateFormat.getDateFormat(context);
	timeFormat = android.text.format.DateFormat.getTimeFormat(context);
    }

    public static String toStaticString(final long val ) {
	if (dateFormat == null || timeFormat == null ) {
	    throw new IllegalStateException(
		    "date or time format is null.call setFormat method."
					    );
	}
	Date stDate  = new Date(val);
 	return dateFormat.format(stDate) + " " + timeFormat.format(stDate);
    }

    @Override
	public String toString() {
	return toStaticString(this.toMilliSecond());
    }
}
