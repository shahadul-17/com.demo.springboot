package com.demo.springboot.core.utilities;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class NumberUtilities {

    private static final Logger logger = LogManager.getLogger(NumberUtilities.class);

    /**
     * Tries to parse a string value as integer.
     * @param value String value to be parsed.
     * @return The integer value if succeeds. Otherwise,
     * returns Integer.MIN_VALUE.
     */
    public static int tryParseInteger(String value) {
        var numericValue = tryParseInteger(value, Integer.MIN_VALUE);

        return numericValue;
    }

    /**
     * Tries to parse a string value as integer.
     * @param value String value to be parsed.
     * @param defaultValue Default value to be returned if exception occurs.
     * @return The integer value if succeeds. Otherwise,
     * returns the default value.
     */
    public static int tryParseInteger(String value, int defaultValue) {
        // sanitizing the value before parsing...
        value = StringUtilities.getDefaultIfNullOrWhiteSpace(
                value, StringUtilities.getEmptyString(), true);

        // if no value is found, we shall return the default value...
        if (StringUtilities.isEmpty(value)) { return defaultValue; }

        var numericValue = defaultValue;

        try {
            // tries to parse the value as integer...
            numericValue = Integer.parseInt(value);
        } catch (Exception exception) {
            logger.log(Level.WARN, "An exception occurred while parsing \"" + value + "\" as integer.", exception);
        }

        // returns the numeric value...
        return numericValue;
    }

    /**
     * Tries to parse a string value as long.
     * @param value String value to be parsed.
     * @return The long value if succeeds. Otherwise,
     * returns Long.MIN_VALUE.
     */
    public static long tryParseLong(String value) {
        var numericValue = tryParseLong(value, Long.MIN_VALUE);

        return numericValue;
    }

    /**
     * Tries to parse a string value as long.
     * @param value String value to be parsed.
     * @param defaultValue Default value to be returned if exception occurs.
     * @return The long value if succeeds. Otherwise,
     * returns the default value.
     */
    public static long tryParseLong(String value, long defaultValue) {
        var numericValue = defaultValue;

        try {
            // tries to parse the value as long...
            numericValue = Long.parseLong(value);
        } catch (Exception exception) {
            logger.log(Level.WARN, "An exception occurred while parsing \"" + value + "\" as long.", exception);
        }

        // returns the numeric value...
        return numericValue;
    }

    /**
     * Tries to parse a string value as double.
     * @param value String value to be parsed.
     * @return The double value if succeeds. Otherwise,
     * returns Double.MIN_VALUE.
     */
    public static double tryParseDouble(String value) {
        var numericValue = tryParseDouble(value, Double.MIN_VALUE);

        return numericValue;
    }

    /**
     * Tries to parse a string value as double.
     * @param value String value to be parsed.
     * @param defaultValue Default value to be returned if exception occurs.
     * @return The double value if succeeds. Otherwise,
     * returns the default value.
     */
    public static double tryParseDouble(String value, double defaultValue) {
        var numericValue = defaultValue;

        try {
            // tries to parse the value as double...
            numericValue = Double.parseDouble(value);
        } catch (Exception exception) {
            logger.log(Level.WARN, "An exception occurred while parsing \"" + value + "\" as double.", exception);
        }

        // returns the numeric value...
        return numericValue;
    }
}
