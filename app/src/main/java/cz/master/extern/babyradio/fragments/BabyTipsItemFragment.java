package cz.master.extern.babyradio.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.master.extern.babyradio.R;
import cz.master.extern.babyradio.helper.Fonts;
import cz.master.extern.babyradio.models.BabyTipsModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BabyTipsItemFragment extends Fragment {


    public BabyTipsItemFragment() {
        // Required empty public constructor
    }

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.template_babytips_item, container, false);
        init();
        return rootView;
    }//end of onCreateview

    TextView txt_babytips_item_titile, txt_babytips_item_description;
    /*
    *init is to initialize all data
     */
    BabyTipsModel babyTipsModelObj;

    private void init() {
        babyTipsModelObj = (BabyTipsModel) getArguments().getSerializable("tips");
        txt_babytips_item_titile = (TextView) rootView.findViewById(R.id.txt_babytips_item_titile);
        txt_babytips_item_description = (TextView) rootView.findViewById(R.id.txt_babytips_item_description);
        txt_babytips_item_titile.setTypeface(Fonts.getUbuntuLightTypeFace());
        txt_babytips_item_description.setTypeface(Fonts.getUbuntuLightTypeFace());
        if (babyTipsModelObj != null) {
            txt_babytips_item_titile.setText(babyTipsModelObj.getTitle());
            txt_babytips_item_description.setText(babyTipsModelObj.getDescription());
        }
    }//end of init

}
