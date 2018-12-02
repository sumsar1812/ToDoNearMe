package sumsar1812.github.io.todonearme;

import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

import sumsar1812.github.io.todonearme.adapter.PlaceAutocompleteAdapter;
import sumsar1812.github.io.todonearme.presenter.CreatePresenter;
import sumsar1812.github.io.todonearme.services.FetchAddressIntentService;
import sumsar1812.github.io.todonearme.services.FetchLocationIntentService;

public class CreateActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private final String TAG = "CreateActivity";
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    private Button btnAdd;
    private ImageButton btnMaps;
    private AutoCompleteTextView addressTextView;
    private TextView descTextView;
    private TextView nameTextView;
    private CreateActivity context;
    private AddressResultReceiver mResultReceiver;
    private LocationResultReceiver mLocResultRecv;
    private CreatePresenter presenter;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GeoDataClient mGeoDataClient;
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        init();
        presenter = new CreatePresenter(this);

    }

    private void init() {
        context = this;
        btnAdd = findViewById(R.id.buttonAdd);
        btnMaps = findViewById(R.id.mapsButton);
        addressTextView = findViewById(R.id.editTextAddress);
        descTextView = findViewById(R.id.editTextDesc);
        nameTextView = findViewById(R.id.editTextName);
        mResultReceiver = new AddressResultReceiver(null);
        mLocResultRecv = new LocationResultReceiver(null);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();
        mGeoDataClient = Places.getGeoDataClient(this);
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this,mGeoDataClient ,LAT_LNG_BOUNDS, null);
        addressTextView.setAdapter(mPlaceAutocompleteAdapter);

        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra(Constants.START_MAP_LOC,LocListener.getInstance().getLastLocation());
                context.startActivityForResult(intent, Constants.REQUEST_MAPLOC);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateFields()) {
                    Toast.makeText(context, "One or more fields are empty", Toast.LENGTH_LONG).show();
                }
                if (presenter.getLocation() == null) {
                    startLocIntentService(addressTextView.getText().toString());
                } else {
                    returnWithData();
                    setNameAndDesc();
                }

            }
        });
        addressTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (presenter.getLocation() != null)
                    presenter.setLocation(null);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.REQUEST_MAPLOC) {
                if (data.getExtras() == null)
                    return;
                LatLng latLng = (LatLng) data.getExtras().get(Constants.RETURN_LOCATION);
                if (latLng == null)
                    return;
                Location l = new Location("");
                l.setLatitude(latLng.latitude);
                l.setLongitude(latLng.longitude);
                presenter.setLocation(l);
                if (!Geocoder.isPresent()) {
                    Toast.makeText(this,
                            R.string.no_geocoder_available,
                            Toast.LENGTH_LONG).show();
                    return;
                }

                startAddressIntentService(l);
            }
        }
    }
    protected void startLocIntentService(String name) {
        Intent intent = new Intent(this, FetchLocationIntentService.class);
        intent.putExtra(Constants.RECEIVER, mLocResultRecv);
        intent.putExtra(Constants.ADDRESS_DATA_EXTRA, name);
        startService(intent);
    }
    protected void startAddressIntentService(Location l) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, l);
        startService(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void setNameAndDesc() {
        presenter.setDescription(descTextView.getText().toString());
        presenter.setName(descTextView.getText().toString());
    }
    private void returnWithData() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.CREATE_RETURN_DATA, presenter.getToDoItem());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
    private boolean validateFields() {
        return !nameTextView.getText().toString().isEmpty() && !descTextView.getText().toString().isEmpty() && !addressTextView.getText().toString().isEmpty();
    }


    /*
        ----------------------- google places api autocomplete suggestion handling -----------------------

     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);
            if (item == null)
                return;
            final String placeId = item.getPlaceId();
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.d(TAG, "Did not find place successfully");
                places.release();
                return;
            }
            final Place place = places.get(0);

            LatLng latLng = place.getLatLng();
            Location l = new Location("");
            l.setLatitude(latLng.latitude);
            l.setLongitude(latLng.longitude);
            presenter.setLocation(l);

            places.release();
        }
    };

    /*
    --------------------------- ResultReceiver classes -----------------------
     */
    public class LocationResultReceiver extends ResultReceiver {
        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public LocationResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultData == null) {
                return;
            }
            ArrayList<Location> data = resultData.getParcelableArrayList(Constants.RESULT_DATA_KEY_ARRAY);
            if (data == null || data.isEmpty()) {
                Toast.makeText(context, "No location found for: " + addressTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                return;
            }
            setNameAndDesc();
            //ToDoItem toDoItem = new ToDoItem(nameTextView.getText().toString(),descTextView.getText().toString(),data.get(0)); TODO FIX
            returnWithData();
        }
    }

    public class AddressResultReceiver extends ResultReceiver{
        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultData == null) {
                return;
            }
            // Display the address string
            // or an error message sent from the intent service.
            String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            if (mAddressOutput == null) {
                mAddressOutput = "";
            }
            if (mAddressOutput.contains(System.lineSeparator())) {
                mAddressOutput = mAddressOutput.split(System.lineSeparator())[0];
            }

            addressTextView.setText(mAddressOutput);

        }
    }
}
