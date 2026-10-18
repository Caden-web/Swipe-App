package com.example.swipeapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the online videos displayed in the app.
 */
public final class VideoRepository {

    private VideoRepository() {
    }

    /**
     * Returns the videos for the vertical feed.
     *
     * @return list of online videos
     */
    public static List<Video> getVideos() {

        List<Video> videos = new ArrayList<>();

        videos.add(new Video(
                "001",
                "Sunset",
                "A sunset on the horizon",
                "https://filesamples.com/samples/video/mp4/sample_960x540.mp4"
        ));

        videos.add(new Video(
                "002",
                "Beach Day",
                "A girl overlooking a sandy beach",
                "https://filesamples.com/samples/video/mp4/sample_640x360.mp4"
        ));

        videos.add(new Video(
                "003",
                "Vast Ocean",
                "Endless waves of the Pacific Ocean",
                "https://filesamples.com/samples/video/mp4/sample_960x400_ocean_with_audio.mp4"
        ));

        videos.add(new Video(
                "004",
                "Surfs Up",
                "A small video of a guy surfing",
                "https://filesamples.com/samples/video/mp4/sample_1280x720_surfing_with_audio.mp4"
        ));

        videos.add(new Video(
                "005",
                "Rocky Shore",
                "Drone footage of a rocky island",
                "https://filesamples.com/samples/video/mp4/sample_1920x1080.mp4"
        ));



        return videos;
    }
}
