package com.testninja.selenium;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class VideoPlayerTest {

    private Youtube youtube;

    @BeforeClass
    public void setUp() {
        youtube = new Youtube();
        youtube.initialize();
    }

    @AfterClass
    public void tearDown() {
        youtube.close();
    }


    @BeforeMethod
    public void resetVideo() {
        youtube.openVideo();
    }

    @Test(priority = 1)
    public void TestYoutubePlayerPlayFunctionality() {

        double playbackSpeed = 1;

        youtube.playVideo();

        /*Wait for 4 seconds and verify currently displayed frame is "Frame 4"*/
        assertVideoFrame(0, 4, playbackSpeed);

        /*Wait for addition 10 seconds and verify currently displayed frame is "Frame 14"*/
        youtube.playVideo();
        assertVideoFrame(4, 10, playbackSpeed);
    }


    @Test(priority = 2)
    public void TestYoutubePlayerPlayBackSpeedFunctionality() {

        double playbackSpeed = 1.75;

        youtube.setPlaybackSpeed(playbackSpeed);

        /*Wait for 4 seconds and verify currently displayed frame is "Frame 8" or "Frame 9"*/
        youtube.playVideo();
        assertVideoFrame(0, 4, playbackSpeed);

        /*Wait for addition 10 seconds and verify currently displayed frame is "Frame 26" or "Frame 27"*/
        youtube.playVideo();
        assertVideoFrame(4, 10, playbackSpeed);
    }

    private void assertVideoFrame(int previousTimeInSeconds, long sleepTimeInSeconds, double playBackSpeed) {

        sleep(sleepTimeInSeconds * 1000);
        youtube.pauseVideo();

        Integer currentTimeInSeconds = youtube.getCurrentTimeInSeconds();
        String currentFrameText = OCRUtil.getTextFromImage(youtube.getVideoFrame());

        double expectedTime = (previousTimeInSeconds + sleepTimeInSeconds) * playBackSpeed;
        Assert.assertTrue(currentTimeInSeconds >= expectedTime, "Expected time is " + expectedTime + " and actual time is " + currentTimeInSeconds);
        Assert.assertEquals(currentFrameText, String.format("Frame %s", currentTimeInSeconds));
    }

    private void sleep(long timeOut) {
        try {
            Thread.sleep(timeOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
