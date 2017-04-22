package appcmpt.example.Traffic.fragment;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;

import appcmpt.example.Traffic.n.DataUserAge;
import appcmpt.example.Traffic.n.R;

import static appcmpt.example.Traffic.n.NetworkChecker.isConnected;
import static appcmpt.example.Traffic.n.NetworkChecker.isConnectedMobile;
import static appcmpt.example.Traffic.n.NetworkChecker.isConnectedWifi;

//import com.example.androidqrcode.R;

public class ScanerFragment extends Fragment {
	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;

	private Button scanButton;
	private Fragment fragment = null;
	private ImageScanner scanner;
	private Context mcontext;
	private boolean barcodeScanned = false;
	private boolean previewing = true;
	private String qrcode;
	private JSONObject jsonObject;

	private final static String USER_AGENT = "Mozilla/5.0";
	static {
		System.loadLibrary("iconv");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.scanerfragment, container, false);

		// Toast.makeText(getActivity(),mCamera.toString(),Toast.LENGTH_LONG).show();
		final FrameLayout preview = (FrameLayout) rootView
				.findViewById(R.id.camerapreview);

		preview.setVisibility(View.INVISIBLE);

		scanButton = (Button) rootView
				.findViewById(R.id.btscan);

		scanButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
            //  mPreview.clearFocus();
				if(isConnectedWifi(getActivity())||isConnectedMobile(getActivity()))
				{
					if(isConnected(getActivity())){

				mCamera = getCameraInstance();
				autoFocusHandler = new Handler();
				scanner = new ImageScanner();
				scanner.setConfig(0, Config.X_DENSITY, 3);
				scanner.setConfig(0, Config.Y_DENSITY, 3);
				mPreview = new CameraPreview(getActivity(), mCamera, previewCb,
						autoFocusCB);

				preview.addView(mPreview);

				preview.setVisibility(View.VISIBLE);

				if (barcodeScanned) {

					preview.addView(mPreview);
					barcodeScanned = false;

					mCamera.setPreviewCallback(previewCb);
					mCamera.startPreview();
					previewing = true;
					mCamera.autoFocus(autoFocusCB);

				}
			}else{
						Toast.makeText(getActivity(),"no connection",Toast.LENGTH_LONG).show();
					}

				}else{
					Toast.makeText(getActivity(),"turn on wifi",Toast.LENGTH_LONG).show();
				}

			}
		});
		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		/* Instance barcode scanner */

	}

	public void onPause() {
		super.onPause();
		releaseCamera();
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {
		}
		return c;
	}

	private void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (previewing)
				mCamera.autoFocus(autoFocusCB);
		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();

			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(data);

			int result = scanner.scanImage(barcode);

			if (result != 0) {
				previewing = false;
				mCamera.setPreviewCallback(previewCb);
				mCamera.stopPreview();

				SymbolSet syms = scanner.getResults();
				for (Symbol sym : syms) {
					qrcode = sym.getData().toString();

					barcodeScanned = true;
					Vibrator v = (Vibrator) getActivity().getSystemService(
							Context.VIBRATOR_SERVICE);
					// Vibrate for 500 milliseconds
					v.vibrate(500);

					CheckScan();
				}
			}
		}
	};

	private void CheckScan() {
		HashMap<String,String>hashmap=new HashMap<>();
		hashmap.put("tag","scan");
		hashmap.put("code",qrcode);

		PostResponseAsyncTask task=new PostResponseAsyncTask(getActivity(), hashmap, new AsyncResponse() {
			@Override
			public void processFinish(String s) {
				Log.e("response",s);
				try{
					JSONObject hj=new JSONObject(s);
					int suc=hj.getInt("success");
					if(suc==1)
					{

						DataUserAge.getInstance().setDistributor_id(s);
						JSONArray er=hj.getJSONArray("Scan_Details");
						for(int i=0;i<er.length();i++)
						{
							JSONObject ko=er.getJSONObject(i);
							String id=ko.getString("id");
							String Driver=ko.getString("Driver Name");
							String phone=ko.getString("phone no");
							String vehicleno=ko.getString("Vehicle_no");
							String rate=ko.getString("Rate");
							String type=ko.getString("Vehicle_type");

						}


						Thread.sleep(2000);

						fragment = new DriverDetails();
						getActivity().getSupportFragmentManager().beginTransaction()
								.replace(R.id.frame_container, fragment).commit();

					}
					else{
						Toast.makeText(mcontext, "something went wrong", Toast.LENGTH_SHORT).show();
					}


				}catch (Exception e){e.printStackTrace();}

				/*if (result.getInt("success") == 1) {
					try {
						JSONObject qrjson = result.getJSONObject("user");
						String qrjstring = qrjson.toString();

						if (editor != null) {
							editor.putString("qrjson", qrjstring);
							editor.commit();
							qrjstring = "";
						}

						Thread.sleep(2000);
						fragment = new DriverDetails();
					} catch (Exception e) {
						e.printStackTrace();
					}
					getActivity().getSupportFragmentManager().beginTransaction()
							.replace(R.id.frame_container, fragment).commit();
				}*/
			}
		});
		task.execute("http://taxi.venturesoftwares.org/index.php");
		task.setEachExceptionsHandler(new EachExceptionsHandler() {
			@Override
			public void handleIOException(IOException e) {
				e.printStackTrace();
			}

			@Override
			public void handleMalformedURLException(MalformedURLException e) {
				e.printStackTrace();

			}

			@Override
			public void handleProtocolException(ProtocolException e) {
				e.printStackTrace();

			}

			@Override
			public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
				e.printStackTrace();

			}
		});
	}

	// Mimic continuous auto-focusing
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};







}