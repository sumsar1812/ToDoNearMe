package sumsar1812.github.io.todonearme;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import sumsar1812.github.io.todonearme.adapter.PagerAdapter;
import sumsar1812.github.io.todonearme.model.ToDoItem;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static sumsar1812.github.io.todonearme.Constants.CREATE_REQ_CODE;
import static sumsar1812.github.io.todonearme.Constants.CREATE_RETURN_DATA;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton faButton;
    private MainActivity context;
    private LocationManager locationManager;
    private int LOCATION_REQUEST = 51;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setSupportActionBar(toolbar);
        initTabs();
        initListeners();
        initLocation();
    }

    private void initLocation() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        locationManager.requestLocationUpdates(NETWORK_PROVIDER, 0, 5, LocListener.getInstance());
        LocListener.getInstance().setLastLocation(locationManager.getLastKnownLocation(NETWORK_PROVIDER));
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST) {
            if (grantResults.length == 0)
                return;
            if (grantResults[0] == PERMISSION_DENIED) {
                Toast t = Toast.makeText(this, "You need Location permissions to continue using this app", Toast.LENGTH_LONG);
                t.show();
            } else if (grantResults[0] == PERMISSION_GRANTED) {
                initLocation();
            }
        }
    }
    private void initTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("To do"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    private void initViews() {
        context = this;
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        faButton = findViewById(R.id.floatingActionButton);
    }

    private void initListeners() {
        faButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateActivity.class);
                context.startActivityForResult(intent,CREATE_REQ_CODE);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CREATE_REQ_CODE) {
                ToDoItem toDoItem = (ToDoItem) data.getExtras().get(CREATE_RETURN_DATA);
                int i = 0;
            }
        }
    }
}
