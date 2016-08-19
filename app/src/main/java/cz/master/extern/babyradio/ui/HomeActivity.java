package cz.master.extern.babyradio.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.splunk.mint.Mint;

import cz.master.extern.babyradio.R;
import cz.master.extern.babyradio.fragments.BabyRadioFragment;
import cz.master.extern.babyradio.fragments.BabyTipsFragment;
import cz.master.extern.babyradio.fragments.LogFragment;
import cz.master.extern.babyradio.fragments.RattleFragment;
import cz.master.extern.babyradio.fragments.SoundTimerFragment;
import cz.master.extern.babyradio.helper.DbHelper;
import cz.master.extern.babyradio.helper.Fonts;
import cz.master.extern.babyradio.helper.PermissionHandler;
import cz.master.extern.babyradio.helper.SoundTimer;
import cz.master.extern.babyradio.ratioresolver.CalculateRatio;
import cz.master.extern.babyradio.resources.HomeResources;
import cz.master.extern.babyradio.settings.Utils;
import cz.masterapp.massdkandroid.HamburgerMenu;
import cz.masterapp.massdkandroid.HamburgerMenuItem;
import cz.masterapp.massdkandroid.PromotionActivity;

public class HomeActivity extends PromotionActivity {
    private ActionBarDrawerToggle mDrawerToggle;
    private HamburgerMenu hamburgerMenu;
    public TextView txt_homeactivity_title, txt_homeactivity_clear;
    HomeResources homeResourcesObj;
    RattleFragment rattleFragmentObj;
    BabyRadioFragment babyRadioFragmentObj;
    BabyTipsFragment babyTipsFragmentObj;
    LogFragment logFragmentObj;
    SoundTimerFragment soundTimerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Utils.context = this;
        CalculateRatio.calculateRatio(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        initHamburgMenu();
        init();
    }//end of on Create

    /*
    *init funtion is to initialize all activity funcitons related to xml for setting screen
     */
    private void init() {
        homeResourcesObj = new HomeResources(this);
    }

    DbHelper dbHelper;

