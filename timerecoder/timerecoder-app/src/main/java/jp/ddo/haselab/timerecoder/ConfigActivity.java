
package jp.ddo.haselab.timerecoder;

import jp.ddo.haselab.timerecoder.dataaccess.DatabaseHelper;
import jp.ddo.haselab.timerecoder.util.DialogExPreference;
import jp.ddo.haselab.timerecoder.util.MyLog;
import jp.ddo.haselab.timerecoder.util.YesJumpDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * ConfigActivity.
 * 
 * 
 */
public final class ConfigActivity extends
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

        MyLog.getInstance()
                .verbose("start init database");

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);

        MyLog.getInstance()
                .verbose("end init database");
        db.close();

    }

    /**
     * Menuの作成. Menuの作成します.
     * 
     * @param menu
     *            メニュー
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.config,
                menu);
        return true;
    }

    /**
     * menu処理の分岐. menu押下時の処理です。 終了させます。
     * 
     * @param item
     *            押下されたitem
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
        case R.id.menu_configtoquit:
            MyLog.getInstance()
            .verbose("YESno");
            new YesJumpDialog(this,
                    MainActivity.class,
                      R.string.dialog_quit_sure_title,
                      R.string.dialog_quit_sure_msg).execute();
            return true;
        default:
            return false;
        }
    }

}
