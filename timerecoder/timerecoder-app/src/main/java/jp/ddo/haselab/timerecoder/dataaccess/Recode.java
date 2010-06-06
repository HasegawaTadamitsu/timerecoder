package jp.ddo.haselab.timerecoder.dataaccess;

import jp.ddo.haselab.timerecoder.util.RecodeDateTime;
import jp.ddo.haselab.timerecoder.util.EventId;

public final class Recode {

    private long  rowId;
    private final int   categoryId;
    private final RecodeDateTime dateTime;
    private final EventId eventId;
    private final String memo;

    public Recode(final int argCategoryId,
		  final RecodeDateTime argRecodeDateTime,
		  final EventId argEventId,
		  final String argMemo){

	this(0L,
	     argCategoryId,
	     argRecodeDateTime,
	     argEventId,
	     argMemo);
    }

    public Recode(final long argRowId,
		  final int argCategoryId,
		  final RecodeDateTime argRecodeDateTime,
		  final EventId argEventId,
		  final String argMemo){

	rowId      = argRowId;
	categoryId = argCategoryId;
	dateTime = argRecodeDateTime;
	eventId  = argEventId;
	memo     = argMemo;
    }

    public void setRowId(final long arg){
	rowId = arg;
    }

    public long getRowId(){
	return rowId;
    }

    public long getKey(){
	return getRowId();
    }

    public int getCategoryId() {
	return categoryId;
    }

    public RecodeDateTime getDateTime() {
	return dateTime;
    }

    public String  getEventToString() {
	return eventId.toString();
    }

    public int  getEventToDBValue() {
	return eventId.toDBValue();
    }

    public String getMemo() {
	return memo;
    }

    @Override
	public String toString() {
	return "rowId[" + rowId + "]" +
	    "categoryId[" + categoryId + "]" +
	    "dateTime[" + dateTime + "]" +
	    "eventId[" + eventId + "]" +
	    "memo[" + memo +"]";
    }
}
