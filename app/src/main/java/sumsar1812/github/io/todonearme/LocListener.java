package sumsar1812.github.io.todonearme;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class LocListener implements LocationListener {
    private static LocListener instance;
    public static LocListener getInstance() {
        if (instance == null)
            instance = new LocListener();
        return instance;
    }
    private Location lastLocation = null;

    private LocListener(){}
    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public Location getLastLocation() {
        return lastLocation;
    }
    public void setLastLocation(Location l) {
        lastLocation = l;
    }
}
