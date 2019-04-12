
package ru.sfedu.computervisionlab;

/**
 *
 * @author orus-kade
 */
public class Constants {
    
    /**
     *
     */
    public static String RESORCES_PATH = "/home/orus-kade/NetBeansProjects/ComputerVisionLab/src/main/resources";
    
    /**
     *
     */
    public static String PATH_TO_NATIVE_LIB_LINUX = "path_to_linux_lib";
            
//    public static String PATH_TO_NATIVE_LIB_WIN = 

    /**
     *
     */
    
    public enum OSType {

        /**
         *
         */
        MACOS,

        /**
         *
         */
        LINUX,

        /**
         *
         */
        WINDOWS,

        /**
         *
         */
        OTHER
    }
    
    /**
     *
     */
    public static String TEST_PROP = "my_prop";
    
    public static String IMAGE_DIR_PATH = "image_dir";
    
    public static String IMAGE_RESULTS_DIR_PATH = "image_dir_res";
    
    public static String SCR_IMAGE_NAME = "image_name";    
    
    public enum Channels{
        BLUE,
        GREEN,
        RED
    }
}
