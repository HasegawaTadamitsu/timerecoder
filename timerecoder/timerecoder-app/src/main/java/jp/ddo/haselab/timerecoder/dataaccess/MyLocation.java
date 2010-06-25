package jp.ddo.haselab.timerecoder.dataaccess;

import android.location.*;

/**
 * My locatin.
 * 
 * @author hasegawa
 * 
 */
public final class MyLocation {

    final int id;
    final double latitude;
    final double longitude;
    final double altitude;
    final double accuracy;
    final double speed;
    final double bearing;

    public MyLocation(final int id, final Location location) {
        this(id, location.getLatitude(), location.getLongitude(), location
                .getAltitude(), location.getAccuracy(), location
                .getSpeed(), location.getBearing());
    }

    MyLocation(final int id, final double a, final double b,
            final double c, final double d, final double e, final double f) {
        this.id = id;
        latitude = a;
        longitude = b;
        altitude = c;
        accuracy = d;
        speed = e;
        bearing = f;
    }

    /**
     * ID
     * 
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * 緯度
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * 経度
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * 標高
     */
    public double getAltitude() {
        return altitude;
    }

    /**
     * 加速度
     */
    public double getAccuracy() {
        return accuracy;
    }

    /**
     * speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * 方向
     */
    public double getBearing() {
        return bearing;
    }

}
