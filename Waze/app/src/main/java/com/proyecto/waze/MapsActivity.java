package com.proyecto.waze;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISOS_GPS = 0;
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

    List<Ruta> RutasResult;
    Gson gson = new Gson();
    String urlRequest;
    String rutasJSON;
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISOS_GPS);
            return;
        }
        else{
            this.setUbicacion();
        }

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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISOS_GPS);
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

        Toast.makeText(this, "Cantidad de Rutas alternativas: "+ rutas.size(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Rutas: " + gson.toJson(rutas), Toast.LENGTH_LONG).show();
        rutasJSON = gson.toJson(rutas);
        urlRequest = Variables.getURLBase();
        new ServerSendTask().execute();

//        DESCOMENTAR PARA QUE SE PINTE LA RUTA EN EL MAPA
//        for (Ruta ruta : RutasResult) {   
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ruta.getStartLocation(), 16));
////            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
////            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);
//
//            originMarkers.add(mMap.addMarker(new MarkerOptions()
////                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
//                    .title(ruta.getStartAddress())
//                    .position(ruta.getStartLocation())));
//
//            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
////                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
//                    .title(ruta.getEndAddress())
//                    .position(ruta.getEndLocation())));
//
//            PolylineOptions polylineOptions = new PolylineOptions().
//                    geodesic(true).
//                    color(Color.BLUE).
//                    width(10);
//
//            for (int i = 0; i < ruta.getPoints().size(); i++) {
//                polylineOptions.add(ruta.getPoints().get(i));
//            }
//
//            polylinePaths.add(mMap.addPolyline(polylineOptions));
//        }
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
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISOS_GPS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setUbicacion();
                } else {
                    Toast.makeText(getApplicationContext(),"Error de permisos, saliendo de la aplicacion", Toast.LENGTH_LONG).show();
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                    this.finish();
                    return;
                }
            }
        }
    }


    public class ServerSendTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String result) {
            if(result == null){
                Toast.makeText(MapsActivity.this, "No hay rutas :(", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MapsActivity.this, "Rutas llegando...", Toast.LENGTH_SHORT).show();
                Toast.makeText(MapsActivity.this, "Rutas: " + result, Toast.LENGTH_LONG).show();

//                aqui parsear el List<Ruta> que viene del server
//                RutasResult = gson.fromJson(result,Ruta[].class);

            }
        }

        @Override
        protected String doInBackground(String... params) {

//            JSONObject postDataParams = new JSONObject();
//            postDataParams.put("name", "abc");
//            postDataParams.put("email", "abc@gmail.com");
//            Log.e("params",postDataParams.toString());
            try {
                URL url = new URL(urlRequest); // here is your URL path
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(rutasJSON);

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
                catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }


//            try {
//                URL url = new URL(urlRequest);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setDoInput(true);
//                connection.setRequestMethod("POST");
//                connection.connect();
//
//                BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String valueResult = bf.readLine();
//                System.out.println("Result: " + valueResult);
////                result = valueResult;
//                return valueResult;
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return null;
        }
    }

}
