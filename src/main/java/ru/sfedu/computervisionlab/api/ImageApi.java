package ru.sfedu.computervisionlab.api;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
                    System.load(config.getConfigurationEntry(Constants.PATH_TO_NATIVE_LIB_LINUX));
                    ;
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
        } catch (java.lang.UnsatisfiedLinkError e) {
            logger.debug(e.getMessage());
            logger.debug("Trying to load locally");
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
    public Mat setChannelZero(Mat srcImage, Constants.Channels channel) {
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
    public void saveImage(Mat image, String imName) throws IOException {
        logger.info("Saving image " + imName + " ...");
        Imgcodecs.imwrite(config.getConfigurationEntry(Constants.IMAGE_RESULTS_DIR_PATH) + imName, image);
    }

    public Mat filtrationBlur(Mat image, int i) throws IOException {;
        Mat dst = image.clone();
        Imgproc.blur(image, dst, new Size(i, i));
        return dst;
    }

    public Mat filtrationMedianBlur(Mat image, int i) throws IOException {
        Mat dst = image.clone();
        Imgproc.medianBlur(image, dst, i);
        return dst;
    }

    public Mat filtrationGaussianBlur(Mat image, int i) throws IOException {
        Mat dst = image.clone();
        Imgproc.GaussianBlur(image, dst, new Size(i, i), 0);
        return dst;
    }

    public Mat filtrationBilateralFilter(Mat image, int i) throws IOException {
        Mat dst = image.clone();
        Imgproc.bilateralFilter(image, dst, i, i * 2, i * 2);
        return dst;
    }

    public Mat dilate(Mat src, int i, int mType) {
        Mat dst = src.clone();
        Mat element = Imgproc.getStructuringElement(mType, new Size(i, i));
        Imgproc.dilate(src, dst, element);
        return dst;
    }

    public Mat erode(Mat src, int i, int mType) {
        Mat dst = src.clone();
        Mat element = Imgproc.getStructuringElement(mType, new Size(i, i));
        Imgproc.erode(src, dst, element);
        return dst;
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

        if (color == null) {
            color = new Scalar(r, g, b);
            loDiff = new Scalar(intVal, intVal, intVal);
            upDiff = new Scalar(intVal, intVal, intVal);
        } else {
            if (loDiff == null) {
                loDiff = new Scalar(intVal, intVal, intVal);
            }
            if (upDiff == null) {
                upDiff = new Scalar(intVal, intVal, intVal);
            }
        }

        if (seedPoint == null) {
            seedPoint = new Point(srcImage.width() / 4, srcImage.height() / 4);
        }

        Mat mask = new Mat();
        Imgproc.floodFill(srcImage, mask, seedPoint, color, rect, loDiff, upDiff, Imgproc.FLOODFILL_FIXED_RANGE);
        return srcImage;
    }

    /**
     *
     * @param srcImage
     */
    public Mat pyramids(Mat srcImage) throws Exception {
        Size imSize = new Size(srcImage.cols(), srcImage.rows());
//        Size downSize = new Size(srcImage.cols()/i, srcImage.rows()/i);
        logger.debug(srcImage.cols() / 2 + " " + srcImage.rows() / 2);
        if (srcImage.cols() % 2 != 0 || srcImage.rows() % 2 != 0) {
            throw new Exception("Size is not suatible");
        }
        Mat mask = srcImage.clone();
        Imgproc.pyrDown(mask, mask);
        Imgproc.pyrUp(mask, mask);
        Core.subtract(srcImage, mask, mask);
//        showImage(mask, "subtrack1");
        return mask;
    }

    public Mat pyramidUp(Mat srcImage, int i) {
        Mat mask = srcImage.clone();
//        Size size = new Size (mask.cols() *  Math.pow(2, 2), mask.rows() * Math.pow(2, 2));
        for (int k = 0; k < i; k++) {
            Imgproc.pyrUp(mask, mask);
        }
        return mask;
    }

    public Mat pyramidDown(Mat srcImage, int i) {
        Mat mask = srcImage.clone();
        for (int k = 0; k < i; k++) {
            Imgproc.pyrDown(mask, mask);
        }
        return mask;
    }

    public int findRect(Mat srcImage, Size min, Size max) throws IOException {

//        showImage(srcImage, "original");
// 1 - сделать в оттенках серого
        Mat grayImage = new Mat();
        Imgproc.cvtColor(srcImage, grayImage, Imgproc.COLOR_BGR2GRAY);

// 2 - убрать шум
        Mat denoisingImage = new Mat();
        Photo.fastNlMeansDenoising(grayImage, denoisingImage);

// 3 - повысить контастность
        Mat histogramEqualizationImage = new Mat();
        Imgproc.equalizeHist(denoisingImage, histogramEqualizationImage);

// 4 - размытие темных областей
        Mat morphologicalOpeningImage = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.morphologyEx(histogramEqualizationImage, morphologicalOpeningImage, Imgproc.MORPH_RECT, kernel);;

// 5 - получение контуров объесков 
        Mat subtractImage = new Mat();
        Core.subtract(histogramEqualizationImage, morphologicalOpeningImage, subtractImage);

// 6 - делаем черно-белое
        Mat thresholdImage = new Mat();
        double threshold = Imgproc.threshold(subtractImage, thresholdImage, 50, 255, Imgproc.THRESH_OTSU);

        thresholdImage.convertTo(thresholdImage, CvType.CV_16SC1); //16-bits signed 1 channel  ?
// 7 - получили границы 
        Mat edgeImage = new Mat();
        thresholdImage.convertTo(thresholdImage, CvType.CV_8U); // 8-bits unsigned  ?

        Imgproc.Canny(thresholdImage, edgeImage, threshold, threshold * 3, 3, true);

// 8 - расфигачили светыле области, то есть контуры
        Mat dilatedImage = new Mat();
        Imgproc.dilate(thresholdImage, dilatedImage, kernel);

// 9
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(dilatedImage, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        contours.sort(Collections.reverseOrder(Comparator.comparing(Imgproc::contourArea)));

//          вывод всех контуров        
//        Mat mm = Mat.zeros(dilatedImage.size(), CvType.CV_8UC3);
//        for (int i = 0; i < contours.size(); i++) {
//            Scalar color = new Scalar(Utils.randomInt(0, 255), Utils.randomInt(0, 255), Utils.randomInt(0, 255));
//            Imgproc.drawContours(mm, contours, i, color);
//        }
//        showImage(mm, "contours");

        logger.debug(contours.size() + "  count of contours");

        List<Mat> mats = new ArrayList<>();

        for (MatOfPoint contour : contours) {
            MatOfPoint2f point2f = new MatOfPoint2f();
            MatOfPoint2f approxContour2f = new MatOfPoint2f();
            MatOfPoint approxContour = new MatOfPoint();
            contour.convertTo(point2f, CvType.CV_32FC2);

            double arcLength = Imgproc.arcLength(point2f, true); // длина кривой - true - кривая замкнутая
            Imgproc.approxPolyDP(point2f, approxContour2f, 0.03 * arcLength, true);
            approxContour2f.convertTo(approxContour, CvType.CV_32S);

            if (approxContour.total() == 4) {
                List<Point> points = approxContour.toList();
                double d1 = Math.sqrt(Math.pow((points.get(0).x - points.get(1).x), 2) + Math.pow((points.get(0).y - points.get(1).y), 2));
                double d2 = Math.sqrt(Math.pow((points.get(2).x - points.get(3).x), 2) + Math.pow((points.get(2).y - points.get(3).y), 2));
                Rect rect = Imgproc.boundingRect(approxContour); // получили прямоугольный контур
                logger.debug("rect size: w_h " + rect.width + "_" + rect.height + "   size " + rect.size());

                if (rect.width >= min.width && rect.width <= max.width
                        && rect.height >= min.height && rect.height <= max.height
                        && (Math.abs(d1 - d2) < 5)) {                   
                    logger.debug("d1_d2" + d1 + "_" + d2);
// вывод контура
//                Mat mcontour = Mat.zeros(dilatedImage.size(), CvType.CV_8UC3);
//                Scalar color = new Scalar(Utils.randomInt(0, 255), Utils.randomInt(0, 255), Utils.randomInt(0, 255));
//                ArrayList<MatOfPoint> aa = new ArrayList<>();
//                aa.add(approxContour);
//                Imgproc.drawContours(mcontour, aa, 0, color);
//                showImage(mcontour, "approxContour" + mats.size()); 

                    Mat submat = srcImage.submat(rect);
//                showImage(submat, "9_result_" + mats.size());
                    mats.add(submat);
                }
            }
        }
        logger.debug("count : " + mats.size());
        return mats.size();
    }

}
