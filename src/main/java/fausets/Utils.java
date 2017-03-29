package fausets;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Utils {
    public static void runScript(String string) {

        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        try {
            engine.eval(string);
        } catch (ScriptException e) {
            e.printStackTrace();
        }

    }

    public static WebElement waitToElementBeVisible(WebDriver driver, By id, int i, int it) {
        int limit = 0;
        while (driver.findElement(id) == null || limit > it) {
            try {
                driver.wait(limit);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            limit++;
        }
        return driver.findElement(id);
    }

    public static void bip(int beep, int waitb) {

        byte[] buf = new byte[1];
        AudioFormat af = new AudioFormat((float) 44100, 8, 1, true, false);
        SourceDataLine sdl;
        try {
            sdl = AudioSystem.getSourceDataLine(af);
            sdl.open();
            sdl.start();
            for (int i = 0; i < beep * (float) 44100 / 1000; i++) {
                double angle = i / ((float) 44100 / 440) * 2.0 * Math.PI;
                buf[0] = (byte) (Math.sin(angle) * 100);
                sdl.write(buf, 0, 1);
            }
            sdl.drain();
            sdl.stop();
            Thread.sleep(waitb * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));

    }

}
