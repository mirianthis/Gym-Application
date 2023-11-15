package maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymbro.R;
import com.example.gymbro.databinding.ActivitySupplementsMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SupplementMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivitySupplementsMapsBinding binding;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySupplementsMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize mFusedLocationProviderClient
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    /**
     * Manipulates the map once available.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, get user's current location
            mMap.setMyLocationEnabled(true);
            mFusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                mLastKnownLocation = location;
                                latLng = new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                                // Set up Places API
                                Places.initialize(getApplicationContext(), "AIzaSyCnAmVQtUkkd4hV-sIYRmg2QGhn-hm5h18");
                                PlacesClient placesClient = Places.createClient(SupplementMapsActivity.this);

                                // Search for nearby supplement stores
                                String placeType = "pharmacy";
                                double radius = 2000; // in meters
                                String mlocation = latLng.latitude + "," + latLng.longitude;
                                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                                        "?location=" + mlocation +
                                        "&radius=" + radius +
                                        "&type=" + placeType +
                                        "&key=" + "AIzaSyCnAmVQtUkkd4hV-sIYRmg2QGhn-hm5h18";

                                // Send the request to the Places API
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    JSONArray results = response.getJSONArray("results");

                                                    // Loop through the results and add markers to the map
                                                    for (int i = 0; i < results.length(); i++) {
                                                        JSONObject result = results.getJSONObject(i);
                                                        JSONObject location = result.getJSONObject("geometry").getJSONObject("location");
                                                        String name = result.getString("name");
                                                        LatLng latLng = new LatLng(location.getDouble("lat"), location.getDouble("lng"));

                                                        mMap.addMarker(new MarkerOptions().position(latLng).title(name));
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                error.printStackTrace();
                                            }
                                        });

                                // Add the request to the queue
                                RequestQueue queue = Volley.newRequestQueue(SupplementMapsActivity.this);
                                queue.add(jsonObjectRequest);
                            }
                        }

                    });
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

    }
}
