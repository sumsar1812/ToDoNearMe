package sumsar1812.github.io.todonearme.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sumsar1812.github.io.todonearme.Constants;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class FetchAddressIntentService extends IntentService {
    protected ResultReceiver mReceiver;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FetchAddressIntentService(String name) {
        super(name);
    }
    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        if (intent == null)
            return;
        Location l = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        if (l == null || mReceiver == null)
            return;
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(l.getLatitude(),l.getLongitude(),5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses == null || addresses.size() == 0) {
            deliverResultToReceiver(Constants.FAILURE_RESULT,"error");
        } else {
            List<String> addressLines = new ArrayList<>();
            for (Address address : addresses) {
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressLines.add(address.getAddressLine(i));
                }
            }
            deliverResultToReceiver(Constants.SUCCESS_RESULT, TextUtils.join(System.lineSeparator(), addressLines));
        }
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
}
