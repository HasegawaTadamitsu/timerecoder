package jp.ddo.haselab.timerecoder.util;

import android.location.Location;

public final class RecodeLocation {

    private final Location location;
    
    public RecodeLocation(final Location argLocation){
	location = argLocation;
    }

    /**
     *	緯度
     */
    public double getLatitude(){
	return location.getLatitude();
    }

    /**
     *	経度
     */
    public double getLongitude(){
	return location.getLongitude();
    }

    /**
     *	標高
     */
    public double getAltitude(){
	return location.getAltitude();
    }

    /**
     *	speed
     */
    public double getSpeed(){
	return location.getSpeed();
    }
	
    /**
     *	方向
     */
    public double getBearing(){
	return location.getBearing();
    }
}
