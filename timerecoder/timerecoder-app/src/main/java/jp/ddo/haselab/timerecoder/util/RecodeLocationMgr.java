
package jp.ddo.haselab.timerecoder.util;

import jp.ddo.haselab.timerecoder.dataaccess.MyLocation;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

/**
 * GPS のみとする。 Wifi やら他の位置取得情報があるけど、使わない。
 */
public final class RecodeLocationMgr implements
        LocationListener {

    private final Context context;

    /**
     * 値取得に対し、合計何秒待つか.その待ち時間
     */
    private int           locationTotalWaitSecondTime;

    /**
     * 一回の取得に対し、何回値をチェックするのかの値
     */
    private int           LOCATION_COUNT_PER_ONE_GET = 1000;

    private final String  PROVIDER                   = LocationManager.GPS_PROVIDER;

    private Location      lastLocation;

    private boolean       enableLocation;

    /**
     * 
     * @param argContext
     *            コンテキスト。ここからサービスを取得.
     * @param argTotalWaitSecondTime 総待ち時間　単位秒
     * @param argWaitSecondTime
     *            取得にあたって最大待つ時間。単位秒.
     */
    public RecodeLocationMgr(final Context argContext,
            final int argTotalWaitSecondTime) {

        this.context = argContext;
        this.locationTotalWaitSecondTime = argTotalWaitSecondTime;
        this.enableLocation = false;
    }

    /**
     * get Location.
     * @param key MyLocation 's key
     * @return MyLocation
     */
    public MyLocation getRecodeLocation(final long key) {

        LocationManager locationMgr;
        locationMgr = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);

        locationMgr.requestLocationUpdates(this.PROVIDER, 0, // interval milli
                // sec
                0.1f, // interval meter
                this);

        this.enableLocation = true;
        this.lastLocation = null;

        final int intervalMilliSecondTime = this.locationTotalWaitSecondTime * 1000 // milli second
                / this.LOCATION_COUNT_PER_ONE_GET;

        for (int i = 0; i < this.LOCATION_COUNT_PER_ONE_GET; i++) {
            try {
                Thread.sleep(intervalMilliSecondTime);
            } catch (InterruptedException e) {
                MyLog.getInstance().error("sleep error.", e);
            }
            if (this.lastLocation != null) {
                break;
            }
            if (this.enableLocation == false) {
                break;
            }
        }
        locationMgr.removeUpdates(this);
        locationMgr = null;

        if (this.lastLocation == null) {
            return null;
        }

        return new MyLocation(key, this.lastLocation);
    }

    @Override
    public void onLocationChanged(final Location location) {

        this.lastLocation = location;
    }

    @Override
    public void onProviderDisabled(final String provider) {

        if (!provider.equals(this.PROVIDER)) {
            return;
        }
        this.enableLocation = false;
    }

    @Override
    public void onProviderEnabled(final String provider) {

        if (!provider.equals(this.PROVIDER)) {
            return;
        }
        this.enableLocation = true;
    }

    public void onStatusChanged(final String provider,
            final int status,
            final Bundle extras) {

        if (!provider.equals(this.PROVIDER)) {
            return;
        }
        if (status == LocationProvider.AVAILABLE) {
            this.enableLocation = true;
            return;
        }

        if (status == LocationProvider.OUT_OF_SERVICE || status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
            this.enableLocation = false;
            return;
        }

        this.enableLocation = false;
        return;
    }
}
