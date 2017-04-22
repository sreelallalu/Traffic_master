package appcmpt.example.Traffic.fragment;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;


import appcmpt.example.Traffic.n.MainDataBAse;
import appcmpt.example.Traffic.n.R;
import appcmpt.example.Traffic.n.profile;

public class DetailsFrag extends Fragment {
	ImageView qrimage;
	MainDataBAse db;
	TextView tvqr;
     Button send;
	Bitmap bitmap;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.aboutfragment, container, false);
		db = new MainDataBAse(getActivity());
		SharedPreferences editor = getActivity().getPreferences(1);
		tvqr = (TextView) view.findViewById(R.id.tvyourqrcode);
		qrimage = (ImageView) view.findViewById(R.id.qrCode);
		send = (Button) view.findViewById(R.id.viasend);
		profile p = db.getContact(1);
		String code = p.getQcode();
		tvqr.setText(code);
		if (code != null) {
			int qrCodeDimention = 500;

			QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(code, null,
					Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
					qrCodeDimention);

			try {
				bitmap = qrCodeEncoder.encodeAsBitmap();
				qrimage.setImageBitmap(bitmap);

			} catch (WriterException e) {
				e.printStackTrace();
			}




	}
		return view;


	}


}
