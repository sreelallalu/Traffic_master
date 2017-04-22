package appcmpt.example.Traffic.n;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by lalu on 4/12/2017.
 */
public class ViewList extends android.support.v4.app.Fragment {

    ListView listView;
    List<Unsafe> listsafe;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.viewlisty, container, false);
        listView=(ListView)rootView.findViewById(R.id.listviewc);

        ApiCall();


    return rootView;
    }

    private void ApiCall() {
        HashMap<String,String> hashmap=new HashMap<>();
        hashmap.put("tag","View_unsafe");
        hashmap.put("status","1");
        PostResponseAsyncTask task=new PostResponseAsyncTask(getActivity(), hashmap, new AsyncResponse() {
            @Override
            public void processFinish(String s) {

                Log.e("response",s);
              try{
                JSONObject op=new JSONObject(s);
                int succ=op.getInt("success");
                if(succ==1)
                {
    listsafe=new ArrayList<>();
                    JSONArray are=op.getJSONArray("View_unsafe");
                    for(int i=0;i<are.length();i++)
                    {
                        JSONObject vb=are.getJSONObject(i);

                        String username=vb.getString("Username");
                        String vehino=vb.getString("VehicleNo");
                        String latlong=vb.getString("Latitude");
                        String date=vb.getString("date");
                        listsafe.add(new Unsafe(username,vehino,date,latlong));
                    }
                    ListAdapter adapter=new ListAdapter(getActivity(),listsafe);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new ITEm());


                }else{

                    Toast.makeText(getActivity(), "empty list", Toast.LENGTH_SHORT).show();
                }



              }catch (Exception e){e.printStackTrace();}













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



    private class ITEm implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
         clickposition(i);
        }
    }

    private void clickposition(int i) {
        try {
            String latlong = listsafe.get(i).getLatlong();
            String[] values = latlong.split(",");

            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Double.parseDouble(values[0]), Double.parseDouble(values[1]));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            getActivity().startActivity(intent);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
