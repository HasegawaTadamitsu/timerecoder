package jp.ddo.haselab.timerecoder.dataaccess;

import android.location.Location;

public final class RecodeLocation {

    final double latitude;
    final double longitude;
    final double altitude;
    final double accuracy;
    final double speed;
    final double bearing;
    
    public RecodeLocation(final Location location){
	this(location.getLatitude(),
	     location.getLongitude(),
	     location.getAltitude(),
	     location.getAccuracy(),
	     location.getSpeed(),
	     location.getBearing()
	     );
    }

    RecodeLocation(final double a,
		   final double b,
		   final double c,
		   final double d,
		   final double e,
		   final double f){
	latitude  = a;
	longitude = b;
	altitude  = c;
	accuracy  = d;
	speed     = e;
	bearing   = f;
    }

    /**
     *	緯度
     */
    public double getLatitude(){
	return latitude;
    }

    /**
     *	経度
     */
    public double getLongitude(){
	return longitude;
    }

    /**
     *	標高
     */
    public double getAltitude(){
	return altitude;
    }

    /**
     *	加速度
     */
    public double getAccuracy(){
	return accuracy;
    }
    /**
     *	speed
     */
    public double getSpeed(){
	return speed;
    }
	
    /**
     *	方向
     */
    public double getBearing(){
	return bearing;
    }
}
