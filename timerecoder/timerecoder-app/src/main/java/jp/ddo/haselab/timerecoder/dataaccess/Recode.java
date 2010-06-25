package jp.ddo.haselab.timerecoder.dataaccess;

import jp.ddo.haselab.timerecoder.util.RecodeDateTime;

public final class Recode {

	public enum EventId {
		START(0), END(1), ETC(2), ;
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

		static EventId getValueFromDBValue(final int argVal) {
			EventId[] arr = EventId.values();
			for (EventId e : arr) {
				if (e.toDBValue() == argVal) {
					return e;
				}
			}
			throw new IllegalArgumentException("unknown value.val[" + argVal
					+ "]");
		}
	}

	private long rowId;
	private final int categoryId;
	private final RecodeDateTime dateTime;
	private final EventId eventId;
	private final String memo;
	private RecodeLocation recodeLocation = null;

	public Recode(final int argCategoryId,
			final RecodeDateTime argRecodeDateTime, final EventId argEventId,
			final String argMemo, final RecodeLocation argRecodeLocation) {

		this(0L, argCategoryId, argRecodeDateTime, argEventId, argMemo);
		recodeLocation = argRecodeLocation;
	}

	public Recode(final int argCategoryId,
			final RecodeDateTime argRecodeDateTime, final EventId argEventId,
			final String argMemo) {

		this(0L, argCategoryId, argRecodeDateTime, argEventId, argMemo);
	}

	public Recode(final long argRowId, final int argCategoryId,
			final RecodeDateTime argRecodeDateTime, final EventId argEventId,
			final String argMemo) {

		rowId = argRowId;
		categoryId = argCategoryId;
		dateTime = argRecodeDateTime;
		eventId = argEventId;
		memo = argMemo;
	}

	public void setRowId(final long arg) {
		rowId = arg;
	}

	public long getRowId() {
		return rowId;
	}

	public long getKey() {
		return getRowId();
	}

	public int getCategoryId() {
		return categoryId;
	}

	public RecodeDateTime getDateTime() {
		return dateTime;
	}

	public String getEventToString() {
		return eventId.toString();
	}

	public int getEventToDBValue() {
		return eventId.toDBValue();
	}

	public String getMemo() {
		return memo;
	}

	public RecodeLocation getRecodeLocation() {
		return recodeLocation;
	}

	@Override
	public String toString() {
		return "rowId[" + rowId + "]" + "categoryId[" + categoryId + "]"
				+ "dateTime[" + dateTime + "]" + "eventId[" + eventId + "]"
				+ "memo[" + memo + "]" + "recodeLocation[" + recodeLocation
				+ "]";
	}
}
