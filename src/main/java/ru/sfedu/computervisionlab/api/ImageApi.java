package ru.sfedu.computervisionlab.api;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Core;
import ru.sfedu.computervisionlab.utils.Utils;
import ru.sfedu.computervisionlab.Constants;
import ru.sfedu.computervisionlab.utils.ConfigurationUtil;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.util.Random;
import java.util.stream.Collectors;
import nu.pattern.OpenCV;
import org.opencv.core.CvType;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.photo.Photo;

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
    public ImageApi() throws Exception {
        logger.info("Checking OS.....");
        // init the API with curent os..
        try {
            switch (Utils.getOperatingSystemType()) {
                case LINUX:
                    System.load(config.getConfigurationEntry(Constants.PATH_TO_NATIVE_LIB_LINUX));;
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
        } catch (java.lang.UnsatisfiedLinkError e){
            logger.debug(e.getMessage());
            logger.debug("Trying to load locally...");
            OpenCV.loadLocally();
            }
    }

    /**
     *
     * @param imageName
     * @return
     * @throws IOException
     */
    public Mat loadImage(String imageName) throws IOException {
        logger.info("Loading image " + imageName + " ...");
        Mat srcImage = Imgcodecs.imread(config.getConfigurationEntry(Constants.IMAGE_DIR_PATH) + imageName);
        return srcImage;    
    }
    
    /**
     *
     * @param srcImage
     * @param channel
     * @return
     */
    public Mat setChannelZero(Mat srcImage, Constants.Channels channel){
        logger.info("Changing " + channel.toString() + " channel...");
        int ch = channel.ordinal();
        int totalBytes = (int) (srcImage.total() * srcImage.elemSize());
        byte buffer[] = new byte[totalBytes];
        srcImage.get(0, 0, buffer);
        for (int i = 0; i < totalBytes; i++) {
            if (i % srcImage.channels() == ch) {
                buffer[i] = 0;
            }
        }
        srcImage.put(0, 0, buffer);
        return srcImage;
    }

    /**
     *
     * @param m
     * @param title
     */
    public void showImage(Mat m, String title) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b);
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        ImageIcon icon = new ImageIcon(image);
        JFrame frame = new JFrame();
        frame.setTitle(title);
        frame.setLayout(new FlowLayout());
        frame.setSize(image.getWidth(null) + 50, image.getHeight(null) + 50);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
     *
     * @param image
     * @param imName
     * @throws IOException
     */
    public void saveImage(Mat image, String imName) throws IOException{
        logger.info("Saving image " + imName +  " ...");
        Imgcodecs.imwrite(config.getConfigurationEntry(Constants.IMAGE_RESULTS_DIR_PATH) + imName, image);
    }
    
    /**
     *
     * @param imName
     * @param i
     * @throws IOException
     */
    public void filtration(String imName, int i) throws IOException{
        Mat image = loadImage(imName);
        Mat dst = image.clone();
        showImage(dst, imName);
        //blur размытие
        Imgproc.blur(image, dst, new Size(i, i));
        String newName = "blur_" + i + "x" + i + "_" + imName;
        saveImage(dst, newName);
        showImage(dst, newName);
        //GaussianBlur 
        Imgproc.GaussianBlur(image, dst, new Size(i, i), 0);
        newName = "gaussianBlur_" + i + "x" + i + "_" + imName;
        saveImage(dst, newName);
        showImage(dst, newName);
        //medianBlur
        Imgproc.medianBlur(image, dst, i);
        newName = "medianBlur_" + i + "_" + imName;
        saveImage(dst, newName);
        showImage(dst, newName);
        //bilateralFilter
        Imgproc.bilateralFilter(image, dst, i, i*2, i*2);
        newName = "bilateralFilter_" + i + "_" + imName;
        saveImage(dst, newName);
        showImage(dst, newName);
    }
    
    /**
     *
     * @param fileName
     * @param i
     * @param morfType
     * @param mType
     */
    public void morfologyTest(String fileName, int i, String morfType, int mType) {
        try {            
            Mat src = loadImage(fileName);
            Mat dst = src.clone();    
            
            String name = morfType + "_" + i + "_";
            Mat element = Imgproc.getStructuringElement(mType, new Size(i, i));
            showImage(src, fileName);
            
            Imgproc.erode(src, dst, element);
            saveImage(dst, name + "erode_" + fileName);
            showImage(dst, name + "erode_" + fileName);
            
            dst = src.clone();
            Imgproc.dilate(src, dst, element);
            saveImage(dst, name + "dilate_" + fileName);
            showImage(dst, name + "dilate_" + fileName);
            
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    /**
     *
     * @param srcImage
     * @param seedPoint
     * @param color
     * @param loDiff
     * @param upDiff
     * @return
     */
    public Mat fillFlood(Mat srcImage, Point seedPoint, Scalar color, Scalar loDiff, Scalar upDiff) {
        Rect rect = new Rect(100, 100, 500, 500);
        int r = Utils.randomInt(0, 255);
        int g = Utils.randomInt(0, 255);
        int b = Utils.randomInt(0, 255);
        int intVal = Utils.randomInt(0, 255);
        
        logger.debug(r + " " + g + " " + b + " " + intVal);
        
        if (color == null){
            color = new Scalar(r, g, b);
        }
        if (loDiff == null){
            loDiff = new Scalar(intVal, intVal, intVal);
        }
        if (upDiff == null){
            upDiff = new Scalar(intVal, intVal, intVal);
        }
        
        if (seedPoint == null){
            seedPoint = new Point(srcImage.width()/2, srcImage.height()/2);
        }
        
        Mat mask = new Mat();
        showImage(srcImage, "before");
        Imgproc.floodFill(srcImage, mask, seedPoint, color, rect, loDiff, upDiff, Imgproc.FLOODFILL_FIXED_RANGE);
        showImage(srcImage, "after");
        return srcImage;
    }
    
    /**
     *
     * @param srcImage
     */
    public Mat pyramids(Mat srcImage, int i){
        Size imSize = new Size(srcImage.cols(), srcImage.rows());
//        Size downSize = new Size(srcImage.cols()/i, srcImage.rows()/i);
//        logger.debug(srcImage.cols()/i + " " + srcImage.rows()/i);
        Mat mask = srcImage.clone();
        showImage(srcImage, "source");
        for (int k = 0; k<i; k++){
            Imgproc.pyrDown(mask, mask);
            showImage(mask, "pyrDown_" + k);
        }
        for (int k = 0; k<i; k++){
            Imgproc.pyrUp(mask, mask);
            showImage(mask, "pyrUp_" + k);
        }
        Core.subtract(srcImage, mask, mask);
        showImage(mask, "subtrack1");
        return mask;
    }
    
    public void oneMoreMethod(Mat srcImage, String imgName) throws IOException{
        
        showImage(srcImage, "original");
// 1 - сделать в оттенках серого
        Mat grayImage = new Mat();
        Imgproc.cvtColor(srcImage, grayImage, Imgproc.COLOR_BGR2GRAY);
//        showImage(grayImage, "1_grayImage_");
//        saveImage(grayImage, "grayImage_" + imgName);

// 2 - убрать шум
        Mat denoisingImage = new Mat();
        Photo.fastNlMeansDenoising(grayImage, denoisingImage);
//        showImage(denoisingImage, "2_noNoise_");
//        saveImage(denoisingImage, "noNoise_"  + imgName);

// 3 - повысить контастность
        Mat histogramEqualizationImage = new Mat();
        Imgproc.equalizeHist(denoisingImage, histogramEqualizationImage);
//        showImage(histogramEqualizationImage, "3__histogramEq_");
//        saveImage(histogramEqualizationImage, "histogramEq_"  + imgName);

// 4 - размытие темных областей
        Mat morphologicalOpeningImage = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.morphologyEx(histogramEqualizationImage, morphologicalOpeningImage, Imgproc.MORPH_RECT, kernel);
//        showImage(morphologicalOpeningImage, "4_morphologicalOpening_");
//        saveImage(morphologicalOpeningImage, "morphologicalOpening_"  + imgName);

// 5 - получение контуров объесков 
        Mat subtractImage = new Mat();
        Core.subtract(histogramEqualizationImage, morphologicalOpeningImage, subtractImage);
//        showImage(subtractImage, "5_subtract_");
//        saveImage(subtractImage, "subtract_"  + imgName);

// 6 - делаем черно-белое
        Mat thresholdImage = new Mat();
        double threshold = Imgproc.threshold(subtractImage, thresholdImage, 50, 255, Imgproc.THRESH_OTSU);
//        showImage(thresholdImage, "6_threshold_");
//        saveImage(thresholdImage, "threshold_"  + imgName);
        thresholdImage.convertTo(thresholdImage, CvType.CV_16SC1); //16-bits signed 1 channel  ?
// 7 - получили границы 
        Mat edgeImage = new Mat();
        thresholdImage.convertTo(thresholdImage, CvType.CV_8U); // 8-bits unsigned  ?
//        showImage(thresholdImage, "7_1_");
        Imgproc.Canny(thresholdImage, edgeImage, threshold, threshold * 3, 3, true);
//        showImage(edgeImage, "7_edge_");
//        saveImage(edgeImage, "edge_"  + imgName);

// 8 - расфигачили светыле области, то есть контуры
        Mat dilatedImage = new Mat();
        Imgproc.dilate(thresholdImage, dilatedImage, kernel);
//        showImage(dilatedImage, "8_dilation_");
//        saveImage(dilatedImage, "dilation_"  + imgName);

// 9
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(dilatedImage, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);        
        contours.sort(Collections.reverseOrder(Comparator.comparing(Imgproc::contourArea)));
        
        Mat mm = Mat.zeros(dilatedImage.size(), CvType.CV_8UC3);
        for (int i = 0; i < contours.size(); i++) {
            Scalar color = new Scalar(Utils.randomInt(0, 255), Utils.randomInt(0, 255), Utils.randomInt(0, 255));
            Imgproc.drawContours(mm, contours, i, color);
        }
        showImage(mm, "contours");
        saveImage(mm, "9_contours_"  + imgName); 
        
        int k = 0;
        logger.debug(contours.size() + "  count of contours");
//        for (MatOfPoint contour : contours.subList(0, 10)) {
        
        for (MatOfPoint contour : contours.stream().filter(
                prdct -> prdct.height() >= dilatedImage.height()*0.1 || prdct.width()>= dilatedImage.width()*0.1).collect(Collectors.toList())){            
            logger.debug(Imgproc.contourArea(contour));
            Mat mcontour = Mat.zeros(dilatedImage.size(), CvType.CV_8UC3);
            Scalar color = new Scalar(Utils.randomInt(0, 255), Utils.randomInt(0, 255), Utils.randomInt(0, 255));
            ArrayList<MatOfPoint> aa = new ArrayList<>();
            aa.add(contour);            
            Imgproc.drawContours(mcontour, aa, 0, color);  
            showImage(mcontour, "contour");
            MatOfPoint2f point2f = new MatOfPoint2f();
            MatOfPoint2f approxContour2f = new MatOfPoint2f();
            MatOfPoint approxContour = new MatOfPoint();
            contour.convertTo(point2f, CvType.CV_32FC2);
            double arcLength = Imgproc.arcLength(point2f, true); // длина кривой - true - кривая замкнутая
            Imgproc.approxPolyDP(point2f, approxContour2f, 0.03 * arcLength, true);
//            Imgproc.approxPolyDP(point2f, approxContour2f, 0.1 * arcLength, true);
            approxContour2f.convertTo(approxContour, CvType.CV_32S);
            Rect rect = Imgproc.boundingRect(approxContour); // получили прямоугольный контур
            double ratio = (double) rect.height / rect.width;    //отношение сторон     
// условие для выбора прямоугольников - у которых определенное соотношение сторон            
//            if (Math.abs(0.3 - ratio) > 0.15) {
//                continue;
//            }

            k++;
            Mat submat = srcImage.submat(rect);
            Imgproc.resize(submat, submat, new Size(400, 400 * ratio));
            showImage(submat, "9_result_" + k);
            saveImage(submat, "result_" + k + " " + imgName);
        }
        logger.debug("count : " + k);
    }
    
}
