package appcmpt.example.Traffic.n;

import org.json.JSONObject;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import appcmpt.example.Traffic.fragment.ScanerFragment;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends ActionBarActivity {

	private ListView mDrawerList = null;
	private DrawerLayout mDrawerLayout = null;
	private ActionBarDrawerToggle mDrawerToggle ;
	private String mTitle = "";
	private MainDataBAse db;
	private String mDrawerTitle = "";
	@SuppressWarnings("unused")
	private TextView menuappname;
	private GPSTracker gps;
	private Toast trigger_toast;
	private String[] data_list = null;
	public static FragmentManager fragmentManager;
	private android.support.v7.app.ActionBar actionBar;
	private Fragment fragment = null;
	private android.support.v4.app.FragmentTransaction fragmentTransaction;
	private Boolean exit = false;
	private String utype = "";
	private String qrcode, lattitude, longitude;
	private JSONObject jsonObject;
private int type;
	private String _userId;
	private Double dbl_latitude, dbl_longitude;
	private final static String USER_AGENT = "Mozilla/5.0";

	@Override
	public void onBackPressed() {
		if (exit)
			System.exit(0);
		else {
			Toast.makeText(this, "Press Back again to Exit.",
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
//		gps = new GPSTracker(getApplicationContext());

		mDrawerList = (ListView) findViewById(R.id.drawer_list);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		db = new MainDataBAse(getApplicationContext());
		/*db.open();
		profile profile = db.getContact(1);
       db.close();*/
		// accessin Location with GPS




		//type=Integer.parseInt(utype);
       //Log.e("type",""+type);


			init1_User();
			if (savedInstanceState == null) {
				ClickPos(1);
//				Log.e("usertype",utype);
			}




		// Toast.makeText(getApplicationContext(),,Toast.LENGTH_LONG).show();

	}



	private void init1_User() {
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		actionBar = getSupportActionBar();
		// Initialize Listview

		// Initialize drawer layout


		MenuComp menu_data[] = new MenuComp[] {
				new MenuComp(R.drawable.qrscanicon, "Scan"),
				new MenuComp(R.drawable.distress, "Distress"),
				new MenuComp(R.drawable.qrcloseicon, "Exit") };

		MenuAdapter adapter = new MenuAdapter(this, R.layout.listview_item_row,
				menu_data);

		View header = (View) getLayoutInflater().inflate(
				R.layout.listview_header_row, null);
		mDrawerList.addHeaderView(header);

		mDrawerList.setAdapter(adapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		// Initializing title
		mTitle = getResources().getString(R.string.app_name);
		mDrawerTitle = getResources().getString(R.string.drawer_open);

		actionbarToggleHandler();
		fragment = new ScanerFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.frame_container, fragment).commit();
	}

	@SuppressLint("NewApi")
	private void actionbarToggleHandler() {
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			@Override
			public void onDrawerClosed(View drawerView) {
				actionBar.setTitle(mTitle);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				actionBar.setTitle(mDrawerTitle);
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	class DrawerItemClickListener implements OnItemClickListener {
		LayoutInflater li = getLayoutInflater();
		// Getting the View object as defined in the customtoast.xml file
		View layout = li.inflate(R.layout.customtoast_layt,
				(ViewGroup) findViewById(R.id.custom_toast_layout));

		@Override
		public void onItemClick(AdapterView adapter, View view, int position,
								long id) {


			ClickPos(position);

		}

	}	private void ClickPos(int position) {

			switch (position) {
				case 1:
					fragment = new ScanerFragment();
					break;
				case 2:
					fragment = new ViewList();
					break;
				case 3:
					mDrawerLayout.closeDrawer(mDrawerList);
					AlertDialog.Builder mbBuilder = new AlertDialog.Builder(
							MainActivity.this);
					mbBuilder.setMessage("Do yo want to quit?");
					mbBuilder.setPositiveButton("Yes",
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
									finish();
								}
							});
					mbBuilder.setNegativeButton("No",
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
													int which) {
									// TODO Auto-generated method stub

								}
							});
					AlertDialog quuit_dilg = mbBuilder.create();
					quuit_dilg.show();

					break;
				default:
					return;
			}






		getSupportFragmentManager().beginTransaction()
				.replace(R.id.frame_container, fragment).commit();

		mDrawerLayout.closeDrawer(mDrawerList);
	}



	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	mDrawerToggle.syncState();
	}

	/*class CheckAvialability extends AsyncTask<String, Void, JSONObject> {

		ProgressDialog p = new ProgressDialog(MainActivity.this);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			super.onPreExecute();
			p.setCancelable(false);
			p.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub

			jsonObject = getJSONFromUrl("http://taxi.venturesoftwares.org");
			if (jsonObject != null) {
				try {
					Log.d("json values",
							"json" + jsonObject.getString("success"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return jsonObject;
		}

		@Override
		protected void onPostExecute(JSONObject result) {

			p.dismiss();
			if (result != null) {
				try {
					if (result.getInt("success") == 1) {
						trigger_toast.show();

						ColorDrawable colorDrawable = new ColorDrawable(
								Color.parseColor("#F54542"));
						actionBar.setBackgroundDrawable(colorDrawable);
					} else {

						Toast.makeText(MainActivity.this,
								result.getString("error_msg"),
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Error in registration", Toast.LENGTH_LONG).show();
			}

			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		public JSONObject getJSONFromUrl(String url) {

			InputStream is = null;
			JSONObject jObj = null;
			String json = "";
			URL obj;
			HttpURLConnection con = null;

			// Making HTTP request
			try {
				System.out.println("url" + url);

				obj = new URL(url);
				con = (HttpURLConnection) obj.openConnection();

				// optional default is GET
				con.setRequestMethod("POST");
				con.setDoOutput(true);
				con.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				// add request header
				// con.setRequestProperty("User-Agent", USER_AGENT);
				// con.setRequestProperty("Accept", "application/json");
				// con.setRequestProperty("tag", "trigger");
				// con.setRequestProperty("code", qrcode);
				// con.setRequestProperty("lat", lattitude);
				// con.setRequestProperty("long", longitude);
				String charset = "UTF-8";
				String s = "tag=" + URLEncoder.encode("trigger", charset);
				s += "&code=" + URLEncoder.encode(qrcode, charset);
				s += "&lat=" + URLEncoder.encode(lattitude, charset);
				s += "&long=" + URLEncoder.encode(longitude, charset);

				con.setFixedLengthStreamingMode(s.getBytes().length);
				PrintWriter out = new PrintWriter(con.getOutputStream());
				out.print(s);
				out.close();
				int responseCode = con.getResponseCode();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				// print result

				json = response.toString();
				Log.e("JSON", json);
			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}

			// try parse the string to a JSON object
			try {
				jObj = new JSONObject(json);
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}

			// return JSON String
			return jObj;

		}
	}*/

}
