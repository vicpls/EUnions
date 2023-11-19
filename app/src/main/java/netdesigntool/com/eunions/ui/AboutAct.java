package netdesigntool.com.eunions.ui;

import android.app.Activity;
import android.content.Context;

import com.mcsoft.aboutactivity.AboutActivityBuilder;
import com.mcsoft.aboutactivity.NoticesParcelable;

import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.License;
import de.psdev.licensesdialog.model.Notice;
import netdesigntool.com.eunions.BuildConfig;
import netdesigntool.com.eunions.R;

public class AboutAct {


    public void startAboutAct(Activity act) {

        final NoticesParcelable notices = new NoticesParcelable();

        License apacheLic2 = new ApacheSoftwareLicense20();

        notices.addNotice(
                new Notice("Privacy Policy",
                        "https://raw.githubusercontent.com/vicpls/PPolicy/main/PPolicy-4gp.txt",
                        null,
                        new PrivacyPolicy()
                ));

        notices.addNotice(
                new Notice("AboutActivity"
                        , "https://github.com/biagiopietro/AboutActivity"
                        , "Copyright 2018 biagiopietro"
                        , apacheLic2)
        );

        notices.addNotice(
                new Notice("MPAndroidChart"
                , "https://github.com/PhilJay/MPAndroidChart"
                , "Copyright 2020 Philipp Jahoda"
                , apacheLic2));

        notices.addNotice(
                new Notice("Flag Icon"
                , "http://freeflagicons.com"
                , "Copyright © 2012—2019 Redpixart LLC"
                , new Redpixart())
        );

        notices.addNotice(
                new Notice("Wikidata"
                , "www.wikidata.org"
                , "No Copyright"
                , new CC0()));


        new AboutActivityBuilder.Builder(act)
                .showAppVersion(true, "Version: ", BuildConfig.VERSION_NAME)
                .setAppLogo(R.mipmap.eur_union_192)
                .setCompanyLogo(R.mipmap.eur_union_192)
                .showRateApp(true, act.getPackageName(), "Please, rate my app")
                .showLicense(true, notices, "Credit & Privacy Policy", "Credit & Privacy Policy", "Close")
                //.showLicense(true, notices2, "Credit & Privacy Policy", "Credit & Privacy Policy", "Close")
                .showGeneral(true
                        , "EUnion"
                        , act.getPackageName()
                        , act.getString(R.string.developed)
                        , act.getString(R.string.thanks)
                        , act.getString(R.string.share))

                .showAboutActivity();
    }


    static final private class PrivacyPolicy extends License {
        @Override
        public String getName() {return "PrivacyPolicy";}
        @Override
        public String readSummaryTextFromResources(Context context) {return "";}
        @Override
        public String readFullTextFromResources(Context context) {return "";}
        @Override
        public String getVersion() {return "1.0";}
        @Override
        public String getUrl() {
            return "https://github.com/vicpls/etc/blob/main/PPolicy-4gp.txt";
        }
    }


    static final private class Redpixart extends License {

        @Override
        public String getName() {
            return "Redpixart License Agreement for Royalty Free images";
        }

        @Override
        public String readSummaryTextFromResources(Context context) {
            return "Free for non-commercial use.";
        }

        @Override
        public String readFullTextFromResources(Context context) {
            return "";
        }

        @Override
        public String getVersion() {
            return "";
        }

        @Override
        public String getUrl() {
            return "http://freeflagicons.com/agreement/";
        }
    }

    final private class CC0 extends License {

        @Override
        public String getName() {
            return "Creative Commons CC0 License";
        }

        @Override
        public String readSummaryTextFromResources(Context context) {
            return context.getResources().getString(R.string.cc0_summary);
        }

        @Override
        public String readFullTextFromResources(Context context) {
            return "";
        }

        @Override
        public String getVersion() {
            return "1.0";
        }

      
        @Override
        public String getUrl() {
            return "https://creativecommons.org/publicdomain/zero/1.0/";
        }
    }
}