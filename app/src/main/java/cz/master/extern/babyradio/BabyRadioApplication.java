package cz.master.extern.babyradio;

import android.app.Application;

import com.splunk.mint.Mint;

import cz.masterapp.massdkandroid.Family;
import cz.masterapp.massdkandroid.HamburgerMenu;

/**
 * Created by Yasir Iqbal on 6/15/2016.
 */
public class BabyRadioApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HamburgerMenu.callOnCreate(this);
        HamburgerMenu.initialize(
                "wbxud3g7d3mq3xg6xqwtu2zdkgfur3u9",
                Family.NO_FAMILY,
                "UA-45339522-3",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlMX14ASUvZVHLgVmCrYu27PiYHfmKhyf7LZhuoHmCQSLbJCSqfVddymlFmRxtQ2gSCKvRQIxiESCNmT2GLg2FsVxxtWtkW3Urtl/p8igHGBNThtCaNkIAP8zoLDhT1XrLoE5PdB2j5LAPbbbytSsu6ZUxJ/xRDuUePVQ3gx0VXR/x2IAOIZXpdIwqlfEGIxbOYITJDTT5x8Pq9yMfphFI3YHpDlXs1ePxI1d4o0D7ikcThLKRfLKkyP6ufATHhOCHHrnqdMP/QKtK5AS3qJQGfTBb4uIUMcWsQbCJAiw/E8ioCHL+ZIWEGnRcW6NM+YMS/zsC/R9Zyoa2QgEA6/bDQIDAQAB",
                "android.test.purchased",
                true,
                getApplicationContext()
        );
        Mint.initAndStartSession(this, "1ff7c79c");
    }//end of oncreate
}
