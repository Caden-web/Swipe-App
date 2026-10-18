package com.example.swipeapp;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.util.List;

/** Displays a vertical video feed and plays only the currently selected video. */
public final class MainActivity extends AppCompatActivity {
    private ViewPager2 videoPager;
    private VideoAdapter adapter;
    private List<Video> videos;
    private ExoPlayer player;
    private VideoAdapter.VideoHolder activeHolder;
    private boolean foreground;

    /** Initializes the vertical pager and its page-change callback. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videos = VideoRepository.getVideos();
        adapter = new VideoAdapter(videos);
        videoPager = findViewById(R.id.videoPager);
        videoPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        videoPager.setAdapter(adapter);
        videoPager.setOffscreenPageLimit(1);
        videoPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            /** Plays the newly selected page when the pager settles. */
            @Override
            public void onPageSelected(int position) {
                videoPager.post(() -> {
                    if (foreground) playPage(position);
                });
            }
        });
    }

    /** Creates the player when the activity enters the foreground. */
    @Override
    protected void onStart() {
        super.onStart();
        foreground = true;
        player = new ExoPlayer.Builder(this).build();
        player.addListener(new Player.Listener() {
            /** Updates the loading indicator and play/pause overlay. */
            @Override
            public void onPlaybackStateChanged(int state) { updateUi(); }

            /** Refreshes the overlay when playback starts or stops. */
            @Override
            public void onIsPlayingChanged(boolean isPlaying) { updateUi(); }

            /** Shows a readable message if a video cannot be streamed. */

            @Override
            public void onPlayerError(
                    @NonNull PlaybackException error) {

                android.util.Log.e(
                        "SwipeVideo",
                        "Playback failed: " + error.getMessage(),
                        error
                );
            }
        });
        videoPager.post(() -> playPage(videoPager.getCurrentItem()));
    }

    /** Attaches the player to the selected page and loads its online URL. */
    private void playPage(int position) {
        if (!foreground || player == null || position < 0 || position >= videos.size()) return;
        RecyclerView recyclerView = (RecyclerView) videoPager.getChildAt(0);
        RecyclerView.ViewHolder rawHolder = recyclerView.findViewHolderForAdapterPosition(position);
        if (!(rawHolder instanceof VideoAdapter.VideoHolder)) {
            videoPager.postDelayed(() -> {
                if (position == videoPager.getCurrentItem()) playPage(position);
            }, 100);
            return;
        }
        VideoAdapter.VideoHolder holder = (VideoAdapter.VideoHolder) rawHolder;
        if (activeHolder != null) {
            activeHolder.playerView.setPlayer(null);
            activeHolder.playOverlay.setOnClickListener(null);
            activeHolder.playOverlay.setVisibility(View.GONE);
            activeHolder.loading.setVisibility(View.GONE);
        }
        player.stop();
        player.clearMediaItems();
        activeHolder = holder;
        holder.description.setText(videos.get(position).getDescription());
        holder.playerView.setPlayer(player);
        holder.playOverlay.setOnClickListener(v -> togglePlayback());
        holder.playerView.setOnClickListener(v -> togglePlayback());

        String videoUrl = videos.get(position).getUrl();

        android.util.Log.d(
                "SwipeVideo",
                "Attempting to play: " + videoUrl
        );

        player.setMediaItem(MediaItem.fromUri(videoUrl));
        player.prepare();
        player.play();

        player.setMediaItem(MediaItem.fromUri(videos.get(position).getUrl()));
        player.setRepeatMode(Player.REPEAT_MODE_ONE);
        player.prepare();
        player.play();
        updateUi();
    }

    /** Toggles pause and resume for the visible video. */
    private void togglePlayback() {
        if (player == null) return;
        if (player.isPlaying()) player.pause();
        else player.play();
        updateUi();
    }

    /** Synchronizes the loading spinner and pause indicator with player state. */
    private void updateUi() {
        if (activeHolder == null || player == null) return;
        boolean buffering = player.getPlaybackState() == Player.STATE_BUFFERING;
        activeHolder.loading.setVisibility(buffering ? View.VISIBLE : View.GONE);
        activeHolder.playOverlay.setVisibility(!buffering && !player.isPlaying()
                ? View.VISIBLE : View.GONE);
    }

    /** Releases playback resources when the activity is no longer visible. */
    @Override
    protected void onStop() {
        foreground = false;
        if (activeHolder != null) {
            activeHolder.playerView.setPlayer(null);
            activeHolder = null;
        }
        if (player != null) {
            player.release();
            player = null;
        }
        super.onStop();
    }
}
