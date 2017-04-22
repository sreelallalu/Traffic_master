package appcmpt.example.Traffic.n;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;

/**
 * Created by lalu on 4/7/2017.
 */
public class LoginPage extends AppCompatActivity {

    EditText vehino;
    EditText password;
    Button send;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        vehino=(EditText)findViewById(R.id.drivervehino);
        password=(EditText)findViewById(R.id.driverpass);
        send=(Button)findViewById(R.id.logindriver);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
    }

    private void Login() {

        String _vehi=vehino.getText().toString();
        String _pass=password.getText().toString();
        if(!_vehi.isEmpty()&&!_pass.isEmpty()) {
            HashMap<String, String> hashmap = new HashMap<>();
            hashmap.put("tag", "driverlogin");
            hashmap.put("vehicle_no", _vehi);
            hashmap.put("password", _pass);
            PostResponseAsyncTask task = new PostResponseAsyncTask(LoginPage.this, hashmap, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    Log.e("response", s);

                    try {
                        JSONObject jsonObject=new JSONObject(s);
                        int succ= 0;
                        succ = jsonObject.getInt("success");
                        if(succ==1)
                        {
                              JSONObject df=jsonObject.getJSONObject("Driver_details");

                            String userId=df.getString("id");
                            String qrcode=df.getString("qrcode");
                            ///driver

//user_id,driver
                            MainDataBAse db=new MainDataBAse(LoginPage.this);
                            db.open();
                            db.addContact(new profile(userId,qrcode,""+1));
                            db.close();
                            Intent i = new Intent(getApplicationContext(),
                                    MainActivity.class);
                            startActivity(i);
                            finish();

                        }
                        else{
                            Toast.makeText(LoginPage.this, "something went wrong", Toast.LENGTH_SHORT).show();
                        }




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }





                }
            });
            task.execute("http://taxi.venturesoftwares.org/index.php");
            task.setEachExceptionsHandler(new EachExceptionsHandler() {
                @Override
                public void handleIOException(IOException e) {
                    Toast.makeText(LoginPage.this, "something went wrong", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void handleMalformedURLException(MalformedURLException e) {
                    Toast.makeText(LoginPage.this, "something went wrong", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void handleProtocolException(ProtocolException e) {
                    Toast.makeText(LoginPage.this, "something went wrong", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                    Toast.makeText(LoginPage.this, "something went wrong", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
