package cz.master.extern.babyradio.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
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
import cz.master.extern.babyradio.helper.SoundTimer;
import cz.master.extern.babyradio.ui.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SoundTimerFragment extends Fragment implements View.OnClickListener {


    public SoundTimerFragment() {
        // Required empty public constructor
    }


    View rootView;
    private NumberPickerView mPickerViewH;
    private NumberPickerView mPickerViewM;
    private NumberPickerView mPickerViewD;
    TextView txt_baby_tips_stopwatch, txt_baby_tips_clock, TipLabel, Label_TimerCountdown, Label_TimerDescription;
    View container_timer_picker, container_timer_text;
    Button Button_TimerStartStop;
    SwitchCompat switch_baby_monitor;
    SeekBar SensitivityBar;

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
        mPickerViewH = (NumberPickerView) rootView.findViewById(R.id.picker_hour);
        mPickerViewM = (NumberPickerView) rootView.findViewById(R.id.picker_minute);
        mPickerViewD = (NumberPickerView) rootView.findViewById(R.id.picker_half_day);
        txt_baby_tips_stopwatch.setSelected(true);
        txt_baby_tips_stopwatch.setOnClickListener(this);
        txt_baby_tips_clock.setOnClickListener(this);
        Button_TimerStartStop.setOnClickListener(this);
        switch_baby_monitor.setOnClickListener(this);
        SensitivityBar.setEnabled(switch_baby_monitor.isSelected());
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
        setData(mPickerViewH, 0, 23, 0);
        setData(mPickerViewM, 0, 59, 0);
        setData(mPickerViewD, 0, 1, 0);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_baby_monitor:
                SensitivityBar.setEnabled(!v.isSelected());
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
                    TipLabel.setText(getResources().getString(R.string.label_monitor_off_description));
                    Button_TimerStartStop.setText("Start");
                    container_timer_text.setVisibility(View.GONE);
                    container_timer_picker.setVisibility(View.VISIBLE);
                    SoundTimer soundTimer = SoundTimer.getSoundTimerObj((HomeActivity) getActivity(), 10000, Label_TimerCountdown);
                    soundTimer.resetTimer();
                }
                break;
        }//end of switch
    }//end of onClick
}//end of fragment
