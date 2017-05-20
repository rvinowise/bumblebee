package game.engine.ads;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.rvinowise.bumblebee_jumper.R; //todo make it more universal


public class Ads extends AdListener implements RewardedVideoAdListener {

    private final Context context;
    //private final FirebaseAnalytics firebaseAnalytics;
    private final InterstitialAd interstitialAd;
    private final RewardedVideoAd rewardedAd;

    public Ads(Context in_context) {
        context = in_context;

        //firebaseAnalytics = FirebaseAnalytics.getInstance(context);

        MobileAds.initialize(
                context.getApplicationContext(), context.getString(R.string.app_admob_id));

        interstitialAd = new InterstitialAd(context);
        //interstitialAd.setAdUnitId(context.getString(R.string.interstitial_ad_test));
        interstitialAd.setAdUnitId(context.getString(R.string.bumblebee_interstitial_ad_1));
        interstitialAd.setAdListener(this);

        rewardedAd = MobileAds.getRewardedVideoAdInstance(context);
        rewardedAd.setRewardedVideoAdListener(this);


    }

    public void init() {

    }

    public void request_interstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
    }
    public void request_rewarded() {
        AdRequest adRequest = new AdRequest.Builder().build();
        rewardedAd.loadAd(
                //context.getString(R.string.bumblebee_rewarded_ad),
                context.getString(R.string.rewarded_ad_test),
                adRequest);
        //rewardedAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
    }

    public void can_show_interstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }
    public void can_show_rewarded() {
        if (rewardedAd.isLoaded()) {
            rewardedAd.show();
        }
    }



    public void onResume() {

    }
    public void onPause() {

    }


    @Override
    public void onAdClosed() {
        if (!interstitialAd.isLoading()) {
            request_interstitial();
        }
    }
    @Override
    public void onAdLeftApplication() {
        Log.d("ADS","onAdLeftApplication");
    }


    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        request_rewarded();
    }

    @Override
    public void onRewarded(RewardItem reward) {
        Toast.makeText(context,
                "onRewarded! currency: " + reward.getType() + "  amount: " +
                reward.getAmount(), Toast.LENGTH_SHORT).show();
        request_rewarded();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        request_rewarded();
    }
}
