
package jp.ddo.haselab.timerecoder;

import jp.ddo.haselab.timerecoder.dataaccess.LocationDao;
import jp.ddo.haselab.timerecoder.dataaccess.MyLocation;
import jp.ddo.haselab.timerecoder.util.MyLog;
import android.database.Cursor;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 *
 */
public final class LocationOverlay extends
        ItemizedOverlay<OverlayItem> {

    private Drawable mIcon = null;

    private Cursor mCursor = null;

    private static final int E6 = 1000000;

    /**
     * コンストラクタ.
     * 
     * @param arg0
     * @param arg1
     */
    public LocationOverlay(final Drawable arg0,
            final Cursor arg1) {

        super(arg0);
        this.mIcon = arg0;
        this.mCursor = arg1;
	populate();
    }

    @Override
    public int size() {

        MyLog.getInstance()
                .verbose("start");

        if (this.mCursor == null) {
            MyLog.getInstance()
                    .verbose("mCursor = null");

            return 0;
        }
        int co = this.mCursor.getCount();
        MyLog.getInstance()
                .verbose("mCursor count[" + co
                         + "]");
        return co;
    }

    @Override
    protected OverlayItem createItem(final int arg1) {

        MyLog.getInstance()
                .verbose("arg1=[" + arg1
                         + "]");

        if (this.mCursor == null) {
            MyLog.getInstance()
                    .verbose("mcursor=null");
        }

        if (!this.mCursor.moveToPosition(arg1)) {
            MyLog.getInstance()
                    .verbose("moveToPosition()=null");
            return null;
        }

        MyLocation loc = LocationDao.cursor2MyLocationAllColumn(this.mCursor);

        int LatitudeE6 = (int) loc.getLatitude() * E6;
        int longitudeE6 = (int) loc.getLongitude() * E6;

        OverlayItem item = new OverlayItem(
                new GeoPoint(LatitudeE6,
                        longitudeE6),
                "hoge",
                "boo");
        item.setMarker(this.mIcon);
        return item;
    }
}
