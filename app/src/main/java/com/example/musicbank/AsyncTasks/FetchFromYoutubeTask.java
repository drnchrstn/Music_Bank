package com.example.musicbank.AsyncTasks;

import android.content.Context;
import android.content.Intent;

import com.example.musicbank.AppConfig.Config;
import com.example.musicbank.Objects.YoutubeResult;
import com.example.musicbank.Utils.YoutubeUtils;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.com.google.common.base.Joiner;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FetchFromYoutubeTask extends BaseAsyncTask<Void, Void, List<YoutubeResult>>{

    private static YouTube youtube;
    private static final long NUMBER_OF_VIDEOS_RETURNED = 10;
    private String nextpageToken;
    private Context context;
    private String keyword;


    public FetchFromYoutubeTask(Response.Listener<List<YoutubeResult>> listener, Context context,String keyword, String nextpageToken) {
        super(listener, context);

        this.context = context;
        this.keyword = keyword;
        this.nextpageToken = nextpageToken;

    }

    @Override
    protected Response<List<YoutubeResult>> doInBackground(Void... voids) {
        try {
//            return Response.success(getDataFromApi());
            Intent intent = new Intent(YoutubeUtils.ACTION_YOUTUBE_BROADCAST);
            intent.putExtra(YoutubeUtils.PUTEXTRA_BROADCAST, (Serializable)getDataFromApi());
            context.sendBroadcast(intent);
            return null;
        } catch (Exception e) {
//            mLastError = e;
            cancel(true);
            return null;
        }
    }



    private List<YoutubeResult> getDataFromApi() throws IOException {
        List<YoutubeResult> youtubeResultList = new ArrayList<>();
        // Get a list of up to 10 files.
        // This object is used to make YouTube Data API requests. The last
        // argument is required, but since we don't need anything
        // initialized when the HttpRequest is initialized, we override
        // the interface and provide a no-op function.
        youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName(context.getPackageName()).build();
        if(keyword !=null && keyword.length() >0){
            youtubeResultList = searchfuntion();
        }
        return youtubeResultList;
    }




    public List<YoutubeResult> searchfuntion(){
        List<YoutubeResult> youtubeResultList = new ArrayList<>();
        try {
            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list("id,snippet");

            // Set your developer key from the {{ Google Cloud Console }} for
            // non-authenticated requests. See:
            // {{ https://cloud.google.com/console }}
            search.setKey(Config.API_YOUTUBE_KEY);
            search.setQ(keyword);
            search.getOauthToken();
            // Restrict the search results to only include videos. See:
            // https://developers.google.com/youtube/v3/docs/search/list#type
            search.setType("video");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
            search.setPageToken(nextpageToken);
            // To increase efficiency, only retrieve the fields that the
            // application uses.
            search.setFields("items(id/videoId), nextPageToken");

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            List<String> videoIds = new ArrayList<String>();

            if (searchResultList != null) {
                for (SearchResult searchResult : searchResultList) {
                    videoIds.add(searchResult.getId().getVideoId());
                }
                Joiner stringJoiner = Joiner.on(',');
                String videoId = stringJoiner.join(videoIds);
                YouTube.Videos.List listVideosRequest = youtube.videos().list("snippet, contentDetails, statistics").setId(videoId);
                listVideosRequest.setKey(Config.API_YOUTUBE_KEY);
                listVideosRequest.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
                //listVideosRequest.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url/statistics/viewCount,statistics)");
                VideoListResponse listResponse = listVideosRequest.execute();
                List<Video> videoList = listResponse.getItems();
                if (videoList != null) {
                    for(int i = 0; i < videoList.size(); i++){
                        YoutubeResult youtubeResult = new YoutubeResult(videoList.get(i), searchResponse.getNextPageToken());
                        youtubeResultList.add(youtubeResult);
                    }

                }
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return youtubeResultList;
    }


}
