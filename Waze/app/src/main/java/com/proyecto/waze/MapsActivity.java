package com.proyecto.waze;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISOS_LOCALIZACION = 0;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private Marker marker;
    String origen;
    EditText destino;
    Button crearRutaBtn;
    ProgressDialog progressDialogUbicacion;
    ProgressDialog progressDialogRuta;
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        destino = (EditText) findViewById(R.id.destino);
        crearRutaBtn = (Button) findViewById(R.id.buscarRutaBtn);

        this.setUbicacion();

        crearRutaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MapsActivity.this, "Calculando ruta...", Toast.LENGTH_SHORT).show();
                enviarDestino();
            }
        });




    }

    private void setUbicacion(){
        //get the location service
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "Por favor activa los permisos de la App y reinicia! ;)", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialogUbicacion = ProgressDialog.show(this, "Por favor espere...","Buscando su ubicación...!", true);
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude,longitude);
                    Geocoder geocoder =  new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude,longitude,1);
                        origen = latitude + "," + longitude;
                        String str = addressList.get(0).getAddressLine(0) + ", ";
                        str += addressList.get(0).getCountryName();
                        // Marcar la localizacion
                        if(marker == null){
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                            progressDialogUbicacion.dismiss();
                        }
//                        else{
//                            marker.remove();
//                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(str));
//                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
            });
        }
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude,longitude);
                    Geocoder geocoder =  new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude,longitude,1);
                        origen = latitude + "," + longitude;
                        String str = addressList.get(0).getAddressLine(0) + ", ";
                        str += addressList.get(0).getCountryName();
                        // Marcar la localizacion
                        if(marker == null){
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                            progressDialogUbicacion.dismiss();
                        }
//                        else{
//                            marker.remove();
//                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(str));
//                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
            });
        }
    }

    private void enviarDestino(){
        String lugarDestino = destino.getText().toString();
        if (lugarDestino.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese destino!", Toast.LENGTH_SHORT).show();
            return;
        }
//        Toast.makeText(this, "Origen: "+ origen + "| Destino: " + lugarDestino, Toast.LENGTH_SHORT).show();

        try {
            new Router(this,origen,lugarDestino).iniciar();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void startRutaProcess(){
        progressDialogRuta = ProgressDialog.show(this, "Por favor espere.","Calculando ruta...!", true);

        if(marker != null){
            marker.remove();
        }

        if (originMarkers != null) {
            for (Marker mker : originMarkers) {
                mker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker mker : destinationMarkers) {
                mker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    public void endRutaProcess(List<Ruta> rutas){
        progressDialogRuta.dismiss();
//        polylinePaths = new ArrayList<>();
//        originMarkers = new ArrayList<>();
//        destinationMarkers = new ArrayList<>();

        for (Ruta ruta : rutas) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ruta.getStartLocation(), 16));
//            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
//            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(ruta.getStartAddress())
                    .position(ruta.getStartLocation())));

            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(ruta.getEndAddress())
                    .position(ruta.getEndLocation())));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < ruta.getPoints().size(); i++) {
                polylineOptions.add(ruta.getPoints().get(i));
            }

            polylinePaths.add(mMap.addPolyline(polylineOptions));
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

//        LatLng una = new LatLng(9.998749, -84.111347);
//        mMap.addMarker(new MarkerOptions().position(una).title("Universidad Nacional"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(una, 18));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "Sin permisos!", Toast.LENGTH_SHORT).show();
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
}
