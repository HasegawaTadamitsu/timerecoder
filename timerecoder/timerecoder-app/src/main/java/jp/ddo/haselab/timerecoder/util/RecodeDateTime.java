package jp.ddo.haselab.timerecoder.util;

import java.util.Date;
import android.content.Context;
import java.text.SimpleDateFormat;

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

    private String toAndroidFormatString(){
	if (dateFormat == null || timeFormat == null ) {
	    dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	    timeFormat = new SimpleDateFormat("HH:mm:ss");
	    MyLog.getInstance().warning(
			"setFromat was not called before this method."
			+ "Use default format." );

	}
 	return dateFormat.format(date) + " " + timeFormat.format(date);
    }

    @Override
	public String toString() {
	return toAndroidFormatString();
    }

    public String toYYYYMMDDHHMMSS(){
	java.text.DateFormat format = 
	    new SimpleDateFormat("yyyyMMddHHmmss");
 	return format.format(date);
    }

    private static java.text.DateFormat dateFormat = null;
    private static java.text.DateFormat timeFormat = null;

    public static void setFormat( final Context context) {
	dateFormat = android.text.format.DateFormat.getDateFormat(context);
	timeFormat = new SimpleDateFormat("HH:mm:ss");
    }



}
