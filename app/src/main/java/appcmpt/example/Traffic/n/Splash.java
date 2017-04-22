package appcmpt.example.Traffic.n;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class Splash extends ActionBarActivity {
	TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		android.support.v7.app.ActionBar a = getSupportActionBar();
		a.hide();
		textView=(TextView) findViewById(R.id.tvsplash);






		Thread timer = new Thread() {
			public void run() {
				try {
					sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					
					Intent i = new Intent(getApplicationContext(),
							Register.class);

					startActivity(i);

				}
			}
		};
		timer.start();

	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}
