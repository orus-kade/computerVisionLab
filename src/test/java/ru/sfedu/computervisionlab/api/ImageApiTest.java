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
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
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
    public void initializeTest() {
        logger.info("Trying to initialize the library...");
        try {
            api = new ImageApi();
            logger.info("Done!");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            fail(ex.getMessage());
        }

    }

    /**
     *
     * @throws IOException
     */
    @Test
    public void testSetChannelZero() throws IOException {
        String imName = "29slDhxgi-g.jpg";
//        String imName = "Stars.png";
//        String imName = "305126.jpg";
//        String imName = "15_8165_oboi_oboi_star_wars_1440x900.jpg";
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
        Mat mat = api.loadImage(imName);
        api.showImage(mat, imName);
        mat = api.setChannelZero(mat, ch);
        api.saveImage(mat, "res_" + ch.toString() + "_" + imName);
        api.showImage(mat, "res_" + ch.toString() + "_" + imName);
    }

    /**
     *
     */
    @Test
    public void testMorfology() throws IOException {
//        String imName = "Stars.png";
        String imName = "305126.jpg";
//        String imName = "15_8165_oboi_oboi_star_wars_1440x900.jpg";
//        String imName = "number.jpg";
//        String imName = "29slDhxgi-g.jpg";        
        logger.info("Trying to load library...");
        try {
            api = new ImageApi();
            logger.debug("");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        logger.info("Trying to modify picture");
        int i = 7;
//        for (int i = 3; i<=15; i += 2){
        String morfType = "cross";
        Mat mat = api.loadImage(imName);
        api.showImage(mat, imName);
        int mType = Imgproc.MORPH_CROSS;
        Mat res = api.erode(mat, i, mType);
        api.saveImage(res, "erode_" + morfType + "_" + i + "_" + imName);
        api.showImage(res, "erode_" + morfType + "_" + i + "_" + imName);
        res = api.dilate(mat, i, mType);
        api.saveImage(res, "dilate_" + morfType + "_" + i + "_" + imName);
        api.showImage(res, "dilate_" + morfType + "_" + i + "_" + imName);
        morfType = "rect";
        mType = Imgproc.MORPH_RECT;
        res = api.erode(mat, i, mType);
        api.saveImage(res, "erode_" + morfType + "_" + i + "_" + imName);
        api.showImage(res, "erode_" + morfType + "_" + i + "_" + imName);
        res = api.dilate(mat, i, mType);
        api.saveImage(res, "dilate_" + morfType + "_" + i + "_" + imName);
        api.showImage(res, "dilate_" + morfType + "_" + i + "_" + imName);
        morfType = "ellipse";
        mType = Imgproc.MORPH_ELLIPSE;
        res = api.erode(mat, i, mType);
        api.saveImage(res, "erode_" + morfType + "_" + i + "_" + imName);
        api.showImage(res, "erode_" + morfType + "_" + i + "_" + imName);
        res = api.dilate(mat, i, mType);
        api.saveImage(res, "dilate_" + morfType + "_" + i + "_" + imName);
        api.showImage(res, "dilate_" + morfType + "_" + i + "_" + imName);
//        }
// не работает gradient, blackhat

    }

    /**
     *
     * @throws IOException
     */
    @Test
    public void testFillFlood() throws IOException {
//        String imName = "29slDhxgi-g.jpg";  
//        String imName = "Stars.png";
//        String imName = "305126.jpg";
//        String imName = "15_8165_oboi_oboi_star_wars_1440x900.jpg";
//        String imName = "number.jpg";
        String imName = "34190519102015.jpg";

        logger.info("Trying to load library...");
        try {
            api = new ImageApi();
            logger.debug("");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        Mat srcImage = api.loadImage(imName);
//        Point point = null;
        Scalar color = null;
        Scalar loDiff = null;
        Scalar upDiff = null;
        Point point = new Point(150, 150);
//        Scalar color = new Scalar(100, 200, 45);
//        Scalar loDiff = new Scalar(10, 10, 10);
//        Scalar upDiff = new Scalar(200, 200, 200);
        srcImage = api.fillFlood(srcImage, point, color, loDiff, upDiff);
        api.saveImage(srcImage, "fillFlood_" + imName);
    }

    /**
     *
     * @throws IOException
     */
    @Test
    public void testPyramids() throws IOException, Exception {
//        String imName = "Stars.png";
//        String imName = "305126.jpg";
//        String imName = "15_8165_oboi_oboi_star_wars_1440x900.jpg";
        String imName = "number.jpg";
//        String imName = "34190519102015.jpg";
        logger.info("Trying to load library...");
        try {
            api = new ImageApi();
            logger.debug("");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        Mat srcImage = api.loadImage(imName);
        api.showImage(api.pyramids(srcImage), imName);
        api.saveImage(srcImage, "subtract_" + imName);
    }
    
    @Test
    public void testPyramidUp() throws IOException{
        //        String imName = "Stars.png";
//        String imName = "305126.jpg";
//        String imName = "15_8165_oboi_oboi_star_wars_1440x900.jpg";
        String imName = "number.jpg";
//        String imName = "34190519102015.jpg";
        logger.info("Trying to load library...");
        try {
            api = new ImageApi();
            logger.debug("");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        Mat srcImage = api.loadImage(imName);        
        api.showImage(api.pyramidUp(srcImage, 2), imName);
    }
    
    @Test
    public void testPyramidDown() throws IOException{
        //        String imName = "Stars.png";
//        String imName = "305126.jpg";
//        String imName = "15_8165_oboi_oboi_star_wars_1440x900.jpg";
        String imName = "number.jpg";
//        String imName = "34190519102015.jpg";
        logger.info("Trying to load library...");
        try {
            api = new ImageApi();
            logger.debug("");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        Mat srcImage = api.loadImage(imName);        
        api.showImage(api.pyramidDown(srcImage, 4), imName);
    }

    @Test
    public void testOneMoreMethod() throws IOException {
//        String imName = "15_8165_oboi_oboi_star_wars_1440x900.jpg";
//        String imName = "number.jpg";
//        String imName = "Stars.png";
//        String imName = "Untitled.png";
//        String imName = "slide_1.jpg";
//        String imName = "ololo.jpg";
//        String imName = "qqq.jpg";
//        String imName = "sss.png";
        String imName = "ddd.png";
        logger.info("Trying to load library...");
        try {
            api = new ImageApi();
            logger.debug("");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        Mat srcImage = api.loadImage(imName);
        Size min = new Size(50, 50);
        Size max = new Size(600, 600);
        api.findRect(srcImage, min, max);
    }

    /**
     *
     * @throws IOException
     */
    @Test
    public void testFiltration() throws IOException {
//        String imName = "15_8165_oboi_oboi_star_wars_1440x900.jpg";
        String imName = "29slDhxgi-g.jpg";
//        String imName = "number.jpg";
//        String imName = "Stars.png";
        logger.info("Trying to load library...");
        try {
            api = new ImageApi();
            logger.debug("");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        };
        Mat mat = api.filtrationBilateralFilter(api.loadImage(imName), 9);
        api.saveImage(mat, "bi_" + imName);
        mat = api.filtrationBlur(api.loadImage(imName), 9);
        api.saveImage(mat, "blur_" + imName);
        mat = api.filtrationGaussianBlur(api.loadImage(imName), 9);
        api.saveImage(mat, "gaus_" + imName);
        mat = api.filtrationMedianBlur(api.loadImage(imName), 9);
        api.saveImage(mat, "med_" + imName);
    }
}
