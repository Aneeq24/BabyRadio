package cz.master.extern.babyradio.fragments;


import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cz.master.extern.babyradio.helper.RattleView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RattleFragment extends Fragment {


    public RattleFragment() {
        // Required empty public constructor
    }

    private SensorManager sensorManager;
    RattleView level1View;
    private Handler frame = new Handler();
    //Velocity includes the speed and the direction of our sprite motion
    private Point sprite1Velocity;
    private Point sprite2Velocity;
    private int sprite1MaxX;
    private int sprite1MaxY;
    private int sprite2MaxX;
    private int sprite2MaxY;
    //Divide the frame by 1000 to calculate how many times per second the screen will update.
    private static final int FRAME_RATE = 20; //50 frames per second

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        level1View = new RattleView(getActivity());
        return level1View;
        //return inflater.inflate(R.layout.fragment_rattle, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
        sensorManager.registerListener(level1View,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(level1View);
    }//end of onPause
}//end of fragment

