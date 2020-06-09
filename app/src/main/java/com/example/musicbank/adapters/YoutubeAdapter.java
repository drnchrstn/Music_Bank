package com.example.musicbank.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicbank.Objects.YoutubeResult;
import com.example.musicbank.R;
import com.example.musicbank.Services.YoutubeWidgetService;
import com.example.musicbank.SongLyricsActivity;
import com.example.musicbank.Utils.PreferenceUtils;
import com.example.musicbank.Utils.YoutubeUtils;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.ViewHolder>{

    private Context context;
    private List<YoutubeResult> youtubeResults = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private String searchword;


    public YoutubeAdapter(Context context, List<YoutubeResult> youtubeResults, String searchword) {
        this.context = context;
        this.youtubeResults = youtubeResults;
        this.searchword = searchword;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_item, parent, false);
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        YoutubeResult youtubeResult = youtubeResults.get(position);
        holder.tvTitle.setText(youtubeResult.getTitle());
        holder.tvChannel.setText(youtubeResult.getChannel());
        holder.tvLength.setText(YoutubeUtils.getDuration(youtubeResult.getVideolength()));
        if (youtubeResult.isLive()) {
            holder.tvViews.setText(context.getResources().getString(R.string.live_text));
            holder.tvViews.setTextColor(context.getResources().getColor(R.color.roundRed));
        }else {
            holder.tvViews.setText(youtubeResult.getViews() == null ? "0":YoutubeUtils.format(youtubeResult.getViews().longValue()));
            holder.tvViews.setTextColor(context.getResources().getColor(R.color.default_text));
        }

        holder.playcont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT){
                    Intent intent = null;
                    try{
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" +youtubeResult.getVideoid()));
                        context.startActivity(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + youtubeResult.getVideoid()));
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)){
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + context.getPackageName()));
                        ((SongLyricsActivity)context).startActivityForResult(intent, 102);


                    }else{
                        PreferenceUtils.setYoutubeTrack(context, youtubeResult.getVideoid());
                        context.stopService(new Intent(context, YoutubeWidgetService.class));
                        context.startService(new Intent(context, YoutubeWidgetService.class));
                    }
                }

            }
        });


        Glide.with(context)
                .load(youtubeResult.getThumbnail())
                .placeholder(context.getResources().getDrawable(R.drawable.youtube_search))
                .centerCrop()
                .into(holder.img);


    }

    @Override
    public int getItemCount() {
        return youtubeResults.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private RelativeLayout playcont;
        private TextView tvTitle, tvChannel, tvViews, tvLength;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.img_youtube);
            tvTitle = (TextView)itemView.findViewById(R.id.youtube_title);
            tvChannel = (TextView)itemView.findViewById(R.id.youtube_uploader);
            tvViews = (TextView)itemView.findViewById(R.id.youtube_views);
            tvLength = (TextView)itemView.findViewById(R.id.youtube_length);
            playcont = (RelativeLayout) itemView.findViewById(R.id.playercon);

        }
    }


    public interface OnItemClickListener {
        void onItemClick(YoutubeResult youtubeResult);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }
}
