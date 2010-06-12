package jp.ddo.haselab.timerecoder.util;

import java.util.regex.Pattern;
import android.util.Log;

public final class MyLog {

    public static interface Logger {
	public void verbose(final String arg);
	public void warning(final String arg);
	public void error(final String arg, final Exception e);
	public void writeDatabase(final String arg);
	public void readDatabase(final String arg);
	public void startTransaction(final String arg);
	public void endTransaction(final String arg);
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
	    @Override
		public void warning(final String arg){
		Log.w(tag, arg);
		return ;
	    }
	    @Override
		public void error(final String arg,
				  final Exception e){
		Log.e(tag, arg, e);
		return ;
	    }

	    @Override
		public void writeDatabase(final String arg){
		Log.v(tag, "[WRITE DB]" + arg);
		return ;
	    }
	    @Override
		public void readDatabase(final String arg){
		Log.v(tag, "[READ DB]" + arg);
		return ;
	    }
	    @Override
		public void startTransaction(final String arg){
		Log.v(tag, "[START TR]" + arg);
		return ;
	    }
	    @Override
		public void endTransaction(final String arg){
		Log.v(tag, "[end TR]" + arg);
		return ;
	    }
	};
	    }
}

