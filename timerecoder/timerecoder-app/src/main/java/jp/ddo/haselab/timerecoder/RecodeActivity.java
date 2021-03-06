
package jp.ddo.haselab.timerecoder;

import java.io.IOException;
import java.util.List;

import jp.ddo.haselab.timerecoder.dataaccess.DatabaseHelper;
import jp.ddo.haselab.timerecoder.dataaccess.LocationDao;
import jp.ddo.haselab.timerecoder.dataaccess.MyLocation;
import jp.ddo.haselab.timerecoder.dataaccess.Recode;
import jp.ddo.haselab.timerecoder.dataaccess.RecodeDao;
import jp.ddo.haselab.timerecoder.util.MyLog;
import jp.ddo.haselab.timerecoder.util.RecodeAudioMgr;
import jp.ddo.haselab.timerecoder.util.RecodeDateTime;
import jp.ddo.haselab.timerecoder.util.RecodeLocationMgr;
import jp.ddo.haselab.timerecoder.util.YesJumpDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 主処理(Recode)Activity. recode 画面の処理を行います。
 * 
 * @author T.Hasegawa
 */
public final class RecodeActivity extends
        Activity implements
        OnClickListener {

    /**
     * category key.use for database and other.{@value}
     */
    public static final String KEY_CATE = "CATEGORY_KEY";

    private int mCategoryId = 0;

    private SQLiteDatabase mDb = null;

    private RecodeListAdapter mListAdapter;

    private RecodeAudioMgr mRecodeAudioMgr;

    private RecodeLocationMgr mRecodeLocationMgr;

    private int mDefaultRecodeTime; // second time

    private void appendListView(final Recode rec) {

        this.mListAdapter.addData(rec);
        ListView list = (ListView) findViewById(R.id.listview_data);
        list.setSelection(list.getCount());
    }

    /**
     * カテゴリIDで削除します.
     * 
     * @return RecodeDaoの削除件数(Locationの結果ではない）
     */
    private int doDeleteByCategoryId() {

        RecodeDao rDao = new RecodeDao(this.mDb);
        LocationDao lDao = new LocationDao(this.mDb);

        this.mDb.beginTransaction();
        MyLog.getInstance()
                .startTransaction("category[" + this.mCategoryId
                                  + "]");
        int rRes = 0;
        try {
            rRes = rDao.deleteByCategoryId(this.mCategoryId);
            lDao.deleteByCategoryId(this.mCategoryId);
            this.mDb.setTransactionSuccessful();
            MyLog.getInstance()
                    .endTransaction("success.delete count[Recode=" + rRes
                                    + "]");
        } finally {
            this.mDb.endTransaction();
        }
        return rRes;
    }

    private void dialogClear() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_clear_sure_title);
        builder.setMessage(R.string.dialog_clear_sure_msg);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    @SuppressWarnings("synthetic-access")
                    public void onClick(final DialogInterface dialog,
                            final int whichButton) {

                        doDeleteByCategoryId();
                        initListView();
                        return;
                    }
                });

        builder.setNegativeButton("No",
                null);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        return;
    }

    /**
     * MyMapActivityへ遷移します。
     */
    private void toMapActivity() {

        Intent intent = new Intent(RecodeActivity.this,
                                MyMapActivity.class);
        intent.putExtra(RecodeActivity.KEY_CATE,
                                0);
        startActivity(intent);
        finish();
        return;
    }

    private void doRecodeAudio(final RecodeDateTime argRecTime) {

        String fileName = "rec" + argRecTime.toYYYYMMDDHHMMSS()
                          + ".3gp";

        try {
            boolean res = this.mRecodeAudioMgr.startRecodingExternalStrage(fileName,
                    this.mDefaultRecodeTime);
            if (res == false) {
                Toast.makeText(this,
                        R.string.toast_recode_audio_now_msg,
                        Toast.LENGTH_SHORT)
                        .show();
                return;
            }

        } catch (IOException e) {
            MyLog.getInstance()
                    .error("recode error.IOException." + "fileName["
                            + fileName
                            + "]",
                            e);
            Toast.makeText(this,
                    R.string.toast_recode_audio_error_msg,
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void initListView() {

        RecodeDao dao = new RecodeDao(this.mDb);
        List<Recode> data = dao.findByCategoryId(this.mCategoryId);
        this.mListAdapter = new RecodeListAdapter(this,
                data);

        ListView list = (ListView) findViewById(R.id.listview_data);
        list.setAdapter(this.mListAdapter);
        list.setSelection(list.getCount());

        View emptyView = findViewById(R.id.listview_empty);
        list.setEmptyView(emptyView);

    }

    private void initWidget() {

        Button button;
        button = (Button) findViewById(R.id.button_clear);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.button_start);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.button_end);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.button_etc);
        button.setOnClickListener(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean useAudio = preferences.getBoolean("recode_use_audio",
                false);
        CheckBox checkBox;
        checkBox = (CheckBox) findViewById(R.id.checkbox_use_audio);
        checkBox.setChecked(useAudio);
        if (RecodeAudioMgr.canRecodeMachine() == false) {
            checkBox.setEnabled(false);
            checkBox.setChecked(false);
        }

        this.mRecodeAudioMgr = new RecodeAudioMgr();
        this.mDefaultRecodeTime = Integer.parseInt(preferences.getString("audio_recode_time",
                "10"));

        checkBox = (CheckBox) findViewById(R.id.checkbox_use_location);
        boolean useLocation = preferences.getBoolean("recode_use_location",
                false);
        checkBox.setChecked(useLocation);

        int localeTimeout = Integer.parseInt(preferences.getString("locale_timeout",
                "1"));
        this.mRecodeLocationMgr = new RecodeLocationMgr(this,
                localeTimeout);

        initListView();
    }

    private Recode insertDBRecode(final Recode rec) {

        RecodeDao dao = new RecodeDao(this.mDb);

        this.mDb.beginTransaction();
        MyLog.getInstance()
                .startTransaction("recode[" + rec.toString()
                                  + "]");

        try {
            dao.insert(rec);
            this.mDb.setTransactionSuccessful();
            MyLog.getInstance()
                    .endTransaction("success.recode[" + rec.toString()
                                    + "]");
        } finally {
            this.mDb.endTransaction();
        }
        return rec;
    }

    private void insertDBLocation(final MyLocation loc) {

        LocationDao dao = new LocationDao(this.mDb);

        this.mDb.beginTransaction();
        MyLog.getInstance()
                .startTransaction("location[" + loc.toString()
                                  + "]");

        try {
            dao.insert(loc);
            this.mDb.setTransactionSuccessful();
            MyLog.getInstance()
                    .endTransaction("success.loc[" + loc.toString()
                                    + "]");
        } finally {
            this.mDb.endTransaction();
        }
        return;
    }

    /**
     * クリック時の処理. 各種ボタンの処理を行います。
     * 
     * @param v
     *            押されたview
     */
    @Override
    public void onClick(final View v) {

        int id = v.getId();

        if (id == R.id.button_clear) {
            EditText editText = (EditText) findViewById(R.id.edittext_memo);
            editText.setText("");
            return;
        }

        EditText editText = (EditText) findViewById(R.id.edittext_memo);
        String memo = editText.getText()
                .toString();
        RecodeDateTime dateTime = new RecodeDateTime();

        Recode rec;
        switch (id) {
        case R.id.button_start:
            rec = new Recode(this.mCategoryId,
                    dateTime,
                    Recode.EventId.START,
                    memo);
            break;
        case R.id.button_end:
            rec = new Recode(this.mCategoryId,
                    dateTime,
                    Recode.EventId.END,
                    memo);
            break;
        case R.id.button_etc:
            rec = new Recode(this.mCategoryId,
                    dateTime,
                    Recode.EventId.ETC,
                    memo);
            break;
        default:
            throw new IllegalArgumentException("unknown button(view).id[" + id
                                               + "]");
        }
        insertDBRecode(rec);
        appendListView(rec);

        CheckBox cb;
        cb = (CheckBox) findViewById(R.id.checkbox_use_audio);
        if (cb.isChecked()) {
            doRecodeAudio(dateTime);
        }

        cb = (CheckBox) findViewById(R.id.checkbox_use_location);
        if (cb.isChecked()) {

            RecodeLocationMgr.Callback callBack = new RecodeLocationMgr.Callback() {

                @SuppressWarnings("synthetic-access")
                @Override
                public void doneGet(MyLocation arg) {

                    if (arg == null) {
                        Toast.makeText(RecodeActivity.this,
                                R.string.toast_recode_location_error_msg,
                                Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    RecodeActivity.this.insertDBLocation(arg);
                }
            };

            boolean canRecode = this.mRecodeLocationMgr.getRecodeLocation(rec.getKey(),
                    callBack);
            if (canRecode == false) {
                Toast.makeText(this,
                        R.string.toast_recode_location_now_msg,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }

        return;
    }

    /**
     * create. 各種ボタンのイベント登録などを行います。
     * 
     * @param savedInstanceState
     *            hmm
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        MyLog.getInstance()
                .verbose("start");
        setContentView(R.layout.recode);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        this.mDb = dbHelper.getWritableDatabase();

        initWidget();
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
        inflater.inflate(R.menu.recode,
                menu);
        return true;
    }

    /**
     * onDestroy. DBのclose
     */
    @Override
    protected void onDestroy() {

        MyLog.getInstance()
                .verbose("start");
        if (this.mDb != null) {
            MyLog.getInstance()
                    .verbose("close db");
            this.mDb.close();
        }
        super.onDestroy();
    }

    /**
     * menu処理の分岐. menu押下時の処理です。 終了や、同一カテゴリのデータ削除などあります。
     * 
     * @param item
     *            押下されたitem
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
        case R.id.menu_quit:
            new YesJumpDialog(this,
                    MainActivity.class,
                      R.string.dialog_quit_sure_title,
                      R.string.dialog_quit_sure_msg).execute();
            return true;
        case R.id.menu_tomap:
            toMapActivity();
            return true;
        case R.id.menu_clear:
            dialogClear();
            return true;
        default:
            return false;
        }
    }
}
