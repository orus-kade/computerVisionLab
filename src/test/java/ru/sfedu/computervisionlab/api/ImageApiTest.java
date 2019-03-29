/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sfedu.computervisionlab.api;

import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author orus-kade
 */
public class ImageApiTest {
    
    private static final Logger logger = LogManager.getLogger();
    private ImageApi api;
    
    /**
     *
     */
    public ImageApiTest() {
    }
    
//    @BeforeClass
//    public static void setUpClass() {
//    }
//    
//    @AfterClass
//    public static void tearDownClass() {
//    }
//    
//    @Before
//    public void setUp() {
//    }
//    
//    @After
//    public void tearDown() {
//    }

    /**
     *
     */

    @Test
    public void testSomeMethod() {  
        logger.info("Trying to load library...");
        try {        
            api = new ImageApi();
            logger.debug("");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
    
}
