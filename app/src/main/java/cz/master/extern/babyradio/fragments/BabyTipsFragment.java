package cz.master.extern.babyradio.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cz.master.extern.babyradio.R;
import cz.master.extern.babyradio.adopter.BabyTipsAdopter;
import cz.master.extern.babyradio.helper.Fonts;
import cz.master.extern.babyradio.models.BabyTipsModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BabyTipsFragment extends Fragment implements View.OnClickListener {


    public BabyTipsFragment() {
        // Required empty public constructor
    }

    ImageView img_baby_tips_ic_left, img_baby_tips_ic_right;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_baby_tips, container, false);
        init();
        return rootView;
    }

    TextView txt_baby_tips_sleeping, txt_baby_tips_safety;
    ViewPager viewpager_babytips_sleeping, viewpager_babytips_safety;

    /*
    *this init is to findviewbyid and setting alll first time setting
     */
    private void init() {
        txt_baby_tips_sleeping = (TextView) rootView.findViewById(R.id.txt_baby_tips_sleeping);
        txt_baby_tips_safety = (TextView) rootView.findViewById(R.id.txt_baby_tips_safety);
        viewpager_babytips_sleeping = (ViewPager) rootView.findViewById(R.id.viewpager_babytips);
        viewpager_babytips_safety = (ViewPager) rootView.findViewById(R.id.viewpager_babytips_safety);
        img_baby_tips_ic_left = (ImageView) rootView.findViewById(R.id.img_baby_tips_ic_left);
        img_baby_tips_ic_right = (ImageView) rootView.findViewById(R.id.img_baby_tips_ic_right);
        txt_baby_tips_sleeping.setTypeface(Fonts.getUbuntuLightTypeFace());
        txt_baby_tips_safety.setTypeface(Fonts.getUbuntuLightTypeFace());
        txt_baby_tips_sleeping.setSelected(true);
        txt_baby_tips_sleeping.setOnClickListener(this);
        txt_baby_tips_safety.setOnClickListener(this);
        img_baby_tips_ic_left.setOnClickListener(this);
        img_baby_tips_ic_right.setOnClickListener(this);

        initSleepingBabyList();
        sleepingTipsAdopter = new BabyTipsAdopter(getActivity(), getChildFragmentManager(), sleepingTipsList);
        viewpager_babytips_sleeping.setAdapter(sleepingTipsAdopter);
        addPageListner();
        img_baby_tips_ic_left.setVisibility(View.GONE);
    }//end of init

    private void addPageListner() {
        viewpager_babytips_sleeping.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    img_baby_tips_ic_left.setVisibility(View.GONE);
                    img_baby_tips_ic_right.setVisibility(View.VISIBLE);
                }//end of if
                else if (position < (sleepingTipsList.size() - 1)) {
                    img_baby_tips_ic_left.setVisibility(View.VISIBLE);
                    img_baby_tips_ic_right.setVisibility(View.VISIBLE);
                } else {

                    img_baby_tips_ic_left.setVisibility(View.VISIBLE);
                    img_baby_tips_ic_right.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager_babytips_safety.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    img_baby_tips_ic_left.setVisibility(View.GONE);
                    img_baby_tips_ic_right.setVisibility(View.VISIBLE);
                }//end of if
                else if (position < (safetyTipsList.size() - 1)) {
                    img_baby_tips_ic_left.setVisibility(View.VISIBLE);
                    img_baby_tips_ic_right.setVisibility(View.VISIBLE);
                } else {

                    img_baby_tips_ic_left.setVisibility(View.VISIBLE);
                    img_baby_tips_ic_right.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }//end of function

    ArrayList<Fragment> sleepingTipsList, safetyTipsList;
    BabyTipsAdopter sleepingTipsAdopter, safetyTipsAdopter;

    public void initSafetyBabyList() {
        safetyTipsList = new ArrayList<>();
        String[] arrForBabyTipsModel = getResources().getStringArray(R.array.safety_tip_1);
        BabyTipsModel babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        Bundle bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        Fragment fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        safetyTipsList.add(fragment);
        //*****************
        arrForBabyTipsModel = getResources().getStringArray(R.array.safety_tip_2);
        babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        safetyTipsList.add(fragment);
        //*****************
        arrForBabyTipsModel = getResources().getStringArray(R.array.safety_tip_3);
        babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        safetyTipsList.add(fragment);
        //*****************
        arrForBabyTipsModel = getResources().getStringArray(R.array.safety_tip_4);
        babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        safetyTipsList.add(fragment);
        //*****************
        arrForBabyTipsModel = getResources().getStringArray(R.array.safety_tip_5);
        babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        safetyTipsList.add(fragment);
        //*****************
        arrForBabyTipsModel = getResources().getStringArray(R.array.safety_tip_6);
        babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        safetyTipsList.add(fragment);

        //*****************
        arrForBabyTipsModel = getResources().getStringArray(R.array.safety_tip_7);
        babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        safetyTipsList.add(fragment);
        //*****************
        arrForBabyTipsModel = getResources().getStringArray(R.array.safety_tip_8);
        babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        safetyTipsList.add(fragment);
        //*****************
        arrForBabyTipsModel = getResources().getStringArray(R.array.safety_tip_9);
        babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        safetyTipsList.add(fragment);

        //*****************
        arrForBabyTipsModel = getResources().getStringArray(R.array.safety_tip_10);
        babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        safetyTipsList.add(fragment);

        //*****************
        arrForBabyTipsModel = getResources().getStringArray(R.array.safety_tip_11);
        babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        safetyTipsList.add(fragment);
    }//end o initSafety

    private void initSleepingBabyList() {
        sleepingTipsList = new ArrayList<>();
        String[] arrForBabyTipsModel = getResources().getStringArray(R.array.sleeping_tip_1);
        BabyTipsModel babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        Bundle bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        Fragment fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        sleepingTipsList.add(fragment);
        //*****************
        arrForBabyTipsModel = getResources().getStringArray(R.array.sleeping_tip_2);
        babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        sleepingTipsList.add(fragment);
        //*****************
        arrForBabyTipsModel = getResources().getStringArray(R.array.sleeping_tip_3);
        babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        sleepingTipsList.add(fragment);
        //*****************
        arrForBabyTipsModel = getResources().getStringArray(R.array.sleeping_tip_4);
        babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        sleepingTipsList.add(fragment);
        //*****************
        arrForBabyTipsModel = getResources().getStringArray(R.array.sleeping_tip_5);
        babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        sleepingTipsList.add(fragment);
        //*****************
        arrForBabyTipsModel = getResources().getStringArray(R.array.sleeping_tip_6);
        babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        sleepingTipsList.add(fragment);

        //*****************
        arrForBabyTipsModel = getResources().getStringArray(R.array.sleeping_tip_7);
        babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        sleepingTipsList.add(fragment);
        //*****************
        arrForBabyTipsModel = getResources().getStringArray(R.array.sleeping_tip_8);
        babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        sleepingTipsList.add(fragment);
        //*****************
        arrForBabyTipsModel = getResources().getStringArray(R.array.sleeping_tip_9);
        babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        sleepingTipsList.add(fragment);

        //*****************
        arrForBabyTipsModel = getResources().getStringArray(R.array.sleeping_tip_10);
        babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        sleepingTipsList.add(fragment);

        //*****************
        arrForBabyTipsModel = getResources().getStringArray(R.array.sleeping_tip_11);
        babyTipsModel = new BabyTipsModel(arrForBabyTipsModel[0], arrForBabyTipsModel[1]);
        bundle = new Bundle();
        bundle.putSerializable("tips", babyTipsModel);
        fragment = new BabyTipsItemFragment();
        fragment.setArguments(bundle);
        sleepingTipsList.add(fragment);
    }//emd pf omotS;ee[ingBabyList

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_baby_tips_sleeping:
                if (v.isSelected()) {
                    return;
                }
                txt_baby_tips_safety.setSelected(false);
                txt_baby_tips_sleeping.setSelected(true);
                viewpager_babytips_sleeping.setVisibility(View.VISIBLE);
                viewpager_babytips_safety.setVisibility(View.GONE);
                if (viewpager_babytips_sleeping.getCurrentItem() == 0) {
                    img_baby_tips_ic_left.setVisibility(View.GONE);
                    img_baby_tips_ic_right.setVisibility(View.VISIBLE);
                } else if (viewpager_babytips_sleeping.getCurrentItem() < (sleepingTipsList.size() - 1)) {
                    img_baby_tips_ic_left.setVisibility(View.VISIBLE);
                    img_baby_tips_ic_right.setVisibility(View.VISIBLE);
                } else {
                    img_baby_tips_ic_left.setVisibility(View.VISIBLE);
                    img_baby_tips_ic_right.setVisibility(View.GONE);
                }
                break;

            case R.id.txt_baby_tips_safety:
                if (v.isSelected()) {
                    return;
                }
                txt_baby_tips_sleeping.setSelected(false);
                txt_baby_tips_safety.setSelected(true);
                if (safetyTipsAdopter == null) {
                    initSafetyBabyList();
                    safetyTipsAdopter = new BabyTipsAdopter(getActivity(), getChildFragmentManager(), safetyTipsList);
                    viewpager_babytips_safety.setAdapter(safetyTipsAdopter);
                }
                viewpager_babytips_sleeping.setVisibility(View.GONE);
                viewpager_babytips_safety.setVisibility(View.VISIBLE);
                if (viewpager_babytips_safety.getCurrentItem() == 0) {
                    img_baby_tips_ic_left.setVisibility(View.GONE);
                    img_baby_tips_ic_right.setVisibility(View.VISIBLE);
                } else if (viewpager_babytips_safety.getCurrentItem() < (safetyTipsList.size() - 1)) {
                    img_baby_tips_ic_left.setVisibility(View.VISIBLE);
                    img_baby_tips_ic_right.setVisibility(View.VISIBLE);
                } else {
                    img_baby_tips_ic_left.setVisibility(View.VISIBLE);
                    img_baby_tips_ic_right.setVisibility(View.GONE);
                }

                break;
            case R.id.img_baby_tips_ic_left:
                if (txt_baby_tips_safety.isSelected()) {
                    viewpager_babytips_safety.setCurrentItem(viewpager_babytips_safety.getCurrentItem() - 1);
                } else {
                    viewpager_babytips_sleeping.setCurrentItem(viewpager_babytips_sleeping.getCurrentItem() - 1);
                }
                break;
            case R.id.img_baby_tips_ic_right:
                if (txt_baby_tips_safety.isSelected()) {
                    viewpager_babytips_safety.setCurrentItem(viewpager_babytips_safety.getCurrentItem() + 1);
                } else {
                    viewpager_babytips_sleeping.setCurrentItem(viewpager_babytips_sleeping.getCurrentItem() + 1);
                }
                break;
        }
    }//end of onClick


}
