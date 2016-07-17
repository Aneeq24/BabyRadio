package cz.master.extern.babyradio.adopter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.master.extern.babyradio.R;
import cz.master.extern.babyradio.helper.DbHelper;
import cz.master.extern.babyradio.models.MediaFileModel;

/**
 * Created by Yasir Iqbal on 7/16/2016.
 */
public class BabyRadioMediaAdopter extends BaseAdapter {
    List<MediaFileModel> allMediaFiles;
    Activity context;
    LayoutInflater inflater;
    DbHelper dbHelper;

    public BabyRadioMediaAdopter(@NonNull Activity context) {
        this.allMediaFiles = new ArrayList<>();
        this.context = context;
        inflater = context.getLayoutInflater();
        dbHelper = new DbHelper(context);
    }//end of constructor

    @Override
    public int getCount() {
        return allMediaFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static int selectedPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.template_babyradio_mediaitem, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txt_babyradio_media_item = (TextView) convertView.findViewById(R.id.txt_babyradio_media_item);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }//end of else
        viewHolder.txt_babyradio_media_item.setText(allMediaFiles.get(position).name);
        if (position == selectedPosition)
            convertView.setSelected(true);
        else {
            convertView.setSelected(false);
        }
        viewHolder.txt_babyradio_media_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message;
                if (v.isSelected()) {
                    message = allMediaFiles.get(position).name + " stopped by User";
                } else {

                    message = allMediaFiles.get(position).name + " started by User";
                }
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm aa");
                String currentDateTimeString = df.format(new Date());
                message = currentDateTimeString + ":" + message;
                Log.d("mesage", message);
                dbHelper.insertLog(message);
                selectedPosition = position;
                v.setSelected(true);
                // notifyDataSetChanged();
            }
        });
        convertView.setTag(viewHolder);
        return convertView;
    }//end of getView


    public void notifyForChangeList(List<MediaFileModel> newMediaFile) {
        this.allMediaFiles.clear();
        allMediaFiles.addAll(newMediaFile);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        public TextView txt_babyradio_media_item;
    }
}//end of class
