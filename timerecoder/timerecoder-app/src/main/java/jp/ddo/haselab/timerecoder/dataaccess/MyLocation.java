
package jp.ddo.haselab.timerecoder.dataaccess;

import android.location.Location;

/**
 * My locatin.
 * 
 * @author hasegawa
 * 
 */
public final class MyLocation {

    private final int    id;

    private final double latitude;

    private final double longitude;

    private final double altitude;

    private final double accuracy;

    private final double speed;

    private final double bearing;

    MyLocation(final int id1,
            final double a,
            final double b,
            final double c,
            final double d,
            final double e,
            final double f) {

        this.id = id1;
        this.latitude = a;
        this.longitude = b;
        this.altitude = c;
        this.accuracy = d;
        this.speed = e;
        this.bearing = f;
    }

    /**
     * constractor.
     * 
     * @param id1
     *            id *
     * @param location
     *            at android.location
     */
    public MyLocation(final int id1, final Location location) {

        this(id1,
                location.getLatitude(),
                location.getLongitude(),
                location.getAltitude(),
                location.getAccuracy(),
                location.getSpeed(),
                location.getBearing());
    }

    /**
     * 加速度
     * 
     * @return 加速度
     */
    public double getAccuracy() {

        return this.accuracy;
    }

    /**
     * 標高
     * 
     * @return 標高
     */
    public double getAltitude() {

        return this.altitude;
    }

    /**
     * 方向
     * 
     * @return 方向
     */
    public double getBearing() {

        return this.bearing;
    }

    /**
     * ID
     * 
     * @return id
     */
    public int getId() {

        return this.id;
    }

    /**
     * 緯度
     * 
     * @return 緯度
     */
    public double getLatitude() {

        return this.latitude;
    }

    /**
     * 経度
     * 
     * @return 経度
     */
    public double getLongitude() {

        return this.longitude;
    }

    /**
     * speed
     * 
     * @return speed
     */
    public double getSpeed() {

        return this.speed;
    }

}
