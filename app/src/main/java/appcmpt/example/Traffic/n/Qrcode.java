package appcmpt.example.Traffic.n;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import appcmpt.example.Traffic.fragment.Contents;
import appcmpt.example.Traffic.fragment.QRCodeEncoder;


/**
 * Created by SLR on 4/20/2016.
 */
public class Qrcode extends ActionBarActivity {

    ImageView qrimage;
    MainDataBAse db;
    TextView tvqr;
    Button send;
    Bitmap bitmap;
    private ListView listView;
    private Button btnShare;
    File file;
    Uri uri;
    private java.util.List<ResolveInfo> listApp;

    private final int WHAT_SHOW_SHARE_APP = 101;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_SHOW_SHARE_APP:
                    listView.setAdapter(new MyAdapter());
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.listsharetest);
        db = new MainDataBAse(Qrcode.this);
        SharedPreferences editor = this.getPreferences(1);
        tvqr = (TextView) findViewById(R.id.tred);
        qrimage = (ImageView) findViewById(R.id.qrCode);
        db.open();

        profile p = db.getContact(1);
        db.close();
        String code = p.getQcode();
        Log.e("code",code);
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


            listView = (ListView) findViewById(R.id.listView);
            btnShare = (Button) findViewById(R.id.btn_share);

            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnShare.setEnabled(false);
                    String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                            "/Qrcode_Fast";
                    File dir = new File(file_path);
                    if(!dir.exists())
                        dir.mkdirs();

                    String format = new SimpleDateFormat("yyyyMMddHHmmss",
                            java.util.Locale.getDefault()).format(new Date());

                     file = new File(dir, format + ".jpg");
                    FileOutputStream fOut;
                    try {
                        fOut = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    listApp = showAllShareApp();
                    if (listApp != null) {
                        listView.setAdapter(new MyAdapter());
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                share(listApp.get(position));
                            }
                        });
                    }
                }
            });

        }}

    private void share(ResolveInfo appInfo) {
        Intent sendIntent = new Intent();
         uri = Uri.fromFile(file);
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);

        if (appInfo != null) {
            sendIntent
                    .setComponent(new ComponentName(
                            appInfo.activityInfo.packageName,
                            appInfo.activityInfo.name));
        }
        sendIntent.setType("text/plain");
//        startActivity(Intent.createChooser(sendIntent, "Share"));
        startActivity(sendIntent);
    }

    private java.util.List<ResolveInfo> showAllShareApp() {
        java.util.List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        //Drawable rt=getResources().getDrawable(R.drawable.citizen);
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        intent.setType("image/jpeg");
        PackageManager pManager = getPackageManager();
        mApps = pManager.queryIntentActivities(intent,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        return mApps;
    }

    class MyAdapter extends BaseAdapter {

        PackageManager pm;
        public MyAdapter(){
            pm=getPackageManager();
        }


        @Override
        public int getCount() {
            return listApp.size();
        }

        @Override
        public Object getItem(int position) {
            return listApp.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(Qrcode.this).inflate(R.layout.listshare, parent, false);
                holder.ivLogo = (ImageView) convertView.findViewById(R.id.iv_logo);
                holder.tvAppName = (TextView) convertView.findViewById(R.id.tv_app_name);
                holder.tvPackageName = (TextView) convertView.findViewById(R.id.tv_app_package_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ResolveInfo appInfo = listApp.get(position);
            holder.ivLogo.setImageDrawable(appInfo.loadIcon(pm));
            holder.tvAppName.setText(appInfo.loadLabel(pm));
            holder.tvPackageName.setText(appInfo.activityInfo.packageName);

            return convertView;
        }
        }




    static class ViewHolder {
        ImageView ivLogo;
        TextView tvAppName;
        TextView tvPackageName;
    }

}
