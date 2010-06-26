
package jp.ddo.haselab.timerecoder;

import jp.ddo.haselab.timerecoder.dataaccess.DatabaseHelper;
import jp.ddo.haselab.timerecoder.util.DialogExPreference;
import jp.ddo.haselab.timerecoder.util.MyLog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * ConfigActivity.
 * 
 * @author hasegawa
 * 
 */
public class ConfigActivity extends
        PreferenceActivity implements
        DialogExPreference.ButtonListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.config);

        DialogExPreference dialog = (DialogExPreference) findPreference("clear_all_datas");
        dialog.setButtonListener(this);
    }

    @Override
    public void onPositiveClick() {

        MyLog.getInstance().verbose("start");

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);

        MyLog.getInstance().verbose("close db");
        db.close();

    }
}