    /*
    *<p>This function is to init HamburgMenu From Library</p>
     */
    private void initHamburgMenu() {
         /* Display hamburger */

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.template_homeactivity_actionbar);
        txt_homeactivity_title = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.txt_homeactivity_title);
        txt_homeactivity_clear = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.txt_homeactivity_clear);
        txt_homeactivity_title.setTypeface(Fonts.getUbuntuLightTypeFace());
        txt_homeactivity_clear.setTypeface(Fonts.getUbuntuLightTypeFace());
        txt_homeactivity_clear.setVisibility(View.GONE);
        txt_homeactivity_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                alertDialogBuilder.setTitle("Clear Log");
                alertDialogBuilder.setMessage("Are you sure you want to clear Log Events?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dbHelper == null)
                            dbHelper = new DbHelper(HomeActivity.this);
                        dbHelper.deleteAllLog();
                        if (logFragmentObj != null) {
                            logFragmentObj.deleteAllLog();
                        }
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialogBuilder.show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        hamburgerMenu = (HamburgerMenu) findViewById(R.id.hamburger_menu);
        int colorPrimary;
        if (Build.VERSION.SDK_INT < 23) {
            colorPrimary = getResources().getColor(R.color.colorPrimary);

        } else {
            colorPrimary = getResources().getColor(R.color.colorPrimary, null);
        }
        hamburgerMenu.useAppTheme();
        Drawable logDrawable, babyRadioDrawable, soundTimerDrawable, babyTipsDrawable, rattleDrawable;
        if (Build.VERSION.SDK_INT < 21) {
            logDrawable = getResources().getDrawable(R.drawable.ic_menu_log);
            babyRadioDrawable = getResources().getDrawable(R.drawable.ic_menu_babyradio);
            soundTimerDrawable = getResources().getDrawable(R.drawable.ic_menu_soundtimer);
            babyTipsDrawable = getResources().getDrawable(R.drawable.ic_menu_babytips);
            rattleDrawable = getResources().getDrawable(R.drawable.ic_menu_restmoment);
        } else {
            logDrawable = getResources().getDrawable(R.drawable.ic_menu_log, null);
            babyRadioDrawable = getResources().getDrawable(R.drawable.ic_menu_babyradio, null);
            soundTimerDrawable = getResources().getDrawable(R.drawable.ic_menu_soundtimer, null);
            babyTipsDrawable = getResources().getDrawable(R.drawable.ic_menu_babytips, null);
            rattleDrawable = getResources().getDrawable(R.drawable.ic_menu_restmoment, null);
        }


        HamburgerMenuItem hamburgerMenuItem = new HamburgerMenuItem("Rattle", rattleDrawable);

        hamburgerMenu.addMenuItem(hamburgerMenuItem, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rattleFragmentObj == null)
                    rattleFragmentObj = new RattleFragment();
                txt_homeactivity_title.setText("Rattle");
                txt_homeactivity_clear.setVisibility(View.GONE);
                FragmentTransaction fragmentTransiction = getSupportFragmentManager().beginTransaction();
                fragmentTransiction.replace(R.id.container_homeactivity_fragment_container, rattleFragmentObj);
                fragmentTransiction.commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }, 0);
        hamburgerMenuItem = new HamburgerMenuItem("Event Log", logDrawable);

        hamburgerMenu.addMenuItem(hamburgerMenuItem, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logFragmentObj == null)
                    logFragmentObj = new LogFragment();
                txt_homeactivity_title.setText("Event Log");
                txt_homeactivity_clear.setVisibility(View.VISIBLE);
                FragmentTransaction fragmentTransiction = getSupportFragmentManager().beginTransaction();
                fragmentTransiction.replace(R.id.container_homeactivity_fragment_container, logFragmentObj);
                fragmentTransiction.commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }, 0);

        hamburgerMenuItem = new HamburgerMenuItem("Baby Tips", babyTipsDrawable);

        hamburgerMenu.addMenuItem(hamburgerMenuItem, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (babyTipsFragmentObj == null)
                    babyTipsFragmentObj = new BabyTipsFragment();
                txt_homeactivity_title.setText("Baby Tips");
                txt_homeactivity_clear.setVisibility(View.GONE);
                FragmentTransaction fragmentTransiction = getSupportFragmentManager().beginTransaction();
                fragmentTransiction.replace(R.id.container_homeactivity_fragment_container, babyTipsFragmentObj);
                fragmentTransiction.commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }, 0);
        hamburgerMenuItem = new HamburgerMenuItem("Sound Timer", soundTimerDrawable);

        hamburgerMenu.addMenuItem(hamburgerMenuItem, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSoundTimerFragment();
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }, 0);
        hamburgerMenuItem = new HamburgerMenuItem("Baby Radio", babyRadioDrawable);

        hamburgerMenu.addMenuItem(hamburgerMenuItem, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (babyRadioFragmentObj == null)
                    babyRadioFragmentObj = new BabyRadioFragment();
                txt_homeactivity_title.setText("Baby Radio");
                txt_homeactivity_clear.setVisibility(View.GONE);
                FragmentTransaction fragmentTransiction = getSupportFragmentManager().beginTransaction();
                fragmentTransiction.replace(R.id.container_homeactivity_fragment_container, babyRadioFragmentObj);
                fragmentTransiction.commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }, 0);
        //***********************************
        setupPromotions(hamburgerMenu);
        hamburgerMenu.setIconColor(colorPrimary);
        hamburgerMenu.setMenuItemTextColor(Color.parseColor("#76757D"));
        hamburgerMenu.setBackgroundColor(Color.parseColor("#F3F3F5"));
        //*************Here set first fragment
        if (babyRadioFragmentObj == null)
            babyRadioFragmentObj = new BabyRadioFragment();
        txt_homeactivity_title.setText("Baby Radio");
        FragmentTransaction fragmentTransiction = getSupportFragmentManager().beginTransaction();
        fragmentTransiction.replace(R.id.container_homeactivity_fragment_container, babyRadioFragmentObj);
        fragmentTransiction.commit();
        //**********************
    }//end f initHamburgMenu fucntion

    public void openSoundTimerFragment() {
        if (soundTimerFragment == null)
            soundTimerFragment = new SoundTimerFragment();
        txt_homeactivity_title.setText("Sound Timer");
        txt_homeactivity_clear.setVisibility(View.GONE);
        FragmentTransaction fragmentTransiction = getSupportFragmentManager().beginTransaction();
        fragmentTransiction.replace(R.id.container_homeactivity_fragment_container, soundTimerFragment);
        fragmentTransiction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopMusicIfPlayed();
    }//end of onpause

    public void stopMusicIfPlayed() {
        if (babyRadioFragmentObj != null) {
            babyRadioFragmentObj.txt_baby_radio_sounds_timer_bottom.setSelected(false);
            babyRadioFragmentObj.btn_babyradio_play_pause.setSelected(false);
            babyRadioFragmentObj.btn_babyradio_play_pause.setText("Play");
            babyRadioFragmentObj.insertMessageToDb("Automatic Stop");
            if (babyRadioFragmentObj.mediaPlayerObj != null) {
                try {
                    babyRadioFragmentObj.mediaPlayerObj.stop();
                    babyRadioFragmentObj.mediaPlayerObj.release();
                    babyRadioFragmentObj.mediaPlayerObj.reset();
                } catch (Exception e) {
                    e.printStackTrace();
                }//end of catch
                babyRadioFragmentObj.txt_baby_radio_sounds_timer_bottom.setText("Sound Timer");
            }//end of

            if (SoundTimer.soundTimerObj != null)
                SoundTimer.soundTimerObj.resetTimer();
        }
        if (soundTimerFragment != null) {
            soundTimerFragment.toStopSoundTimer();
        }

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case HamburgerMenu.BUY_REQUEST:
                hamburgerMenu.finishBuy(requestCode, resultCode, data);
                break;
            case HamburgerMenu.REGISTER_LOGIN_REQUEST:
                hamburgerMenu.loginRegistrationFinished(requestCode, resultCode, data);
                break;
            case HamburgerMenu.LOGIN_REQUEST:
                hamburgerMenu.loginFinished(resultCode);
                break;
            case HamburgerMenu.BROWSER_REQUEST:
                hamburgerMenu.refreshSmallBanner();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /*
    * We used tthis callback is to return to homepage either if it is not visible otherwise go to back main menu
     */
    @Override
    public void onBackPressed() {
        Fragment currentDisplayedFragment = getSupportFragmentManager().findFragmentById(R.id.container_homeactivity_fragment_container);
        if (currentDisplayedFragment instanceof BabyRadioFragment)
            super.onBackPressed();
        else {
            if (babyRadioFragmentObj == null)
                babyRadioFragmentObj = new BabyRadioFragment();
            txt_homeactivity_title.setText("Baby Radio");
            FragmentTransaction fragmentTransiction = getSupportFragmentManager().beginTransaction();
            fragmentTransiction.replace(R.id.container_homeactivity_fragment_container, babyRadioFragmentObj);
            fragmentTransiction.commit();
        }
    }//end of onBackPressed

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) || (keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            if (babyRadioFragmentObj != null) {
                babyRadioFragmentObj.initSeekBarVolume();
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return false;
    }

    /**
     * For Runtime permision callback
     *
     * @param requestCode  request code from where we create request
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionHandler.PERMISSIONS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (soundTimerFragment != null) {
                    soundTimerFragment.afterPermsissionGranted();
                } else {
                    Toast.makeText(this, "Kindly grant Audio Record permission for Baby Monitor to work properly.", Toast.LENGTH_LONG).show();
                }
            }
        }//end of if
    }//end of function

    public void restartSoundAfterBabyMonitor() {
        try {
            if (babyRadioFragmentObj != null) {
                if (BabyRadioFragment.mediaPlayerObj != null && !BabyRadioFragment.mediaPlayerObj.isPlaying()) {
                    babyRadioFragmentObj.startMediaFile(BabyRadioFragment.pathForMediaFile);
                    babyRadioFragmentObj.btn_babyradio_play_pause.setText("Stop");
                    babyRadioFragmentObj.btn_babyradio_play_pause.setSelected(true);
                }//when babyradio Obj is not null
            }
        } catch (Exception e) {
            Mint.logException(e);
        }
    }//end of function
}//end of class
