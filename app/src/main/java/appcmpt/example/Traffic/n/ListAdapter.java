package appcmpt.example.Traffic.n;

/**
 * Created by lalu on 4/7/2017.
 */


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2/23/2017.
 */
public class ListAdapter extends BaseAdapter {

    private Context context;
    private List<Unsafe> navDrawerItems=new ArrayList<>();

    public ListAdapter(Context context, List<Unsafe> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.attch_file, null);
        }

        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView amout = (TextView) convertView.findViewById(R.id.amount);
        TextView reas = (TextView) convertView.findViewById(R.id.reason);
        // TextView txtCount = (TextView) convertView.findViewById(R.id.counter);


        date.setText(navDrawerItems.get(position).getDate());
        amout.setText("vehi_no:"+navDrawerItems.get(position).getVehino());
        reas.setText("user name :"+navDrawerItems.get(position).getUsername());

        // displaying count
        // check whether it set visible or not

        return convertView;
    }

}