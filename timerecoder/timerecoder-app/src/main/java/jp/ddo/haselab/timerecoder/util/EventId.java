package jp.ddo.haselab.timerecoder.util;

public enum EventId {
    START(0),
    END(1),
    ETC(2),
	;
    
    private int dbValue;

    private EventId(final int arg) {
	dbValue = arg;
    }

    public int toDBValue() {
	return dbValue;
    }

    @Override
	public String toString() {
	return name();
    }

    public static EventId getValueFromDBValue(final int argVal){
	EventId[] arr = EventId.values();
	for (EventId e : arr) {
	    if (e.toDBValue() == argVal) {
		return e;
	    }
	}
	throw new IllegalArgumentException(
			   "unknown value.val[" + argVal + "]");
    }
}
