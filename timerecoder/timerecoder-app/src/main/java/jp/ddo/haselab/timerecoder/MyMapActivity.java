
package jp.ddo.haselab.timerecoder;

import java.util.List;

import jp.ddo.haselab.timerecoder.dataaccess.DatabaseHelper;
import jp.ddo.haselab.timerecoder.dataaccess.LocationDao;
import jp.ddo.haselab.timerecoder.util.MyLog;
import jp.ddo.haselab.timerecoder.util.YesJumpDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * 
 */
public final class MyMapActivity extends
        MapActivity {

    // マップの初期位置
    private static final GeoPoint FIRST_POINT = new GeoPoint(35364787,
                                                                138729758);

    // マップの初期拡大率
    private static final int FIRST_ZOOM_LEVEL = 8;

    private MapView mMapView;

    private MapController mMapController = null;

    private SQLiteDatabase mDb;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymap);

        MyLog.getInstance()
                .verbose("start");

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        this.mDb = dbHelper.getWritableDatabase();

        this.mMapView = (MapView) findViewById(R.id.mymap_map);
        this.mMapView.setClickable(true);
        this.mMapView.setBuiltInZoomControls(true);

        this.mMapController = this.mMapView.getController();
        this.mMapController.animateTo(FIRST_POINT);
        this.mMapController.setZoom(FIRST_ZOOM_LEVEL);

        Drawable pin = getResources().getDrawable(R.drawable.icon);
        pin.setBounds(0,
                  0,
                pin.getMinimumWidth(),
                pin.getMinimumHeight());
        LocationDao dao = new LocationDao(this.mDb);
        Cursor cur = dao.findByCategoryId(0);

        LocationOverlay overlay = new LocationOverlay(pin,
                cur);
        overlay.pop();
        List<Overlay> overlays = this.mMapView.getOverlays();
        overlays.add(overlay);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        // DBを閉じる
        this.mDb.close();
    }

    @Override
    protected boolean isRouteDisplayed() {

        return false;
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
        inflater.inflate(R.menu.mymap,
                menu);
        return true;
    }

    /**
     * 
     * @param item
     *            押下されたitem
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
        case R.id.menu_maptoquit:
            new YesJumpDialog(this,
                    MainActivity.class,
                      R.string.dialog_quit_sure_title,
                      R.string.dialog_quit_sure_msg).execute();
            return true;
        case R.id.menu_maptorecode:
            toRecodeActivity();
            return true;
        default:
            return false;
        }
    }

    /**
     * recodeActivityへ遷移します。
     */
    private void toRecodeActivity() {

        Intent intent = new Intent(MyMapActivity.this,
                                RecodeActivity.class);
        intent.putExtra(RecodeActivity.KEY_CATE,
                                0);
        startActivity(intent);
        finish();
        return;
    }

}
