
package jp.ddo.haselab.timerecoder;

import java.util.List;

import jp.ddo.haselab.timerecoder.dataaccess.Recode;
import jp.ddo.haselab.timerecoder.util.MyLog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * @author T.Hasegawa
 */
final class RecodeListAdapter extends
        BaseAdapter {

    private List<Recode>  data;

    private final Context context;

    public RecodeListAdapter(final Context argContext,
            final List<Recode> argData) {

        MyLog.getInstance().verbose("start");
        this.context = argContext;
        this.data = argData;
    }

    public void addData(final Recode argData) {

        this.data.add(argData);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        MyLog.getInstance().verbose("called.result[" + this.data.size()
                + "]");
        return this.data.size();
    }

    @Override
    public Object getItem(final int argPosition) {

        MyLog.getInstance().verbose("argPosition[" + argPosition + "]");
        MyLog.getInstance().verbose("result[" + this.data.get(argPosition)
                + "]");
        return this.data.get(argPosition);
    }

    @Override
    public long getItemId(final int argPosition) {

        MyLog.getInstance().verbose("argPosition[" + argPosition + "]");
        return argPosition;
    }

    @Override
    public View getView(final int position,
            final View convertView,
            final ViewGroup parentViewGroup) {

        View resultView = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            resultView = inflater.inflate(R.layout.recode_item,
                    parentViewGroup,
                    false);
        }
        Recode rec = (Recode) this.getItem(position);

        TextView number = (TextView) resultView.findViewById(R.id.number);
        number.setText(position + "");

        TextView dateTime = (TextView) resultView.findViewById(R.id.datetime);
        dateTime.setText(rec.getDateTime().toString());

        TextView event = (TextView) resultView.findViewById(R.id.eventid);
        event.setText(rec.getEventToString());

        TextView memo = (TextView) resultView.findViewById(R.id.memo);
        memo.setText(rec.getMemo());

        return resultView;
    }
}
