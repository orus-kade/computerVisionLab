package ru.sfedu.computervisionlab.utils;

import java.util.Locale;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.computervisionlab.Constants;


/**
 *
 * @author orus-kade
 */
public class Utils {
    
    private static final Logger logger = LogManager.getLogger();

    /**
     *
     * @return
     */
    public static Constants.OSType getOperatingSystemType() {
        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        logger.debug(OS + " is current OS");
        if ((OS.contains("mac")) || (OS.contains("darwin"))) {
            return Constants.OSType.MACOS;
        } else if (OS.contains("win")) {
            return Constants.OSType.WINDOWS;
        } else if (OS.contains("nux")) {
            return Constants.OSType.LINUX;
        } else {
            return Constants.OSType.OTHER;
        }
    }
    
    /**
     *
     * @param low
     * @param up
     * @return
     */
    public static int randomInt(int low, int up){
        Random rand = new Random();
        int randomNum = rand.nextInt((up - low) + 1) + low;
        return randomNum;
    }
}
