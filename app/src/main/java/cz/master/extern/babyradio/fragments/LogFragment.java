package cz.master.extern.babyradio.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cz.master.extern.babyradio.R;
import cz.master.extern.babyradio.adopter.LogAdopter;
import cz.master.extern.babyradio.helper.DbHelper;
import cz.master.extern.babyradio.helper.Fonts;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogFragment extends Fragment {


    public ArrayList<String> allLogList;

    public LogFragment() {
        // Required empty public constructor
    }


    View rootView;
    ListView list_all_logs;

    TextView txt_logscreen_norecord;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_log, container, false);
        dbHelper = new DbHelper(getActivity());
        txt_logscreen_norecord = (TextView) rootView.findViewById(R.id.txt_logscreen_norecord);
        txt_logscreen_norecord.setTypeface(Fonts.getUbuntuRegularTypeFace());
        return rootView;
    }//end of onCreateView

    public LogAdopter logAdopter;
    DbHelper dbHelper;

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        allLogList = dbHelper.getAllLog();
        if (allLogList.size() > 0) {
            list_all_logs = (ListView) rootView.findViewById(R.id.list_all_logs);
            rootView.findViewById(R.id.txt_logscreen_norecord).setVisibility(View.GONE);
            list_all_logs.setVisibility(View.VISIBLE);
            logAdopter = new LogAdopter(getActivity(), allLogList);
            list_all_logs.setAdapter(logAdopter);
        }
    }//end of init

    public void deleteAllLog() {
        rootView.findViewById(R.id.txt_logscreen_norecord).setVisibility(View.VISIBLE);
        if (logAdopter != null) {
            logAdopter.allLogRecords.clear();
            list_all_logs.setVisibility(View.GONE);
        }
    }
}
