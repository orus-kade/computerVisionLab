package ru.sfedu.computervisionlab.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.computervisionlab.utils.Utils;
import ru.sfedu.computervisionlab.Constants;
import ru.sfedu.computervisionlab.utils.ConfigurationUtil;

/**
 *
 * @author orus-kade
 */
public class ImageApi {

    private static final Logger logger = LogManager.getLogger();
    private ConfigurationUtil config = new ConfigurationUtil(Constants.RESORCES_PATH);

    /**
     *
     * @throws Exception
     */
    public ImageApi() throws Exception{
        logger.info("Checking OS.....");
        // init the API with curent os..
        logger.debug(Utils.getOperatingSystemType() + "  lol");
        switch (Utils.getOperatingSystemType()) {
            case LINUX:
                logger.debug("  lol 1");
                System.load(config.getConfigurationEntry(Constants.PATH_TO_NATIVE_LIB_LINUX));
                logger.debug("  lol 2");
                break;
            case WINDOWS:
                throw new Exception("Windows OS does not support!!!!!!!!");
            case MACOS:
                throw new Exception("Mac OS does not support!!!!!!!!");
            case OTHER:
                throw new Exception("Current OS does not support!!!!!");
            default:
                throw new Exception("Your OS does not support!!!");

        }
    }
}
