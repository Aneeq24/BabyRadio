package cz.master.extern.babyradio.adopter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Yasir Iqbal on 7/3/2016.
 */
public class BabyTipsAdopter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragmentList;

    Context context;
    FragmentManager fragmentManager;

    public BabyTipsAdopter(Context context, FragmentManager fm,
                           ArrayList<Fragment> fragmentList) {
        super(fm);
        this.context = context;
        this.fragmentManager = fm;
        this.fragmentList = fragmentList;

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int posiion) {

        return fragmentList.get(posiion);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return fragmentList.size();
    }
}
