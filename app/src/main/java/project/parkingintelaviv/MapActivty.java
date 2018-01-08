package project.parkingintelaviv;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by Almog_Shemesh on 04/01/2018.
 */

public class MapActivty extends AppCompatActivity implements OnMapReadyCallback {


    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 555;
    private static final float DEFAULT_ZOOM = 15f;

    //vars
    private boolean mLocationPramissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPramissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
         }
     }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getLocationPermission();
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devicees current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPramissionGranted){
                Task loction = mFusedLocationProviderClient.getLastLocation();
                loction.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            if(currentLocation == null){
                                Toast.makeText(MapActivty.this, "turn GPS on to get youer location", Toast.LENGTH_LONG).show();
                            }else {
                                moveCamera(new LatLng(currentLocation.getLatitude() , currentLocation.getLongitude()) , DEFAULT_ZOOM);
                            }

                        }else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivty.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch (SecurityException e){}
    }

    private void moveCamera(LatLng latLng , float zoom){//focuse on the location
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ",lng: "+ latLng.latitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng , zoom));
    }


    private void  initMap(){
        Log.d(TAG ,"initMap: initializing map ");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivty.this);

    }

    private  void  getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location premission");
        String[]permission= {Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};//WE DONT NEED TO Check INTENET PORMISHIND ONLINE LOCATION PERMISSION

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                mLocationPramissionGranted = true;
                initMap();
            }else {
                ActivityCompat.requestPermissions(this,permission,LOCATION_PERMISSION_REQUEST_CODE);
            }

        }
        else {
            ActivityCompat.requestPermissions(this,permission,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {//IN CASE WE DONT HVAE A PRAMISSION
        Log.d(TAG, "onRequestPermissionsResult: called.");
       mLocationPramissionGranted = false;

       switch (requestCode){
           case LOCATION_PERMISSION_REQUEST_CODE:{
               if(grantResults.length>0){
                    for(int i=0 ; i < grantResults.length;i ++ ){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPramissionGranted = false;
                            Log.d(TAG,"onRequestPermissionsResult: permission failed");
                            return;

                        }
                    }
                   Log.d(TAG, "onRequestPermissionsResult: premission granted");
                   mLocationPramissionGranted = true;
                   initMap();

               }
           }
       }
    }


 }
