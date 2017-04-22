package appcmpt.example.Traffic.n;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lalu on 3/19/2017.
 */
public class ValidPoint extends android.support.v4.app.Fragment {
  ListView parkingpont;
    MainDataBAse db;
    String userId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.pen_driver_dtls, container, false);
        parkingpont=(ListView)rootView.findViewById(R.id.listitems) ;
        db = new MainDataBAse(getActivity());
        db.open();
        profile profile = db.getContact(1);
        userId=profile.getUser_id();
        db.close();
        NetSet();


        return rootView;
    }

    private void NetSet() {
        HashMap<String,String> hashmap=new HashMap<>();
        hashmap.put("tag","view_penality");
        hashmap.put("id_driver",userId);
        PostResponseAsyncTask task=new PostResponseAsyncTask(getActivity(), hashmap, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.e("result",s);
                try {
                    JSONObject re=new JSONObject(s);
                    int suc=re.getInt("success");
                    if(suc==1)
                    {
                        List<DriverDetails> list=new ArrayList<>();
                        JSONArray json=re.getJSONArray("driver_details");
                        for(int i=0;i<json.length();i++)
                        {
                            DriverDetails df=new DriverDetails();

                            JSONObject f=json.getJSONObject(i);
                            String reason=f.getString("penality reason");
                            String amount=f.getString("amount");
                            String date=f.getString("Date Of Penality");
                            df.setAmount(amount);
                            df.setReason(reason);
                            df.setDate(reason);
                            list.add(df);
                        }
                       NavDrawerListAdapter adapter=new NavDrawerListAdapter(getActivity(),list);
                             parkingpont.setAdapter(adapter);


                    }else{
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        task.execute("");
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
