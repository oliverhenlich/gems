package com.mangospice.gems.jmx;

import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.VMOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import java.util.List;

public class HeapDumper {
    private static final Logger logger = LoggerFactory.getLogger(HeapDumper.class);

    /**
     * This is the name of the HotSpot Diagnostic MBean
     */
    private static final String HOTSPOT_BEAN_NAME = "com.sun.management:type=HotSpotDiagnostic";

    private static volatile HotSpotDiagnosticMXBean hotspotMBean;

    /**
     * Call this method from your application whenever you
     * want to dump the heap snapshot into a file.
     *
     * @param fileName name of the heap dump file
     * @param live     flag that tells whether to dump only the live objects
     */
    public static void dumpHeap(String fileName, boolean live) {
        final String method = "[dumpHeap] ";

        initHotspotMBean();
        try {
            hotspotMBean.dumpHeap(fileName, live);
            logger.info(method + "dumped heap to filename={}", fileName);
        }
        catch (RuntimeException re) {
            throw re;
        }
        catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    public static void dumpDiagnosticOptions() {
        final String method = "[dumpDiagnosticOptions] ";

        initHotspotMBean();
        try {
            List<VMOption> list = hotspotMBean.getDiagnosticOptions();
            for (VMOption vmOption : list) {
                logger.info(method + "{}", vmOption);
            }
        }
        catch (RuntimeException re) {
            throw re;
        }
        catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }


    private static void initHotspotMBean() {
        if (hotspotMBean == null) {
            synchronized (HeapDumper.class) {
                if (hotspotMBean == null) {
                    hotspotMBean = getHotspotMBean();
                }
            }
        }
    }

    /**
     * Return the hotspot diagnostic MBean from the platform MBean server
     */
    private static HotSpotDiagnosticMXBean getHotspotMBean() {
        HotSpotDiagnosticMXBean ret;
        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            ret = (HotSpotDiagnosticMXBean) ManagementFactory.newPlatformMXBeanProxy(server,
                    HOTSPOT_BEAN_NAME, HotSpotDiagnosticMXBean.class);
        }
        catch (RuntimeException re) {
            throw re;
        }
        catch (Exception exp) {
            throw new RuntimeException(exp);
        }
        return ret;
    }

    public static void main(String[] args) {
        // default heap dump file name
        String fileName = "d:/temp/heap.bin2";
        // by default dump only the live objects
        boolean live = true;

        // simple command line options
        switch (args.length) {
            case 2:
                live = args[1].equals("true");
            case 1:
                fileName = args[0];
        }

        // dump the heap
        dumpDiagnosticOptions();
        dumpHeap(fileName, live);
    }
}