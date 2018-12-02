package sumsar1812.github.io.todonearme;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location startLocation;
    private LatLng returnLatLng;
    private ImageView backBtn;
    private ImageView confirmBtn;
    private Marker lastMarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                startLocation = null;
            } else {
                startLocation = (Location) extras.get(Constants.START_MAP_LOC);
            }
        } else {
            startLocation = (Location) savedInstanceState.getSerializable(Constants.START_MAP_LOC);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.title);
        }


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (lastMarker != null)
                    lastMarker.remove();
                lastMarker = mMap.addMarker(new MarkerOptions().position(latLng));
                returnLatLng = latLng;


            }
        });
        if (startLocation == null)
            return;
        LatLng latLng = new LatLng(startLocation.getLatitude(), startLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title("Your position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
        backBtn = findViewById(R.id.imageView5);
        confirmBtn = findViewById(R.id.imageView6);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnLatLng = null;
                returnLocation();
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnLocation();
            }
        });
    }

    private void returnLocation() {
        if (lastMarker != null)
            lastMarker.remove();
        Intent returnIntent = new Intent();
        if (returnLatLng != null) {
            returnIntent.putExtra(Constants.RETURN_LOCATION, returnLatLng);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else {
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }
}
