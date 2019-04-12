/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sfedu.computervisionlab.api;

import java.io.IOException;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opencv.core.Mat;
import ru.sfedu.computervisionlab.Constants;

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
    public void testSomeMethod() throws IOException { 
//        String imName = "Stars.png";
//        String imName = "305126.jpg";
        String imName = "15_8165_oboi_oboi_star_wars_1440x900.jpg";
//        Constants.Channels ch = Constants.Channels.RED;
        Constants.Channels ch = Constants.Channels.GREEN;
//        Constants.Channels ch = Constants.Channels.BLUE;
        logger.info("Trying to load library...");
        try {        
            api = new ImageApi();
            logger.debug("");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        logger.info("Trying to load picture");
        Mat  mat = api.loadImage(imName);
        api.showImage(mat);
        mat = api.changeImage(mat, ch);
        api.saveImage(mat, "res_" + ch.toString() + "_" + imName);
        api.showImage(mat);
    }    
}
