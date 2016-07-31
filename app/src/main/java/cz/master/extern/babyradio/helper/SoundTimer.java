package cz.master.extern.babyradio.helper;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.text.DecimalFormat;

import cz.master.extern.babyradio.ui.HomeActivity;

/**
 * Created by Yasir Iqbal on 7/26/2016.
 */
public class SoundTimer extends CountDownTimer {
    TextView textViewForCountDownTimer;
    HomeActivity homeActivity;


    public static SoundTimer soundTimerObj;

    public static SoundTimer getSoundTimerObj(HomeActivity homeActivity, long millisInFuture, TextView textViewForCountDownTimer) {
        if (soundTimerObj == null) {
            soundTimerObj = new SoundTimer(millisInFuture);
        }
        soundTimerObj.textViewForCountDownTimer = textViewForCountDownTimer;
        soundTimerObj.homeActivity = homeActivity;
        return soundTimerObj;
    }

    /**
     * @param millisInFuture The number of millis in the future from the call
     *                       to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                       is called.
     */
    private SoundTimer(long millisInFuture) {
        super(millisInFuture, 1000);

    }

    private long seconds;
    private long minutes;
    private long hours;

    @Override
    public void onTick(final long millisUntilFinished) {
        seconds = millisUntilFinished / 1000;
        minutes = seconds / 60;
        seconds = seconds % 60;
        hours = minutes / 60;
        minutes = minutes % 60;
        final String temp = setTimerTwoDigit(hours) + ":" + setTimerTwoDigit(minutes)
                + ":" + setTimerTwoDigit(seconds);
        if (textViewForCountDownTimer != null) {
            textViewForCountDownTimer.post(new Runnable() {
                @Override
                public void run() {
                    textViewForCountDownTimer.setText(temp);
                }
            });
        }
        if (textForBabyRadioScreen != null) {
            textForBabyRadioScreen.setText("Sound Timer: " + temp);
        }

    }//end of onTick

    @Override
    public void onFinish() {
        homeActivity.stopMusicIfPlayed();
    }//end of onFinish

    private String setTimerTwoDigit(long num) {
        DecimalFormat formatter = new DecimalFormat("00");
        return formatter.format(num);
    }

    public void resetTimer() {
        if (soundTimerObj != null)
            soundTimerObj.cancel();
        soundTimerObj = null;
    }

    TextView textForBabyRadioScreen;

    public void setTextForBabyRadioScreen(TextView textForBabyRadioScreen) {
        this.textForBabyRadioScreen = textForBabyRadioScreen;
    }//end of func
}
