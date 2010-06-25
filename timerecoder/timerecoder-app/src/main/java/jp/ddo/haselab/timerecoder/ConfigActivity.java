package jp.ddo.haselab.timerecoder;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.database.sqlite.SQLiteDatabase;

import jp.ddo.haselab.timerecoder.util.DialogExPreference;
import jp.ddo.haselab.timerecoder.util.MyLog;
import jp.ddo.haselab.timerecoder.dataaccess.DatabaseHelper;

public class ConfigActivity
        extends PreferenceActivity implements
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
        SQLiteDatabase mDb = dbHelper.getWritableDatabase();
        dbHelper.onCreate(mDb);
    }
}
