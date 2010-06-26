
package jp.ddo.haselab.timerecoder.util;

import jp.ddo.haselab.timerecoder.dataaccess.MyLocation;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;

/**
 * GPS のみとする。 Wifi やら他の位置取得情報があるけど、使わない。
 */
public final class RecodeLocationMgr implements
        LocationListener {

    private final Context    context;

    /**
     * 値取得に対し、合計何秒待つか.その待ち時間
     */
    private final int        locationTotalWaitSecondTime;

    private final String     PROVIDER                   = LocationManager.GPS_PROVIDER;

    private Location         lastLocation;

    private boolean          enableLocation;

    private boolean          nowRecoding;

    private static final int LOCATION_COUNT_PER_ONE_GET = 1000;

    /**
     * RecodeedCallBack interface
     */
    public static interface Callback {

        /**
         * 取得完了コールバック
         * 
         * @param arg
         *            取得した値。エラー発生時は、null.原因は、ログ出力される。
         */
        public void doneGet(final MyLocation arg);
    }

    /**
     * 
     * @param argContext
     *            コンテキスト。ここからサービスを取得.
     * @param argTotalWaitSecondTime
     *            総待ち時間　単位秒
     */
    public RecodeLocationMgr(final Context argContext,
            final int argTotalWaitSecondTime) {

        this.context = argContext;
        this.locationTotalWaitSecondTime = argTotalWaitSecondTime;
        this.enableLocation = false;
        this.nowRecoding = false;

    }

    /**
     * get Location.
     * 
     * @param key
     *            MyLocation 's key
     * @param callback
     *            取得完了時に呼ばれる.
     * @return true 測定開始を行う。/false すでに測定中なので測定を行わない。
     */
    public boolean getRecodeLocation(final long key,
            final Callback callback) {

        if (this.nowRecoding == true) {
            return false;
        }

        this.nowRecoding = true;

        final LocationManager locationMgr;
        locationMgr = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);

        final LocationListener lc = this;
        locationMgr.requestLocationUpdates(this.PROVIDER, 0, // interval
                // milli sec
                0.1f, // interval meter
                lc);

        this.enableLocation = true;
        this.lastLocation = null;

        final int intervalMilliSecondTime = this.locationTotalWaitSecondTime * 1000
                // milli // second
                / LOCATION_COUNT_PER_ONE_GET;

        final Handler handler = new Handler();

        final RecodeLocationMgr mgr = this;

        new Thread() {

            @SuppressWarnings("synthetic-access")
            @Override
            public void run() {

                MyLog.getInstance().verbose("location start");

                for (int i = 0; i < LOCATION_COUNT_PER_ONE_GET; i++) {
                    try {
                        Thread.sleep(intervalMilliSecondTime);
                    } catch (InterruptedException e) {
                        MyLog.getInstance().error("sleep error.", e);
                    }
                    if (RecodeLocationMgr.this.lastLocation != null) {
                        break;
                    }
                    if (RecodeLocationMgr.this.enableLocation == false) {
                        break;
                    }
                }

                handler.post(new Runnable() {

                    public void run() {

                        locationMgr.removeUpdates(lc);

                        RecodeLocationMgr.this.nowRecoding = false;

                        if (RecodeLocationMgr.this.lastLocation == null) {
                            callback.doneGet(null);
                            return;
                        }

                        callback.doneGet(new MyLocation(key,
                                new RecodeDateTime(),
                                RecodeLocationMgr.this.lastLocation));
                    }
                });
            }
        }.start();
        return true;
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
