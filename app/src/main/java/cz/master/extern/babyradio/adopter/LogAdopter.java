package cz.master.extern.babyradio.adopter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cz.master.extern.babyradio.R;

/**
 * Created by Yasir Iqbal on 7/17/2016.
 */
public class LogAdopter extends BaseAdapter {
    ArrayList<String> allLogRecords;
    Activity context;
    LayoutInflater inflater;

    public LogAdopter(Activity context, ArrayList<String> allLogRecords) {
        this.allLogRecords = allLogRecords;
        this.context = context;
        inflater = context.getLayoutInflater();
    }//end of constructor

    @Override
    public int getCount() {
        return allLogRecords.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.template_log_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.Icon = (ImageView) convertView.findViewById(R.id.Icon);
            viewHolder.Label = (TextView) convertView.findViewById(R.id.Label);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.Label.setText(allLogRecords.get(position));
        if (position % 3 == 0) {
            if (position > 0)
                viewHolder.Icon.setSelected(true);
        } else {
            viewHolder.Icon.setSelected(false);
        }
        convertView.setTag(viewHolder);
        return convertView;
    }//end of getView

    static class ViewHolder {
        public ImageView Icon;
        public TextView Label;
    }
}
