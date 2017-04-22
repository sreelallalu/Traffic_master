package appcmpt.example.Traffic.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import appcmpt.example.Traffic.n.R;


public class penaltydetails  extends Fragment{
	TextView username,usertype,vno,rent;
	Button go;
	Fragment fragment=null;
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		 View rootView = inflater.inflate(
					R.layout.pen_driver_dtls, container, false);

		 fragment = new MapFragment();
		 go.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		            alert.setTitle("Penalty"); 
		            alert.setMessage("Enter the Amount");

		            final EditText input = new EditText(getActivity());
		            alert.setView(input);

		            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {
		                  // String srt = input.getEditableText().toString();
		                   Toast.makeText(getActivity(),"Penalty Added",Toast.LENGTH_LONG).show();                
		            } 
		        }); 
		            alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
		              public void onClick(DialogInterface dialog, int whichButton) {
		                
		                  dialog.cancel();
		              }
		        }); 
		            AlertDialog alertDialog = alert.create();
		            alertDialog.show();
		             

			}
		});
		 
		 SharedPreferences editor = getActivity().getPreferences(0);
		 String qrjson = editor.getString("qrjson",null);
		 if(qrjson!=null){
		 try {
			JSONObject qj=new JSONObject(qrjson);
			username.setText(qj.getString("username"));
			usertype.setText(qj.getString("user_type"));
			vno.setText(qj.getString("vehicle_no"));
			rent.setText(qj.getString("rate"));
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 }else{
			 Toast.makeText(getActivity(),"json null",Toast.LENGTH_LONG).show();
		 }
	        return rootView;
	        
	    }
	

}
