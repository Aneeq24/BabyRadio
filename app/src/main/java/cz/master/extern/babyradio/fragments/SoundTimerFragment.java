package cz.master.extern.babyradio.fragments;


import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;
import cz.master.extern.babyradio.R;
import cz.master.extern.babyradio.helper.BarLevelDrawable;
import cz.master.extern.babyradio.helper.MicrophoneInput;
import cz.master.extern.babyradio.helper.PermissionHandler;
import cz.master.extern.babyradio.helper.SoundTimer;
import cz.master.extern.babyradio.interfaces.MicrophoneInputListener;
import cz.master.extern.babyradio.ui.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SoundTimerFragment extends Fragment implements View.OnClickListener, MicrophoneInputListener, SeekBar.OnSeekBarChangeListener {


    private static final String TAG = "Sound Level";

    public SoundTimerFragment() {
        // Required empty public constructor
    }

    int babyMonitorLimit = 100;
    PermissionHandler permissionHandler;
    BarLevelDrawable mBarLevel;
    MicrophoneInput micInput;  // The micInput object provides real time audio.
    double mOffsetdB = 10;  // Offset for bar, i.e. 0 lit LEDs at 10 dB.
    // The Google ASR input requirements state that audio input sensitivity
    // should be set such that 90 dB SPL at 1000 Hz yields RMS of 2500 for
    // 16-bit samples, i.e. 20 * log_10(2500 / mGain) = 90.
    double mGain = 2500.0 / Math.pow(10.0, 90.0 / 20.0);
    // For displaying error in calibration.
    double mDifferenceFromNominal = 0.0;
    double mRmsSmoothed;  // Temporally filtered version of RMS.
    double mAlpha = 0.9;  // Coefficient of IIR smoothing filter for RMS.
    private int mSampleRate = 16000;  // The audio sampling rate to use.
    private int mAudioSource = MediaRecorder.AudioSource.DEFAULT;  // The audio source to use.

    // Variables to monitor UI update and check for slow updates.
    private volatile boolean mDrawing = false;
    private volatile int mDrawingCollided;
    //***********************
    View rootView;
    private NumberPickerView mPickerViewH;
    private NumberPickerView mPickerViewM;
    private NumberPickerView mPickerViewD;
    TextView txt_baby_tips_stopwatch, txt_baby_tips_clock, TipLabel, Label_TimerCountdown, Label_TimerDescription;
    View container_timer_picker, container_timer_text;
    Button Button_TimerStartStop;
    SwitchCompat switch_baby_monitor;
    SeekBar SensitivityBar, SensitivityBar_upper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_sound_timer, container, false);
            init();
        }
        return rootView;
    }//end o fonCreateView

    /*
    *init here findViewby id etc
     */
    private void init() {
        permissionHandler = new PermissionHandler(getActivity());
        txt_baby_tips_stopwatch = (TextView) rootView.findViewById(R.id.txt_baby_tips_stopwatch);
        txt_baby_tips_clock = (TextView) rootView.findViewById(R.id.txt_baby_tips_clock);
        TipLabel = (TextView) rootView.findViewById(R.id.TipLabel);
        Label_TimerCountdown = (TextView) rootView.findViewById(R.id.Label_TimerCountdown);
        Label_TimerDescription = (TextView) rootView.findViewById(R.id.Label_TimerDescription);
        container_timer_picker = rootView.findViewById(R.id.container_timer_picker);
        container_timer_text = rootView.findViewById(R.id.container_timer_text);
        Button_TimerStartStop = (Button) rootView.findViewById(R.id.Button_TimerStartStop);
        switch_baby_monitor = (SwitchCompat) rootView.findViewById(R.id.switch_baby_monitor);
        SensitivityBar = (SeekBar) rootView.findViewById(R.id.SensitivityBar);
        SensitivityBar_upper = (SeekBar) rootView.findViewById(R.id.SensitivityBar_upper);
        mPickerViewH = (NumberPickerView) rootView.findViewById(R.id.picker_hour);
        mPickerViewM = (NumberPickerView) rootView.findViewById(R.id.picker_minute);
        mPickerViewD = (NumberPickerView) rootView.findViewById(R.id.picker_half_day);
        txt_baby_tips_stopwatch.setSelected(true);
        txt_baby_tips_stopwatch.setOnClickListener(this);
        txt_baby_tips_clock.setOnClickListener(this);
        Button_TimerStartStop.setOnClickListener(this);
        switch_baby_monitor.setOnClickListener(this);
        SensitivityBar.setEnabled(switch_baby_monitor.isSelected());
        SensitivityBar_upper.setEnabled(switch_baby_monitor.isSelected());
        SensitivityBar.setOnSeekBarChangeListener(this);
        SensitivityBar_upper.setOnSeekBarChangeListener(this);
        babyMonitorLimit = SensitivityBar_upper.getProgress();
        //***************************
        mBarLevel = (BarLevelDrawable) rootView.findViewById(R.id.bar_level_drawable_view);
        micInput = new MicrophoneInput(this);
        micInput.setSampleRate(mSampleRate);
        micInput.setAudioSource(mAudioSource);
    }//end of init

    /*
    *init here we current time
    *
     */
    private void initTime() {
        GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        int d = h < 12 ? 0 : 1;

        setData(mPickerViewH, 0, 23, h);
        setData(mPickerViewM, 0, 59, m);
        setData(mPickerViewD, 0, 1, d);
    }

    void initStopWatch() {
        initTime();
//        setData(mPickerViewH, 0, 23, 0);
//        setData(mPickerViewM, 0, 59, 0);
//        setData(mPickerViewD, 0, 1, 0);
    }

    private void setData(NumberPickerView picker, int minValue, int maxValue, int value) {
        picker.setMinValue(minValue);
        picker.setMaxValue(maxValue);
        picker.setValue(value);
    }//end of setData

    int hourTime, minuteTime;

    public void getTimeFromPicker() {
        String h = mPickerViewH.getContentByCurrValue();
        String m = mPickerViewM.getContentByCurrValue();
        hourTime = Integer.parseInt(h);
        minuteTime = Integer.parseInt(m);

    }//end of getTimeFromPicker

    @Override
    public void onResume() {
        super.onResume();
        if (switch_baby_monitor.isSelected()) {
            micInput.start();
            SensitivityBar.setEnabled(true);
            SensitivityBar_upper.setEnabled(true);
        }//end of if
    }

    @Override
    public void onPause() {
        super.onPause();
        if (switch_baby_monitor.isSelected()) {
            micInput.stop();
            SensitivityBar.setEnabled(false);
            SensitivityBar_upper.setEnabled(false);
        }//end of if
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_baby_monitor:
                if (!permissionHandler.isPermissionAvailable(PermissionHandler.RECORD_AUDIO)) {
                    permissionHandler.requestPermission(PermissionHandler.RECORD_AUDIO);
                    return;
                }
                if (v.isSelected()) {
                    micInput.stop();
                    TipLabel.setText(getString(R.string.label_monitor_off_description));
                    SensitivityBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SensitivityBar.setProgress(0);
                        }
                    }, 100);
                } else {
                    TipLabel.setText(getString(R.string.label_monitor_on_description));
                    micInput.start();
                }
                SensitivityBar.setEnabled(!v.isSelected());
                SensitivityBar_upper.setEnabled(!v.isSelected());
                v.setSelected(!v.isSelected());
                break;
            case R.id.txt_baby_tips_stopwatch:
                initStopWatch();
                txt_baby_tips_stopwatch.setSelected(true);
                txt_baby_tips_clock.setSelected(false);
                mPickerViewD.setVisibility(View.GONE);
                break;
            case R.id.txt_baby_tips_clock:
                initTime();
                txt_baby_tips_stopwatch.setSelected(false);
                txt_baby_tips_clock.setSelected(true);
                mPickerViewD.setVisibility(View.GONE);
                break;
            case R.id.Button_TimerStartStop:
                if (Button_TimerStartStop.getText().toString().equalsIgnoreCase("Start")) {
                    getTimeFromPicker();
                    if (txt_baby_tips_clock.isSelected()) {
                        //Implement as time as clock is set
                        Calendar now = Calendar.getInstance();
                        Calendar SoundTimeCal = Calendar.getInstance();
                        SoundTimeCal.set(Calendar.HOUR_OF_DAY, hourTime);
                        SoundTimeCal.set(Calendar.MINUTE, minuteTime);
                        if (now.getTime().before(SoundTimeCal.getTime())) {
                            container_timer_text.setVisibility(View.VISIBLE);
                            container_timer_picker.setVisibility(View.INVISIBLE);
                            long hoursMilis = SoundTimeCal.getTimeInMillis() - now.getTimeInMillis();
                            SoundTimer soundTimer = SoundTimer.getSoundTimerObj((HomeActivity) getActivity(), hoursMilis, Label_TimerCountdown);
                            soundTimer.resetTimer();
                            soundTimer = SoundTimer.getSoundTimerObj((HomeActivity) getActivity(), hoursMilis, Label_TimerCountdown);
                            soundTimer.start();
                            SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
                            String timeForStopSound = df.format(SoundTimeCal.getTime());
                            Label_TimerDescription.setText("Sound Stop at " + timeForStopSound);
                            Toast.makeText(getActivity(), "Valid Timer", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Please Select Valid Time", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        //Implement as stopwatch is set
                        if (hourTime == 0 && minuteTime == 0) {
                            Toast.makeText(getActivity(), "Please Select Valid Time", Toast.LENGTH_LONG).show();
                            return;
                        }
                        container_timer_text.setVisibility(View.VISIBLE);
                        container_timer_picker.setVisibility(View.INVISIBLE);
                        long hoursMilis = TimeUnit.HOURS.toMillis(hourTime);
                        long minutesMilis = TimeUnit.MINUTES.toMillis(minuteTime);
                        hoursMilis = hoursMilis + minutesMilis;
                        SoundTimer soundTimer = SoundTimer.getSoundTimerObj((HomeActivity) getActivity(), hoursMilis, Label_TimerCountdown);
                        soundTimer.resetTimer();
                        soundTimer = SoundTimer.getSoundTimerObj((HomeActivity) getActivity(), hoursMilis, Label_TimerCountdown);
                        soundTimer.start();
                        Calendar calendarNow = Calendar.getInstance();
                        calendarNow.setTimeInMillis((calendarNow.getTimeInMillis() + hoursMilis));
                        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
                        String timeForStopSound = df.format(calendarNow.getTime());
                        Label_TimerDescription.setText("Sound Stop at " + timeForStopSound);
                    }//end of else
                    TipLabel.setText(getResources().getString(R.string.label_monitor_on_description));
                    Button_TimerStartStop.setText("Stop");
                }//end of if for playing timer
                else {
                    toStopSoundTimer();
                }
                break;
        }//end of switch
    }//end of onClick

    public void toStopSoundTimer() {
        TipLabel.setText(getResources().getString(R.string.label_monitor_off_description));
        Button_TimerStartStop.setText("Start");
        container_timer_text.setVisibility(View.GONE);
        container_timer_picker.setVisibility(View.VISIBLE);
        SoundTimer soundTimer = SoundTimer.getSoundTimerObj((HomeActivity) getActivity(), 10000, Label_TimerCountdown);
        soundTimer.resetTimer();
    }

    @Override
    public void processAudioFrame(short[] audioFrame) {
        if (!mDrawing) {
            mDrawing = true;
            // Compute the RMS value. (Note that this does not remove DC).
            double rms = 0;
            for (int i = 0; i < audioFrame.length; i++) {
                rms += audioFrame[i] * audioFrame[i];
            }
            rms = Math.sqrt(rms / audioFrame.length);

            // Compute a smoothed version for less flickering of the display.
            mRmsSmoothed = mRmsSmoothed * mAlpha + (1 - mAlpha) * rms;
            final double rmsdB = 20.0 * Math.log10(mGain * mRmsSmoothed);

            // Set up a method that runs on the UI thread to update of the LED bar
            // and numerical display.
            SensitivityBar.post(new Runnable() {
                @Override
                public void run() {
                    int level = (int) (((mOffsetdB + rmsdB) / 100) * 100);
                    SensitivityBar.setProgress(level);
                    mDrawing = false;
                }
            });
            mBarLevel.post(new Runnable() {
                @Override
                public void run() {
                    // The bar has an input range of [0.0 ; 1.0] and 10 segments.
                    // Each LED corresponds to 6 dB.
                    mBarLevel.setLevel((mOffsetdB + rmsdB) / 100);

//                    DecimalFormat df = new DecimalFormat("##");
//                    mdBTextView.setText(df.format(20 + rmsdB));
//
//                    DecimalFormat df_fraction = new DecimalFormat("#");
//                    int one_decimal = (int) (Math.round(Math.abs(rmsdB * 10))) % 10;
//                    mdBFractionTextView.setText(Integer.toString(one_decimal));
                    mDrawing = false;
                }
            });
        } else {
            mDrawingCollided++;
            Log.v(TAG, "Level bar update collision, i.e. update took longer " +
                    "than 20ms. Collision count" + Double.toString(mDrawingCollided));
        }
    }//end of function

    public void afterPermsissionGranted() {
        onClick(switch_baby_monitor);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == SensitivityBar_upper) {
            babyMonitorLimit = progress;
        } else {
            if (progress > babyMonitorLimit) {
                performOperationForBabyMonitor();
            }
        }
    }

    /**
     * This function call when noice increase from user selected
     */
    private void performOperationForBabyMonitor() {
        HomeActivity homeActivity = (HomeActivity) getActivity();
        homeActivity.restartSoundAfterBabyMonitor();
    }//enmd of funciton

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}//end of fragment
