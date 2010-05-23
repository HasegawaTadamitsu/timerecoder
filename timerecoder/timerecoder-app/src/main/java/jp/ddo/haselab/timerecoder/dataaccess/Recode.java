package jp.ddo.haselab.timerecoder.dataaccess;

public class Recode {

    private String date;
    private int    eventId;
    private String memo;

    public Recode(String argDate,
		  int argEventId,
		  String argMemo){
	date= argDate;
	eventId = argEventId;
	memo=argMemo;
    }

    public String getDate() {
	return date;
    }
    public int  getEventId() {
	return eventId;
    }
    public String getMemo() {
	return memo;
    }
    public String toString() {
	return "date[" + date + "]" +
	    "eventId[" + eventId + "]" +
	    "memo[" + memo +"]";
    }

}
