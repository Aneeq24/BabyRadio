package cz.master.extern.babyradio.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.master.extern.babyradio.R;
import cz.master.extern.babyradio.helper.DbHelper;
import cz.master.extern.babyradio.helper.Fonts;
import cz.master.extern.babyradio.helper.SoundTimer;
import cz.master.extern.babyradio.models.MediaFileModel;
import cz.master.extern.babyradio.ui.HomeActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class BabyRadioFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, SeekBar.OnSeekBarChangeListener, WheelPicker.OnItemSelectedListener {


    public AudioManager audioManager;
    private int maxVolumeStream;

    public BabyRadioFragment() {
        // Required empty public constructor
    }

    ListView list_babyradio_sound_music;
    View rootView;
    TextView txt_baby_radio_sounds, txt_baby_radio_music;
    public TextView btn_babyradio_play_pause;
    SeekBar seekbar_babyradio_sound;
    public TextView txt_baby_radio_sounds_timer_bottom, txt_baby_radio_baby_mode_bottom;

    WheelPicker wheelCenter;
    public static MediaPlayer mediaPlayerObj;
    public static String nameForMediaFile, pathForMediaFile;
    DbHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_baby_radio, container, false);

        isSound = false;//this is for getting uri from sound
        init();
        initBabyMode();
        return rootView;
    }//end of onCreateView

    private void initBabyMode() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            boolean statusForAeroplaneMode = isAirplaneMode();
            if (statusForAeroplaneMode) {
                txt_baby_radio_baby_mode_bottom.setText("Baby Mode: OFF");
                txt_baby_radio_baby_mode_bottom.setSelected(false);
            } else {
                txt_baby_radio_baby_mode_bottom.setText("Baby Mode: ON");
                txt_baby_radio_baby_mode_bottom.setSelected(true);
            }
        } else {
            AudioManager am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
            if (am.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                txt_baby_radio_baby_mode_bottom.setText("Baby Mode: ON");
                txt_baby_radio_baby_mode_bottom.setSelected(true);
            } else {
                txt_baby_radio_baby_mode_bottom.setSelected(false);
                txt_baby_radio_baby_mode_bottom.setText("Baby Mode: OFF");
            }
        }
    }//end of initBabyMode

    /*
    *Here init to all baby radio segment
     */
    ArrayList<MediaFileModel> allMusicFiles, allSoundFiles;

    Activity context;


    private void init() {
        btn_babyradio_play_pause = (TextView) rootView.findViewById(R.id.btn_babyradio_play_pause);
        txt_baby_radio_sounds = (TextView) rootView.findViewById(R.id.txt_baby_radio_sounds);
        txt_baby_radio_music = (TextView) rootView.findViewById(R.id.txt_baby_radio_music);
        list_babyradio_sound_music = (ListView) rootView.findViewById(R.id.list_babyradio_sound_music);
        seekbar_babyradio_sound = (SeekBar) rootView.findViewById(R.id.seekbar_babyradio_sound);
        txt_baby_radio_sounds_timer_bottom = (TextView) rootView.findViewById(R.id.txt_baby_radio_sounds_timer_bottom);
        txt_baby_radio_baby_mode_bottom = (TextView) rootView.findViewById(R.id.txt_baby_radio_baby_mode_bottom);
        context = getActivity();
        wheelCenter = (WheelPicker) rootView.findViewById(R.id.main_wheel_center);
        wheelCenter.setItemTextSize(27);
        wheelCenter.setAtmospheric(true);
        allMusicFiles = new ArrayList<>();
        allSoundFiles = new ArrayList<>();
        populateMusicFromAsset();
        populateSoundFromAsset();
        updateListViewWithMedia(allMusicFiles);
        txt_baby_radio_sounds.setOnClickListener(this);
        txt_baby_radio_sounds.setTypeface(Fonts.getUbuntuRegularTypeFace());
        txt_baby_radio_music.setOnClickListener(this);
        txt_baby_radio_music.setTypeface(Fonts.getUbuntuRegularTypeFace());
        list_babyradio_sound_music.setOnItemClickListener(this);
        txt_baby_radio_music.setSelected(true);
        btn_babyradio_play_pause.setOnClickListener(this);
        btn_babyradio_play_pause.setTypeface(Fonts.getUbuntuRegularTypeFace());
        audioManager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        maxVolumeStream = audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        initSeekBarVolume();
        seekbar_babyradio_sound.setOnSeekBarChangeListener(this);
        txt_baby_radio_sounds_timer_bottom.setOnClickListener(this);
        txt_baby_radio_sounds_timer_bottom.setTypeface(Fonts.getUbuntuRegularTypeFace());
        txt_baby_radio_baby_mode_bottom.setOnClickListener(this);
        txt_baby_radio_baby_mode_bottom.setTypeface(Fonts.getUbuntuRegularTypeFace());
        dbHelper = new DbHelper(getActivity());
    }//end of init

    @Override
    public void onResume() {
        super.onResume();
        HomeActivity homeActivity = (HomeActivity) getActivity();
        homeActivity.txt_homeactivity_clear.setVisibility(View.GONE);

        if (mediaPlayerObj != null) {
            btn_babyradio_play_pause.setText("Stop");
            btn_babyradio_play_pause.setSelected(true);
        }//end of if
        if (SoundTimer.soundTimerObj != null) {
            txt_baby_radio_sounds_timer_bottom.setSelected(true);
            SoundTimer.soundTimerObj.setTextForBabyRadioScreen(txt_baby_radio_sounds_timer_bottom);
        }
    }//end of onResume

    /*

         */
    private void updateListViewWithMedia(List<MediaFileModel> allMediaFiles) {
        List<String> allstrings = new ArrayList<>();
        for (MediaFileModel tempModel : allMediaFiles) {
            allstrings.add(tempModel.name);
        }
        wheelCenter.setData(allstrings);
        wheelCenter.setSelectedItemPosition(allstrings.size() / 2);
    }

    private void populateSoundFromAsset() {
        try {
            String folderName = "babysounds";
            String[] files = context.getAssets().list(folderName);
            if (files != null) {
                Log.d("Sound files", "" + files.length);
                for (int i = 0; i < files.length; i++) {
                    String tempFile = files[i];
                    String[] fileName = tempFile.split("\\.");
                    MediaFileModel tempMediaFileModel = new MediaFileModel();
                    tempMediaFileModel.path = folderName + File.separator + tempFile;
                    tempMediaFileModel.name = fileName[0];
                    allSoundFiles.add(tempMediaFileModel);
                    Log.d("Sound Name", tempMediaFileModel.name);
                }//end of for loop
            }//end of files not null
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end of populateSoundFromAsset

    private void populateMusicFromAsset() {
        try {
            String folderName = "musics";
            String[] files = context.getAssets().list(folderName);
            if (files != null) {
                Log.d("Music files", "" + files.length);
                for (int i = 0; i < files.length; i++) {
                    String tempFile = files[i];
                    String[] fileName = tempFile.split("\\.");
                    MediaFileModel tempMediaFileModel = new MediaFileModel();
                    tempMediaFileModel.path = folderName + File.separator + tempFile;
                    tempMediaFileModel.name = fileName[0];
                    allMusicFiles.add(tempMediaFileModel);
                    Log.d("Music Name", tempMediaFileModel.name);
                }//end of for loop
            }//end of files not null
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end of populateMusicFromAsset

    boolean isSound = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_baby_radio_baby_mode_bottom:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    boolean statusForAeroplaneMode = isAirplaneMode();
                    toggleAirplaneMode(statusForAeroplaneMode);
                    if (statusForAeroplaneMode) {
                        txt_baby_radio_baby_mode_bottom.setText("Baby Mode: OFF");
                    } else {
                        txt_baby_radio_baby_mode_bottom.setText("Baby Mode: ON");
                    }
                } else {
                    String text = txt_baby_radio_baby_mode_bottom.getText().toString();
                    if (text.toLowerCase().contains("off")) {
                        AudioManager am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
                        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        txt_baby_radio_baby_mode_bottom.setText("Baby Mode: ON");
                    } else {
                        AudioManager am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
                        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        txt_baby_radio_baby_mode_bottom.setText("Baby Mode: OFF");
                    }
                }
                txt_baby_radio_baby_mode_bottom.setSelected(!txt_baby_radio_baby_mode_bottom.isSelected());
                break;
            case R.id.txt_baby_radio_sounds_timer_bottom:
                HomeActivity homeActivity = (HomeActivity) getActivity();
                homeActivity.openSoundTimerFragment();
                break;
            case R.id.txt_baby_radio_sounds:
                txt_baby_radio_music.setSelected(false);
                txt_baby_radio_sounds.setSelected(true);
                updateListViewWithMedia(allSoundFiles);
                isSound = true;
                break;
            case R.id.txt_baby_radio_music:
                txt_baby_radio_music.setSelected(true);
                txt_baby_radio_sounds.setSelected(false);
                updateListViewWithMedia(allMusicFiles);
                isSound = false;
                break;
            case R.id.btn_babyradio_play_pause:
                String message = null;
                if (btn_babyradio_play_pause.getText().toString().equalsIgnoreCase("play")) {


                    String path = "";
                    String name = "";
                    if (isSound) {
                        if (wheelCenter.getCurrentItemPosition() < allSoundFiles.size()) {
                            path = allSoundFiles.get(wheelCenter.getCurrentItemPosition()).path;
                            name = allSoundFiles.get(wheelCenter.getCurrentItemPosition()).name;
                        }
                    } else {
                        if (wheelCenter.getCurrentItemPosition() < allMusicFiles.size()) {
                            path = allMusicFiles.get(wheelCenter.getCurrentItemPosition()).path;
                            name = allMusicFiles.get(wheelCenter.getCurrentItemPosition()).name;
                        }
                    }
                    pathForMediaFile = path;
                    startMediaFile(path);
                    btn_babyradio_play_pause.setText("Stop");
                    btn_babyradio_play_pause.setSelected(true);
                    message = name + " started by User";
                    nameForMediaFile = name;

                } else {
                    btn_babyradio_play_pause.setText("Play");
                    btn_babyradio_play_pause.setSelected(false);
                    stopMediaFile();


                    message = nameForMediaFile + " stopped by User";

                }
                if (message != null)
                    insertMessageToDb(message);
                break;
        }//end of switch
    }//end of oonClick

    public void initSeekBarVolume() {
        // TODO Auto-generated method stub
        int vol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.d("vol", vol + "");
        vol = (vol * 100) / maxVolumeStream;
        if (seekbar_babyradio_sound != null)
            seekbar_babyradio_sound.setProgress(vol);
    }

    public void setVolumeForMedia(int progress) {
        progress = (progress * maxVolumeStream) / 100;
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
    }//end of setVolumeOfApplication

    public void setTextForPlayPauseButton(String text) {
        if (btn_babyradio_play_pause != null)
            btn_babyradio_play_pause.setText(text);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }//end of onItem

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser)
            setVolumeForMedia(progress);
    }//end of onProgressChanged

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    public void toggleAirplaneMode(boolean state) {
        // toggle airplane mode
        Settings.System.putInt(getActivity().getContentResolver(), Settings.System.AIRPLANE_MODE_ON, state ? 0 : 1);

        // broadcast an intent to inform
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", !state);
        getActivity().sendBroadcast(intent);
    }


    public boolean isAirplaneMode() {
        return Settings.System.getInt(getActivity().getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;
    }

    @Override
    public void onItemSelected(WheelPicker wheelPicker, Object o, int i) {

    }//end of onItemSelected

    public void insertMessageToDb(String message) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String currentDateTimeString = df.format(new Date());
        message = currentDateTimeString + ": " + message;
        Log.d("mesage", message);
        dbHelper.insertLog(message);
    }//end of insertMessageToDb

    public void startMediaFile(String path) {
        stopMediaFile();
        try {
            setTextForPlayPauseButton("Stop");
            nameForMediaFile = path;
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
                setTextForPlayPauseButton("Play");
                mediaPlayerObj.stop();
                mediaPlayerObj.release();
                mediaPlayerObj.reset();
            } catch (Exception e) {

            } finally {
                mediaPlayerObj = null;
            }
        }
    }
}
