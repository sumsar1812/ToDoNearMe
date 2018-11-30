package sumsar1812.github.io.todonearme;

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


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class FetchAddressIntentService extends IntentService {
    protected ResultReceiver mReceiver;
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationaddress";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FetchAddressIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        if (intent == null)
            return;
        Location l = intent.getParcelableExtra(LOCATION_DATA_EXTRA);
        if (l == null)
            return;
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(l.getLatitude(),l.getLongitude(),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses == null || addresses.size() == 0) {
            deliverResultToReceiver(FAILURE_RESULT,"error");
        } else {
            List<String> addressLines = new ArrayList<>();
            for (int i = 0;i <= addresses.get(0).getMaxAddressLineIndex(); i++) {
                addressLines.add(addresses.get(0).getAddressLine(i));
            }
            deliverResultToReceiver(SUCCESS_RESULT, TextUtils.join(System.lineSeparator(), addressLines));
        }
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
}
