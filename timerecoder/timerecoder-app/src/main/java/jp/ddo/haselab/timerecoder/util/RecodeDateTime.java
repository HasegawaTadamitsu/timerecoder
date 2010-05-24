package jp.ddo.haselab.timerecoder.util;

import java.util.Date;
import java.text.SimpleDateFormat;

public class RecodeDateTime {

    private Date  date;

    public RecodeDateTime(){
	date = new Date();
    }

    public RecodeDateTime(final long arg){
	date = new Date(arg);
    }

    public long toMilliSecond(){
	return date.getTime();
    }

    @Override
	public String toString() {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:");    
	return sdf.format(date);
    }
    public String toYYYYMMDD() {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");    
	return sdf.format(date);
    }
    public String toHHMMSS() {
	SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");    
	return sdf.format(date);
    }

}
