/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sfedu.computervisionlab.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.sfedu.computervisionlab.Constants;

/**
 *
 * @author orus-kade
 */
public class ConfigurationUtilTest {
    
    private ConfigurationUtil config;
    private static final Logger logger = LogManager.getLogger();
    
    /**
     *
     */
    public ConfigurationUtilTest() {
        logger.info(ConfigurationUtilTest.class);
    }
    
    /**
     * Test of getConfigurationEntry method, of class ConfigurationUtil.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetConfigurationEntry() throws Exception {
        logger.info("Get config " + Constants.PATH_TO_NATIVE_LIB_LINUX);  
        this.config = new ConfigurationUtil(Constants.RESORCES_PATH);
        logger.info(config.getConfigurationEntry(Constants.PATH_TO_NATIVE_LIB_LINUX));
    }
    
}
