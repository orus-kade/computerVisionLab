package ru.sfedu.computervisionlab.api;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.computervisionlab.utils.Utils;
import ru.sfedu.computervisionlab.Constants;
import ru.sfedu.computervisionlab.utils.ConfigurationUtil;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

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

    public Mat loadImage() throws IOException {
        Mat srcImage = Imgcodecs.imread(config.getConfigurationEntry(Constants.DIR_PATH) + config.getConfigurationEntry(Constants.SCR_IMAGE_NAME));
        int totalBytes = (int) (srcImage.total() * srcImage.elemSize());
        byte buffer[] = new byte[totalBytes];
        srcImage.get(0, 0, buffer);
        for (int i = 0; i < totalBytes; i++) {
            if (i % srcImage.channels() != 0) {
                buffer[i] = 0;
            }
        }
        srcImage.put(0, 0, buffer);
        return srcImage;    }

    public void showImage(Mat m) {
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
        frame.setLayout(new FlowLayout());
        frame.setSize(image.getWidth(null) + 50, image.getHeight(null) + 50);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
