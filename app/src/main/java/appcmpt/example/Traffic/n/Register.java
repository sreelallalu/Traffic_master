package appcmpt.example.Traffic.n;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Register extends ActionBarActivity {
	private Spinner vetype;
	private EditText uname, vno,umob,uemail,us_pass;
	private Button register;
	private String st_username,st_mobile,st_email;
	private String st_vehnumber;
	private String st_vehicle_type;
	private String st_usertype = "Driver";
	private JSONObject jsonObject;
	private ImageView official, citizen;
	private TextView tvutype,dseCt;
	private GPSTracker gps;
	MainDataBAse db;
	private Double dbl_latitude, dbl_longitude;

	private final static String USER_AGENT = "Mozilla/5.0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		official = (ImageView) findViewById(R.id.ivofficila);
		citizen = (ImageView) findViewById(R.id.ivcitizen);
		tvutype = (TextView) findViewById(R.id.tvusertypeinreg);
		dseCt=(TextView)findViewById(R.id.dselect);
		gps = new GPSTracker(getApplicationContext());
		if (gps.canGetLocation()) {

			dbl_latitude = gps.getLatitude();
			dbl_longitude = gps.getLongitude();
			/*lattitude = String.valueOf(dbl_latitude);
			longitude = String.valueOf(dbl_longitude);*/

		} else {
			Toast.makeText(getApplicationContext(), "Please enable your GPS",
					Toast.LENGTH_LONG).show();
		}
		vetype = (Spinner) findViewById(R.id.sp_vtype_register);

		register = (Button) findViewById(R.id.bt_register_register);
		umob=(EditText)findViewById(R.id.et_uname_mobile);
		uemail=(EditText)findViewById(R.id.et_uname_email);
		uname = (EditText) findViewById(R.id.et_uname_register);
		vno = (EditText) findViewById(R.id.et_vno_register);
		us_pass = (EditText) findViewById(R.id.et_password);
		//st_usertype ="Driver";
		tvutype.setText("Driver");
		register.setText("Register");
		uemail.setVisibility(View.GONE);
		//us_pass.setVisibility(View.GONE);
		us_pass.setHint("driver password");
		List<String> spinnerArray_vetype = new ArrayList<String>();
		spinnerArray_vetype.add("CAR");
		spinnerArray_vetype.add("AUTO");

		ArrayAdapter<String> adapter_vetype = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spinnerArray_vetype);

		adapter_vetype
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		vetype.setAdapter(adapter_vetype);
		official.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tvutype.setText("Driver");
				st_usertype = "Driver";
				//register.setText("Register");
				vetype.setVisibility(View.VISIBLE);
				vno.setVisibility(View.VISIBLE);
				uemail.setVisibility(View.GONE);
				us_pass.setHint("driver password");
				//us_pass.setVisibility(View.GONE);
				dseCt.setVisibility(View.VISIBLE);
			}
		});
		citizen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tvutype.setText("Citizen");
				st_usertype = "Citizen";
				vetype.setVisibility(View.INVISIBLE);
				vno.setVisibility(View.INVISIBLE);
				uemail.setVisibility(View.VISIBLE);
				//us_pass.setVisibility(View.VISIBLE);
				us_pass.setHint("user password");
				dseCt.setVisibility(View.INVISIBLE);
			}
		});
		android.support.v7.app.ActionBar a = getSupportActionBar();
		a.hide();
		// finding widgets
	db = new MainDataBAse(this);



				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(i);
		     finish();


			register.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					if (st_usertype != "Citizen") {

						st_username = uname.getText().toString();
						st_mobile = umob.getText().toString();
						st_vehnumber = vno.getText().toString();
						st_vehicle_type = vetype.getSelectedItem().toString();

						String pass=us_pass.getText().toString();

						HashMap<String, String> hashMap = new HashMap<String, String>();
						hashMap.put("tag", "register");
						hashMap.put("username", st_username);
						hashMap.put("phone_dr", st_mobile);
						hashMap.put("vehicle_type", st_vehicle_type);
						hashMap.put("vehicle_no", st_vehnumber);
						hashMap.put("password", pass);


						//hashMap.put("");


						Networking(hashMap);
						//new CheckAvialability().execute();
					} else {
						st_username = uname.getText().toString();
						st_mobile = umob.getText().toString();
						st_email = uemail.getText().toString();
						String pass=us_pass.getText().toString();
						HashMap<String, String> hashMap = new HashMap<String, String>();
						hashMap.put("tag", "user_register");
						hashMap.put("name", st_username);
						hashMap.put("phone_no", st_mobile);
						hashMap.put("Location",""+dbl_latitude+","+dbl_longitude);
						hashMap.put("email", st_email);
						hashMap.put("password",pass);
						Networking(hashMap);
					/*final String POPUP_LOGIN_TITLE = "Add Buddy";
					final String POPUP_LOGIN_TEXT = "Add two mobile numbers for emergency alert";
					final String NUMBER1 = "--number1--";
					final String NUMBER2 = "--number2--";*/

				/*	AlertDialog.Builder alert = new AlertDialog.Builder(
							Register.this);

					alert.setTitle(POPUP_LOGIN_TITLE);
					alert.setMessage(POPUP_LOGIN_TEXT);

					// Set an EditText view to get user input
					final EditText email = new EditText(Register.this);
					email.setHint(NUMBER1);
					final EditText password = new EditText(Register.this);
					password.setHint(NUMBER2);
					LinearLayout layout = new LinearLayout(Register.this);
					layout.setOrientation(LinearLayout.VERTICAL);
					layout.addView(email);
					layout.addView(password);
					alert.setView(layout);

					alert.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (email.getText().toString() != null
											|| email.getText().toString() != ""
											&& password.getText().toString() != null
											|| password.getText().toString() != "") {
										SharedPreferences.Editor editor = getPreferences(
												MODE_PRIVATE).edit();
										editor.putBoolean("register", true);
										db.addContact(new profile(st_usertype,
												"0"));
										db.addContact(new profile("contact1",
												email.getText().toString()));
										db.addContact(new profile("contact2",
												password.getText().toString()));
										editor.commit();
										finish();
										Intent i = new Intent(
												getApplicationContext(),
												MainActivity.class);
										startActivity(i);
									} else {
										Toast.makeText(getApplicationContext(),
												"add two contacts",
												Toast.LENGTH_LONG).show();
									}
								}
							});

					alert.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Canceled.
								}
							});

					alert.show();*/

					}

				}

			});


		}

	private void Networking(HashMap<String, String> hashMap) {


		PostResponseAsyncTask task=new PostResponseAsyncTask(Register.this, hashMap, new AsyncResponse() {
			@Override
			public void processFinish(String s) {

				try {
					Log.e("response",s);
					JSONObject jsonObject=new JSONObject(s);
					int succ=jsonObject.getInt("success");
					if(succ==1)
					{
						int type=jsonObject.getInt("type");
						if(type==1)
						{


							SharedPreferences.Editor editor = getPreferences(
									MODE_PRIVATE).edit();
							editor.putBoolean("register", true);
							editor.putBoolean("typerr", false);
							editor.commit();
							//
							/*
							JSONObject jds=new JSONObject("driver_details");
							JSONObject fr=ui.getJSONObject(0);

							String userId=fr.getString("user_id");
							String qrcode=fr.getString("qrcode");
							///driver

//user_id,driver
							db.open();
							db.addContact(new profile(userId,""+type,qrcode));
							db.close();

							finish();*/
							Intent i = new Intent(getApplicationContext(),
									LoginPage.class);
							startActivity(i);
							finish();
						}
						else{

							SharedPreferences.Editor editor = getPreferences(
									MODE_PRIVATE).edit();
							editor.putBoolean("register", true);
							editor.putBoolean("typerr", true);
							JSONObject jh=jsonObject.getJSONObject("user_details");

							 String ids=jh.getString("id");


							db.open();
							db.addContact(new profile(ids,ids,""+0));
							db.close();
							editor.commit();
							finish();

							Intent i = new Intent(getApplicationContext(),
									MainActivity.class);
							startActivity(i);
						}




					}
					else

					{
						String msg=jsonObject.getString("error");

						/*error_msg*/
						Toast.makeText(Register.this, "error",
								Toast.LENGTH_LONG).show();

					}



				} catch (JSONException e) {
					e.printStackTrace();
				}


				Log.e("success",s.toString());

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


	/*class CheckAvialability extends AsyncTask<String, Void, JSONObject> {

		ProgressDialog p = new ProgressDialog(Register.this);

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

						Toast.makeText(Register.this, "Register complete",
								Toast.LENGTH_LONG).show();
						SharedPreferences.Editor editor = getPreferences(
								MODE_PRIVATE).edit();
						editor.putBoolean("register", true);
						db.addContact(new profile(st_usertype, result
								.getString("code")));
						editor.commit();
						finish();
						Intent i = new Intent(getApplicationContext(),
								MainActivity.class);
						startActivity(i);

					} else {

						Toast.makeText(Register.this,
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
				con.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				// add request header
				con.setDoOutput(true);
				// con.setRequestProperty("tag", "register");
				// con.setRequestProperty("username", st_username);
				// con.setRequestProperty("user_type", st_usertype);
				// con.setRequestProperty("vehicle_type", st_vehicle_type);
				// con.setRequestProperty("vehicle_no", st_vehnumber);
				String charset = "UTF-8";
				String s = "tag=" + URLEncoder.encode("register", charset);
				s += "&username=" + URLEncoder.encode(st_username, charset);
				s += "&user_type=" + URLEncoder.encode(st_usertype, charset);
				s += "&vehicle_type="
						+ URLEncoder.encode(st_vehicle_type, charset);
				s += "&vehicle_no=" + URLEncoder.encode(st_vehnumber, charset);

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

	Boolean exit = false;

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



}
