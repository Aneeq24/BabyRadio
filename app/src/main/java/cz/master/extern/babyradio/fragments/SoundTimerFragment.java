package cz.master.extern.babyradio.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;
import cz.master.extern.babyradio.R;

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
    TextView txt_baby_tips_stopwatch, txt_baby_tips_clock;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_sound_timer, container, false);
        init();
        return rootView;
    }//end o fonCreateView

    /*
    *init here findViewby id etc
     */
    private void init() {
        txt_baby_tips_stopwatch = (TextView) rootView.findViewById(R.id.txt_baby_tips_stopwatch);
        txt_baby_tips_clock = (TextView) rootView.findViewById(R.id.txt_baby_tips_clock);
        mPickerViewH = (NumberPickerView) rootView.findViewById(R.id.picker_hour);
        mPickerViewM = (NumberPickerView) rootView.findViewById(R.id.picker_minute);
        mPickerViewD = (NumberPickerView) rootView.findViewById(R.id.picker_half_day);
        txt_baby_tips_stopwatch.setSelected(true);
        txt_baby_tips_stopwatch.setOnClickListener(this);
        txt_baby_tips_clock.setOnClickListener(this);
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
        h = h % 12;

        setData(mPickerViewH, 0, 11, h);
        setData(mPickerViewM, 0, 59, m);
        setData(mPickerViewD, 0, 1, d);
    }

    private void setData(NumberPickerView picker, int minValue, int maxValue, int value) {
        picker.setMinValue(minValue);
        picker.setMaxValue(maxValue);
        picker.setValue(value);
    }//end of setData

    public void getTimeFromPicker() {
        String h = mPickerViewH.getContentByCurrValue();
        String m = mPickerViewM.getContentByCurrValue();
        String d = mPickerViewD.getContentByCurrValue();
        Toast.makeText(getActivity(), h + getString(R.string.hour_hint) + " "
                + m + getString(R.string.minute_hint) + " " + d, Toast.LENGTH_LONG).show();
    }//end of getTimeFromPicker

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_baby_tips_stopwatch:
                txt_baby_tips_stopwatch.setSelected(true);
                txt_baby_tips_clock.setSelected(false);
                mPickerViewD.setVisibility(View.GONE);
                break;
            case R.id.txt_baby_tips_clock:
                txt_baby_tips_stopwatch.setSelected(false);
                txt_baby_tips_clock.setSelected(true);
                mPickerViewD.setVisibility(View.VISIBLE);
                break;
        }//end of switch
    }//end of onClick
}//end of fragment
