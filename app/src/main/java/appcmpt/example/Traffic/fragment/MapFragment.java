package appcmpt.example.Traffic.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import appcmpt.example.Traffic.n.DataUserAge;
import appcmpt.example.Traffic.n.DirectionsJSONParser;
import appcmpt.example.Traffic.n.GPSTracker;
import appcmpt.example.Traffic.n.R;

import static appcmpt.example.Traffic.n.NetworkChecker.isConnected;
import static appcmpt.example.Traffic.n.NetworkChecker.isConnectedMobile;
import static appcmpt.example.Traffic.n.NetworkChecker.isConnectedWifi;

public class MapFragment extends Fragment implements GoogleMap.OnMyLocationButtonClickListener,ActivityCompat.OnRequestPermissionsResultCallback
		{
	private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
	private GoogleMap map;

	private Button waypoint,ClearAll;
	TextView km, fare;

	private ArrayList<LatLng> markerPoints;
	EditText etsearchplace;
	ImageView ivsearch;
	GPSTracker gps;
	Double latitude, longitude;
	String rate="20";
			String driverId="34";



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.mapfragment, container, false);
		initilizeMap();

		gps = new GPSTracker(getActivity());
		markerPoints = new ArrayList<LatLng>();


		// and next place it, for exemple, on bottom right (as Google Maps app)


		km = (TextView) view.findViewById(R.id.tvkilo);
		fare = (TextView) view.findViewById(R.id.tvfare);
		etsearchplace = (EditText) view.findViewById(R.id.etsearchplace);
		ivsearch = (ImageView) view.findViewById(R.id.btimagesrch);
        ClearAll=(Button)view.findViewById(R.id.clearall);


		map.setOnMyLocationButtonClickListener(this);
     ClearAll.setOnClickListener(new OnClickListener() {
	@Override
	public void onClick(View v) {

		km.setText("");
		fare.setText("");
		map.clear();

		// Removes all the points in the ArrayList
		markerPoints.clear();

	}
});
		ourlocation();
		View btnMyLocat = ((View) view.findViewById(1).getParent()).findViewById(2);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(80,80); // size of button in dp
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

		params.setMargins(0, 0, 0, 0);
		btnMyLocat.setLayoutParams(params);
		String s= DataUserAge.getInstance().getDistributor_id();
		if (s != null) {


			try {



				JSONObject hj=new JSONObject(s);

				DataUserAge.getInstance().setDistributor_id(s);
				JSONArray er = hj.getJSONArray("Scan_Details");
				for (int i = 0; i < er.length(); i++) {
					JSONObject ko = er.getJSONObject(i);
					driverId= ko.getString("id");

					rate= ko.getString("Rate");



				}


				// Toast.LENGTH_LONG).show();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}




		ivsearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String location = etsearchplace.getText().toString();

				if (location != null && !location.equals("")) {
					new GeocoderTask().execute(location);
				}
			}
		});
		waypoint = (Button) view.findViewById(R.id.button2);
		waypoint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
               if(isConnectedWifi(getActivity())||isConnectedMobile(getActivity()))
			   {
				   if(isConnected(getActivity())){
				if (markerPoints.size() >= 2) {
					final LatLng origin = markerPoints.get(0);
					final LatLng dest = markerPoints.get(1);

					// Getting URL to the Google Directions API
					String url = getDirectionsUrl(origin, dest);

					DownloadTask downloadTask = new DownloadTask();

					// Start downloading json data from Google Directions API
					downloadTask.execute(url);
					Location mylocation = new Location("");
					Location dest_location = new Location("");
					double lat = dest.latitude;
					double lon = dest.longitude;
					dest_location.setLatitude(lat);
					dest_location.setLongitude(lon);

					mylocation.setLatitude(origin.latitude);
					mylocation.setLongitude(origin.longitude);
					final int distance = (int) mylocation.distanceTo(dest_location) / 1000;

					km.setText(Integer.toString(distance) + "km");
					if (rate != null) {
						final int farebydist = distance * Integer.parseInt(rate) + 20;
						fare.setText(String.valueOf(farebydist));
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								SendServerJourney(farebydist,distance,dest.latitude,dest.longitude,origin.latitude,origin.longitude);

							}
						},500);
					} else {
						final int farebydist = distance * 20 + 20;
						fare.setText(String.valueOf(farebydist));

						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								SendServerJourney(farebydist,distance,dest.latitude,dest.longitude,origin.latitude,origin.longitude);

							}
						},500);

					}

				}}
				   else{
					   Toast.makeText(getActivity(),"no connection",Toast.LENGTH_LONG).show();
				   }
			}

			else{
				   Toast.makeText(getActivity(),"turn on wifi",Toast.LENGTH_LONG).show();
			   }
			}
		});

		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng point) {

				if (markerPoints.size() >= 2) {
					return;
				}

				// Adding new item to the ArrayList
				markerPoints.add(point);

				// Creating MarkerOptions
				MarkerOptions options = new MarkerOptions();

				// Setting the position of the marker
				options.position(point);
				options.snippet("Home");
				options.title("Destitantion");

				/**
				 * For the start location, the color of marker is GREEN and for
				 * the end location, the color of marker is RED and for the rest
				 * of markers, the color is AZURE
				 */
				if (markerPoints.size() == 1) {
					options.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
				} else if (markerPoints.size() == 2) {
					options.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
				}

				// Add new marker to the Google Map Android API V2
				map.addMarker(options);
			}
		});

		map.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng point) {
				// Removes all the points from Google Map
				map.clear();

				// Removes all the points in the ArrayList
				markerPoints.clear();
			}
		});
		return view;

	}

			private void SendServerJourney(int farebydist, int distance, double latitudedes, double longitudedes, double latitude1star, double longitude1start) {


				      HashMap<String,String> hasmap=new HashMap<>();
				            hasmap.put("tag","sendJourney");
				            hasmap.put("fare", String.valueOf(farebydist));
				            hasmap.put("distance", String.valueOf(distance));
				            hasmap.put("lat_destination", ""+latitudedes+","+longitudedes);
				            hasmap.put("lat_start", ""+latitude1star+","+longitude1start);
				            hasmap.put("driverid", driverId);




					PostResponseAsyncTask task=new PostResponseAsyncTask(getActivity(), hasmap, new AsyncResponse() {
					@Override
					public void processFinish(String s) {
Log.e("response",s);

						try{

							JSONObject df=new JSONObject(s);
							int sdd=df.getInt("success");
							if(sdd==1)
							{
								Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
							}

						}catch (Exception e)
						{e.printStackTrace();}



					}
				});
				task.execute("http://taxi.venturesoftwares.org/index.php");
				task.setEachExceptionsHandler(new EachExceptionsHandler() {
					@Override
					public void handleIOException(IOException e) {

					}

					@Override
					public void handleMalformedURLException(MalformedURLException e) {

					}

					@Override
					public void handleProtocolException(ProtocolException e) {

					}

					@Override
					public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {

					}
				});




			}

			private void ourlocation() {
		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {
			// Permission to access the location is missing.
			PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
					Manifest.permission.ACCESS_FINE_LOCATION, true);

		} else if (map != null) {
			// Access to the location has been granted to the app.
			map.setMyLocationEnabled(true);
		}

			longitude = gps.getLongitude();if (gps.canGetLocation()) {
				latitude = gps.getLatitude();
			MarkerOptions marker = new MarkerOptions().position(
					new LatLng(latitude, longitude)).title("Me");
			markerPoints.add(new LatLng(latitude, longitude));
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(latitude, longitude)).zoom(12).build();

			map.addMarker(marker);
			map.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
		}
		/*


			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(latitude, longitude)).zoom(12).build();

			MarkerOptions marker = new MarkerOptions().position(
					new LatLng(latitude, longitude)).title("Me");
			markerPoints.add(new LatLng(latitude, longitude));

			map.addMarker(marker);
			map.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
		}*/

	}


	public boolean onMyLocationButtonClick() {
		ourlocation();
		Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();

		// Return false so that we don't consume the event and the default behavior still occurs
		// (the camera animates to the user's current position).
		return false;
	}

	Boolean exit = false;

	public void onBackPressed() {
		if (exit)
			System.exit(0);
		else {
			Toast.makeText(getActivity(), "Press Back again to Exit.",
					Toast.LENGTH_SHORT).show();
			exit = true;
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					exit = false;
				}
			}, 3 * 1000);

		}

	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=true";

		// Waypoints
		String waypoints = "";
		for (int i = 2; i < markerPoints.size(); i++) {
			LatLng point = (LatLng) markerPoints.get(i);
			if (i == 2)
				waypoints = "waypoints=";
			waypoints += point.latitude + "," + point.longitude + "|";
		}

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor + "&"
				+ waypoints;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;

		return url;
	}

	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception w", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	private void initilizeMap() {
		if (map == null) {
			map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                    .getMap();
		//	map.setMyLocationEnabled(true);
			// check if map is created successfully or not
			if (map == null) {
				Toast.makeText(getActivity(), "Sorry! unable to create maps",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service

			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();
			if (result != null) {
				// Invokes the thread for parsing the JSON data
				parserTask.execute(result);
			}
		}
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {

			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;

			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(6);
				lineOptions.color(Color.BLUE);
			}

			// Drawing polyline in the Google Map for the i-th route
			map.addPolyline(lineOptions);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		initilizeMap();
	}

	// class for getting user specified locations

	// An AsyncTask class for accessing the GeoCoding Web Service
	private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

		@Override
		protected List<Address> doInBackground(String... locationName) {
			// Creating an instance of Geocoder class
			Geocoder geocoder = new Geocoder(getActivity());
			List<Address> addresses = null;

			try {
				// Getting a maximum of 3 Address that matches the input text
				addresses = geocoder.getFromLocationName(locationName[0], 3);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return addresses;
		}

		@Override
		protected void onPostExecute(List<Address> addresses) {

			if (addresses == null || addresses.size() == 0) {
				Toast.makeText(getActivity(), "No Location found",
						Toast.LENGTH_SHORT).show();
			} else {

				// Clears all the existing markers on the map
				// map.clear();

				// Adding Markers on Google Map for each matching address
				for (int i = 0; i < addresses.size(); i++) {

					Address address = (Address) addresses.get(i);
					LatLng latLng;
					MarkerOptions markerOptions;
					// Creating an instance of GeoPoint, to display in Google
					// Map
					latLng = new LatLng(address.getLatitude(),
							address.getLongitude());

					String addressText = String.format(
							"%s, %s",
							address.getMaxAddressLineIndex() > 0 ? address
									.getAddressLine(0) : "", address
									.getCountryName());

					markerOptions = new MarkerOptions();
					markerOptions.position(latLng);
					markerOptions.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
					markerOptions.title(addressText);
					markerOptions.snippet("Destination");
					markerPoints.add(latLng);
					map.addMarker(markerOptions);

					// Locate the first location
					if (i == 0)
						map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
				}
			}
		}
	}

}