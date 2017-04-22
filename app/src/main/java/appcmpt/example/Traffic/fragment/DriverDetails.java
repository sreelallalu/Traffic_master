package appcmpt.example.Traffic.fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;

import appcmpt.example.Traffic.n.DataUserAge;
import appcmpt.example.Traffic.n.R;
import appcmpt.example.Traffic.n.TAG;


public class DriverDetails extends Fragment {
	TextView username, usertype, vno, rent;
	Button go;
	Fragment fragment = null;
	int key = 0;
EditText reason,amount;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.driverdetls, container, false);
		Bundle bundle = getArguments();
		username = (TextView) rootView.findViewById(R.id.tvqrusername);
		usertype = (TextView) rootView.findViewById(R.id.tvqrusertype);
		vno = (TextView) rootView.findViewById(R.id.tvqrvno);
		rent = (TextView) rootView.findViewById(R.id.tvqrrate);
		go = (Button) rootView.findViewById(R.id.btstartj);
		reason=(EditText)rootView.findViewById(R.id.reason);
		amount=(EditText)rootView.findViewById(R.id.amount);

		go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try{
				SendValue();
				}catch (Exception e){

				}
				// TODO Auto-generated method stub


			}
		});
         String s= DataUserAge.getInstance().getDistributor_id();
		Log.e("driver_details",s);

		if (s != null) {

			try {


				JSONObject hj=new JSONObject(s);

					DataUserAge.getInstance().setDistributor_id(s);
					JSONArray er = hj.getJSONArray("Scan_Details");
					for (int i = 0; i < er.length(); i++) {
						JSONObject ko = er.getJSONObject(i);

						String Driver = ko.getString("Driver Name");
						String phone = ko.getString("phone no");
						String vehicleno = ko.getString("Vehicle_no");
						String rate = ko.getString("Rate");
						String type = ko.getString("Vehicle_type");

						username.setText(Driver);
						usertype.setText(type);
						vno.setText(vehicleno);
						rent.setText(rate);

					}







			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Toast.makeText(getActivity(), "no details", Toast.LENGTH_LONG)
					.show();
		}
		return rootView;

	}

	private void SendValue() throws Exception {
		String res=reason.getText().toString();
		String amo=amount.getText().toString();
		HashMap<String,String> hashmap=new HashMap<>();
		hashmap.put("tag", TAG.tagname);
		hashmap.put( TAG.reason,res);
		hashmap.put( TAG.amount,amo);
		hashmap.put( TAG.vehicleno,vno.getText().toString());

		PostResponseAsyncTask task=new PostResponseAsyncTask(getActivity(), hashmap, new AsyncResponse() {
			@Override
			public void processFinish(String s) {
				Log.e("success",s);

						Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();

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

}
