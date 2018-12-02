package sumsar1812.github.io.todonearme.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sumsar1812.github.io.todonearme.Constants;

public class FetchLocationIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    ResultReceiver mResultRecv;
    public FetchLocationIntentService(String name) {
        super(name);
    }
    public FetchLocationIntentService() {
        super("FetchLocationIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String address = intent.getStringExtra(Constants.ADDRESS_DATA_EXTRA);
        mResultRecv = intent.getParcelableExtra(Constants.RECEIVER);
        if (address == null || mResultRecv == null)
            return;
        try {
            List<Address> locations =  geocoder.getFromLocationName(address, 5);
            ArrayList<Parcelable> returnList = new ArrayList<>();
            for (Address location : locations) {
                Location l = new Location("");
                l.setLatitude(location.getLatitude());
                l.setLongitude(location.getLongitude());
                returnList.add(l);
            }
            if (returnList.size() == 0) {
                deliverResultToReceiver(Constants.FAILURE_RESULT, "error");
            } else {
                deliverResultToReceiver(Constants.SUCCESS_RESULT, returnList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mResultRecv.send(resultCode, bundle);
    }
    private void deliverResultToReceiver(int resultCode, ArrayList<Parcelable> message) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.RESULT_DATA_KEY_ARRAY, message);
        mResultRecv.send(resultCode, bundle);
    }
}
