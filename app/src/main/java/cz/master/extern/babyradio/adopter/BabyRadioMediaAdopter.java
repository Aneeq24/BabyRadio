package cz.master.extern.babyradio.adopter;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.master.extern.babyradio.R;
import cz.master.extern.babyradio.fragments.BabyRadioFragment;
import cz.master.extern.babyradio.helper.DbHelper;
import cz.master.extern.babyradio.helper.Fonts;
import cz.master.extern.babyradio.models.MediaFileModel;

/**
 * Created by Yasir Iqbal on 7/16/2016.
 */
public class BabyRadioMediaAdopter extends BaseAdapter {
    public List<MediaFileModel> allMediaFiles;
    Activity context;
    LayoutInflater inflater;
    DbHelper dbHelper;
    public MediaPlayer mediaPlayerObj;
    public String pathForMediaFile;
    BabyRadioFragment babyRadioFragment;

    public BabyRadioMediaAdopter(@NonNull Activity context, BabyRadioFragment babyRadioFragment) {
        this.allMediaFiles = new ArrayList<>();
        this.context = context;
        inflater = context.getLayoutInflater();
        dbHelper = new DbHelper(context);
        this.babyRadioFragment = babyRadioFragment;
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

    public static int selectedPosition = -1;
    View prevView;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.template_babyradio_mediaitem, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txt_babyradio_media_item = (TextView) convertView.findViewById(R.id.txt_babyradio_media_item);
            viewHolder.txt_babyradio_media_item.setTypeface(Fonts.getUbuntuRegularTypeFace());
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }//end of else
        viewHolder.txt_babyradio_media_item.setText(allMediaFiles.get(position).name);
        if (position == selectedPosition) {
            viewHolder.txt_babyradio_media_item.setSelected(true);
            Log.d("Selected", position + ":true");
        } else {
            viewHolder.txt_babyradio_media_item.setSelected(false);
            Log.d("Selected", position + ":false");
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
                if (prevView != null) {
                    prevView.setSelected(false);
                }
                insertMessageToDb(message);

                if (v == prevView) {
                    stopMediaFile();
                    prevView = null;
                    v.setSelected(false);
                } else {
                    startMediaFile(allMediaFiles.get(position).path);
                    v.setSelected(true);
                }//end of else for playing file

                prevView = v;
                selectedPosition = position;
            }
        });

        convertView.setTag(viewHolder);
        return convertView;
    }//end of getView

    public void insertMessageToDb(String message) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String currentDateTimeString = df.format(new Date());
        message = currentDateTimeString + ": " + message;
        Log.d("mesage", message);
        dbHelper.insertLog(message);
    }//end of insertMessageToDb


    public void notifyForChangeList(List<MediaFileModel> newMediaFile) {
        this.allMediaFiles.clear();
        allMediaFiles.addAll(newMediaFile);
        notifyDataSetChanged();
    }//end of notifyForChangeList

    static class ViewHolder {
        public TextView txt_babyradio_media_item;
    }

    public void startMediaFile(String path) {
        stopMediaFile();
        try {
            babyRadioFragment.setTextForPlayPauseButton("Pause");
            pathForMediaFile = path;
            AssetFileDescriptor assetFileDescriptor = context.getAssets().openFd(path);
            mediaPlayerObj = new MediaPlayer();
            mediaPlayerObj.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            mediaPlayerObj.prepare();
            mediaPlayerObj.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopMediaFile() {
        if (mediaPlayerObj != null) {
            try {
                babyRadioFragment.setTextForPlayPauseButton("Play");
                mediaPlayerObj.stop();
                mediaPlayerObj.release();
                mediaPlayerObj.reset();
            } catch (Exception e) {

            } finally {
                mediaPlayerObj = null;
            }
        }
    }
}//end of class
