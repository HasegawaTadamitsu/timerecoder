package jp.ddo.haselab.timerecoder.util;

import java.util.Date;
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

    public static String toStaticString( final long val ) {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");   
	Date stDate  = new Date(val);
 	return sdf.format(stDate);
    }

    @Override
	public String toString() {
	return toStaticString(this.toMilliSecond());
    }
}
