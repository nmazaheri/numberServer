package data;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ServerUtils;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by navid.mazaheri on 8/16/15.
 * <p/>
 * Used for storage of numbers
 */
public class WindowDataStore {
    private static final Logger logger = LoggerFactory.getLogger(WindowDataStore.class);
    private Set<Integer> concurrentSet = Collections.newSetFromMap(new ConcurrentHashMap<Integer, Boolean>());
    private AtomicInteger windowRequestCount = new AtomicInteger();

    public boolean updateWindow(String in) {
        if (ServerUtils.isTerminationString(in)) {
            logger.warn("Termination String found = \"{}\" ", in);
            return false;
        }

        if (!isValidNumber(in)) {
            logger.warn("input is invalid number = \"{}\" ", in);
            return false;
        }

        try {
            updateWindow(Integer.parseInt(in));
        } catch (NumberFormatException e) {
            logger.warn("Unable to convert \"{}\" to integer", in);
            return false;
        }
        return true;
    }

    private boolean isValidNumber(String in) {
        return StringUtils.isNotEmpty(in) && in.length() == 9 && StringUtils.isNumeric(in);
    }

    synchronized private void updateWindow(Integer dataKey) {
            concurrentSet.add(dataKey);
            windowRequestCount.incrementAndGet();
    }

    Set<Integer> getConcurrentSet() {
        return concurrentSet;
    }

    AtomicInteger getWindowRequestCount() {
        return windowRequestCount;
    }
}
