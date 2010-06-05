package jp.ddo.haselab.timerecoder;

import android.content.Context;
import android.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.TextView;
import android.database.Cursor;

import jp.ddo.haselab.timerecoder.util.RecodeDateTime;
import jp.ddo.haselab.timerecoder.dataaccess.RecodeDao;

/**
 *
 * @author T.Hasegawa
 */
final class RecodeListAdapter extends SimpleCursorAdapter {

    private static final int[] TO_RECODE_ITEM = {
	R.id._id,
	R.id.datetime,
	R.id.eventid,
	R.id.memo
    };

    public RecodeListAdapter(final Context cx,
			     final Cursor cursor) {
	super(cx,
	      R.layout.recode_item,
	      cursor,
	      RecodeDao.FIND_BY_CATEGORY_COLUMN,
	      TO_RECODE_ITEM);
	setViewBinder(new ListViewBinder());
    }

    class ListViewBinder implements SimpleCursorAdapter.ViewBinder {
	private int count = 0;
        public ListViewBinder() {
        }

        @Override
	    public boolean setViewValue(final View view,
					final Cursor cursor,
					final int columnIndex ) {

            int dateTimeIndex = cursor.getColumnIndex(
					RecodeDao.COLUMN_DATE_TIME);

            if (columnIndex  == dateTimeIndex) {
                long dateTime = cursor.getLong(dateTimeIndex);
		String val = RecodeDateTime.toStaticString(dateTime);
		((TextView)view).setText(val);
		return true;
	    }

	    return false;
	}
    }
}
