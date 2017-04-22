package appcmpt.example.Traffic.n;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GetStarted extends ActionBarActivity {
	Button register, skip;
	SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getstarted);
		android.support.v7.app.ActionBar a = getSupportActionBar();
		a.hide();
		register = (Button) findViewById(R.id.bt_register_getstart);
		skip = (Button) findViewById(R.id.bt_skip_getstart);
		skip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				preferences = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("skip", "true");
				editor.commit();
				startActivity(new Intent(getApplicationContext(),
						MainActivity.class));
				finish();
			}
		});
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(),
						Register.class));
				finish();
			}
		});
	}

}
