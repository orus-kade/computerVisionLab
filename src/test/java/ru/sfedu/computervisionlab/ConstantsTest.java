package ru.sfedu.computervisionlab;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author orus-kade
 */
public class ConstantsTest {
    
    private static final Logger logger = LogManager.getLogger("HelloWorld");
    
    /**
     *
     */
    public ConstantsTest() {
    }
    
    /**
     *
     */
    @org.junit.Test
    public void testSomeMethod() {
        System.out.println("Ololo");
        logger.info("Hello, World!");
        logger.debug("Debug");
        logger.warn("Warn");
        logger.trace("Trace");
    }
    
}
