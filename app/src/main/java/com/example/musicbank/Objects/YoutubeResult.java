package com.example.musicbank.Objects;

import java.io.Serializable;
import java.math.BigInteger;
import com.google.api.services.youtube.model.Video;

public class YoutubeResult implements Serializable {

    String videoid;
    String thumbnail;
    String title;
    BigInteger views;
    String videolength;
    String channel;
    String nexpagetoken;
    Boolean isLive = false;




    public YoutubeResult(Video video, String nextpagetoken) {
        this.videoid = video.getId();
        this.thumbnail = video.getSnippet().getThumbnails().getMedium().getUrl();
        this.title = video.getSnippet().getTitle();
        this.views = video.getStatistics().getViewCount();
        this.videolength = video.getContentDetails().getDuration();
        this.channel = video.getSnippet().getChannelTitle();
        this.nexpagetoken = nextpagetoken;
        this.isLive = video.getSnippet().getLiveBroadcastContent().equals("live")?true:false;
    }


    public String getVideoid() {
        return videoid;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public BigInteger getViews() {
        return views;
    }

    public String getVideolength() {
        return videolength;
    }

    public String getChannel() {
        return channel;
    }

    public String getNexpagetoken() {
        return nexpagetoken;
    }

    public Boolean isLive() {
        return isLive;
    }

}
