package com.testninja.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;

public class Youtube {

    private WebDriver driver;
    private static final String videoUrl = "https://youtu.be/ayjsGkcwWC4";
    private static final By videoElement = By.cssSelector(".html5-main-video");
    private static final By playButton = By.cssSelector("button.ytp-play-button");
    private static final By currentPlayTime = By.cssSelector("span.ytp-time-current");
    private static final By settingsIcon = By.cssSelector("button.ytp-settings-button");

    public void initialize() {
        System.setProperty("webdriver.chrome.driver", "/Users/sudharsan/Documents/Applications/chromedriver");
        System.setProperty("logback.configurationFile", System.getProperty("user.dir") + "/logback.xml");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(Arrays.asList("--log-level=OFF", "--silent", "--headless true"));
        driver = new ChromeDriver(chromeOptions);
    }

    public void openVideo() {
        driver.get(videoUrl);
    }

    public void close() {
        driver.quit();
    }


    public void playVideo() {
        try {
            if (!isVideoPlaying()) {
                clickPlayButton();
            }
        } catch (Exception e) {

        }
    }

    public void pauseVideo() {
        try {
            if (isVideoPlaying()) {
                clickPlayButton();
            }
            Thread.sleep(1000);
        } catch (Exception e) {

        }
    }

    public Integer getCurrentTimeInSeconds() {
        try {
            hoverVideoPlayer();
            return Integer.valueOf(driver.findElement(currentPlayTime).getText().split(":")[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getVideoFrame() {
        try {
            return ((TakesScreenshot) driver.findElement(videoElement)).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setPlaybackSpeed(double  speed) {
        try {
            hoverVideoPlayer();
            driver.findElement(settingsIcon).click();
            WebElement playBackOption = waitForElementToBeDisplayed(By.xpath(".//*[contains(@class,'ytp-menuitem-label')][text()='Playback speed']"));
            playBackOption.click();
            WebElement playBackSpeed = waitForElementToBeDisplayed(By.xpath(String.format(".//*[contains(@class,'ytp-menuitem-label')][text()='%s']", speed)));
            playBackSpeed.click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isVideoPlaying(WebElement playButtonEle) {
        return playButtonEle.getAttribute("title").contains("Pause");
    }

    public boolean isVideoPlaying() {
        return isVideoPlaying(driver.findElement(playButton));
    }

    private void clickPlayButton() {
        WebElement playButtonEle = waitForPlayButton();
        playButtonEle.click();
    }


    private WebElement waitForPlayButton() {
        return new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.presenceOfElementLocated(playButton));
    }

    private WebElement waitForElementToBeDisplayed(By locator) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.visibilityOf(element));
        return element;
    }

    private WebElement waitForControlsToBeDisplayed() {
        return waitForElementToBeDisplayed(playButton);
    }

    private void hoverVideoPlayer() {
        new Actions(driver).moveToElement(driver.findElement(videoElement)).perform();
        new Actions(driver).moveToElement(driver.findElement(videoElement)).perform();
        waitForControlsToBeDisplayed();
    }
}
