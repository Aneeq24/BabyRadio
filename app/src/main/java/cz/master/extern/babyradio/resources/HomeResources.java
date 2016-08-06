package cz.master.extern.babyradio.resources;

import android.view.View;

import cz.master.extern.babyradio.R;
import cz.master.extern.babyradio.ui.HomeActivity;

/**
 * Created by Yasir Iqbal on 6/25/2016.
 */
public class HomeResources {
    HomeActivity context;
    View container_homeactivity_fragment_container;

    public HomeResources(HomeActivity context) {
        this.context = context;
        initResources();
    }//end of constructor

    /*
    *Here in function we all call findViewById to init Resources
     */
    private void initResources() {
        container_homeactivity_fragment_container = context.findViewById(R.id.container_homeactivity_fragment_container);
    }//end of initResources for homeactivity
}
