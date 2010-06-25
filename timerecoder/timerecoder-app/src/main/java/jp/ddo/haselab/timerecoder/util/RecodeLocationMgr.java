package jp.ddo.haselab.timerecoder.util;

import android.os.Bundle;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.location.LocationProvider;
import jp.ddo.haselab.timerecoder.dataaccess.RecodeLocation;

/**
 * GPS のみとする。 Wifi やら他の位置取得情報があるけど、使わない。
 */
public final class RecodeLocationMgr implements LocationListener {

	private final Context context;

	/**
	 * 値取得に対し、合計何秒待つか.その待ち時間
	 */
	private int locationTotalWaitSecondTime;

	/**
	 * 一回の取得に対し、何回値をチェックするのかの値
	 */
	private int LOCATION_COUNT_PER_ONE_GET = 1000;

	private final String PROVIDER = LocationManager.GPS_PROVIDER;

	private Location lastLocation;

	private boolean enableLocation;

	/**
	 * 
	 * @param argContext
	 *            コンテキスト。ここからサービスを取得.
	 * @param argWaitSecondTime
	 *            取得にあたって最大待つ時間。単位秒.
	 */
	public RecodeLocationMgr(final Context argContext,
			final int argTotalWaitSecondTime) {
		context = argContext;
		locationTotalWaitSecondTime = argTotalWaitSecondTime;
		enableLocation = false;
	}

	@Override
	public void onLocationChanged(final Location location) {
		lastLocation = location;
	}

	@Override
	public void onProviderDisabled(final String provider) {
		if (!provider.equals(PROVIDER)) {
			return;
		}
		enableLocation = false;
	}

	@Override
	public void onProviderEnabled(final String provider) {
		if (!provider.equals(PROVIDER)) {
			return;
		}
		enableLocation = true;
	}

	public void onStatusChanged(final String provider, final int status,
			final Bundle extras) {
		if (!provider.equals(PROVIDER)) {
			return;
		}
		if (status == LocationProvider.AVAILABLE) {
			enableLocation = true;
			return;
		}

		if (status == LocationProvider.OUT_OF_SERVICE
				|| status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
			enableLocation = false;
			return;
		}

		enableLocation = false;
		return;
	}

	public RecodeLocation getRecodeLocation() {

		LocationManager locationMgr;
		locationMgr = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		locationMgr.requestLocationUpdates(PROVIDER, 0, // interval milli sec
				0.1f, // interval meter
				this);

		enableLocation = true;
		lastLocation = null;

		final int intervalMilliSecondTime = locationTotalWaitSecondTime * 1000 // milli
																				// second
				/ LOCATION_COUNT_PER_ONE_GET;

		for (int i = 0; i < LOCATION_COUNT_PER_ONE_GET; i++) {
			try {
				Thread.sleep(intervalMilliSecondTime);
			} catch (InterruptedException e) {
				MyLog.getInstance().error("sleep error.", e);
			}
			if (lastLocation != null) {
				break;
			}
			if (enableLocation == false) {
				break;
			}
		}
		locationMgr.removeUpdates(this);
		locationMgr = null;

		if (lastLocation == null) {
			return null;
		}

		return new RecodeLocation(lastLocation);
	}
}
