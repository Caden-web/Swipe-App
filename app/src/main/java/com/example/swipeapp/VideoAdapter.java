package com.example.swipeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/** Creates swipeable pages containing a player surface and video metadata. */
public final class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    private final List<Video> videos;

    /** Stores the videos that appear in the vertical feed. */
    public VideoAdapter(List<Video> videos) { this.videos = videos; }

    /** Inflates the XML layout for one video page. */
    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
        return new VideoHolder(view);
    }

    /** Binds the video title, description, and ID to the page. */
    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        Video video = videos.get(position);
        holder.title.setText(video.getTitle());
        holder.description.setText(video.getDescription());
        holder.videoId.setText(holder.itemView.getContext().getString(R.string.video_id_format, video.getId()));
        holder.playerView.setPlayer(null);
    }

    /** Returns the number of video pages. */
    @Override
    public int getItemCount() { return videos.size(); }

    /** Holds views for a single video page. */
    public static final class VideoHolder extends RecyclerView.ViewHolder {
        public final PlayerView playerView;
        public final TextView title;
        public final TextView description;
        public final TextView videoId;
        public final View playOverlay;
        public final View loading;

        /** Finds the views declared in item_video.xml. */
        public VideoHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.playerView);
            title = itemView.findViewById(R.id.videoTitle);
            description = itemView.findViewById(R.id.videoDescription);
            videoId = itemView.findViewById(R.id.videoId);
            playOverlay = itemView.findViewById(R.id.playOverlay);
            loading = itemView.findViewById(R.id.loadingIndicator);
        }
    }
}
