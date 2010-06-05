package jp.ddo.haselab.timerecoder.util;

import java.util.regex.Pattern;
import android.util.Log;

public final class MyLog {

    public static interface Logger {
	public void verbose(final String arg);
    }

    private MyLog(){
    }

    public static Logger getInstance(){

	final StackTraceElement trace = 
	    Thread.currentThread().getStackTrace()[3];

	final String cla = trace.getClassName();

	Pattern pattern = Pattern.compile("[\\.]+");
	final String[] splitedStr = pattern.split(cla);
	final String simpleClass = splitedStr[splitedStr.length -1];

	final String mthd = trace.getMethodName();
	final int   line  = trace.getLineNumber();
	final String tag =
	    simpleClass + "#" + mthd +":" + line;
	return new Logger() {
	    @Override
		public void verbose(final String arg){
		Log.v(tag, arg);
		return ;
	    }
	};
	    }
}

