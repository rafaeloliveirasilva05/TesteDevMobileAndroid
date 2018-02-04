package com.example.rafael.testedevmobileandroid.interfaces;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

/**
 * Created by rafael on 31/01/18.
 */

public interface BannerAdListener {

    // Sent when the banner has successfully retrieved an ad.
    public void onBannerLoaded(MoPubView banner);

    // Sent when the banner has failed to retrieve an ad. You can use the MoPubErrorCode value to diagnose the cause of failure.
    public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode);

    // Sent when the user has tapped on the banner.
    public void onBannerClicked(MoPubView banner);

    // Sent when the banner has just taken over the screen.
    public void onBannerExpanded(MoPubView banner);

    // Sent when an expanded banner has collapsed back to its original size.
    public void onBannerCollapsed(MoPubView banner);

}
