package jp.ddo.haselab.timerecoder.util;

import android.util.Log;

public final class MyLog {

    public static interface Logger {
	public void verbose(final String arg);
    }

    private MyLog(){
    }

    public static Logger getInstance(Object obj){
	final String tag = obj.getClass().getSimpleName();
	return new Logger() {
	    @Override
		public void verbose(final String arg){
		Log.v(tag, arg);
		return ;
	    }
	};
	    }
}

