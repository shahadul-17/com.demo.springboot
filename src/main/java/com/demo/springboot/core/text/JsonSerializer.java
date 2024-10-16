package com.demo.springboot.core.text;

import com.demo.springboot.core.utilities.DateTimeFormatter;
import com.demo.springboot.core.utilities.StringUtilities;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.util.List;
import java.util.Map;

public final class JsonSerializer {

    private static final Logger logger = LogManager.getLogger(JsonSerializer.class);
    private static final DateFormat dateFormat = DateTimeFormatter.createDateFormat("dd-MMM-yyyy hh:mm:ss:SSS a z");
    // NOTE: INSTANCES OF THE OBJECTMAPPER CLASS ARE THREAD SAFE...
    private static final ObjectMapper primaryObjectMapper = new ObjectMapper()
            // ignores unknown properties during deserialization...
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            // ignores empty objects during serialization...
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            // Object mapper shall format dates in this format...
            .setDateFormat(dateFormat)
            // pretty-prints the JSON...
            .enable(SerializationFeature.INDENT_OUTPUT);
    // the secondary object mapper does not pretty-print the JSON...
    private static final ObjectMapper secondaryObjectMapper = new ObjectMapper()
            // ignores unknown properties during deserialization...
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            // ignores empty objects during serialization...
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            // ignores null values during serialization...
            .setSerializationInclusion(Include.NON_NULL)
            // Object mapper shall format dates in this format...
            .setDateFormat(dateFormat);

    private JsonSerializer() { }

    public static String serialize(Object object) {
        return serialize(object, true);
    }

    public static String serialize(Object object, boolean prettyPrint) {
        // if pretty-print is enabled...
        final var objectMapper = prettyPrint
                ? primaryObjectMapper       // <-- we shall select the primary object mapper...
                : secondaryObjectMapper;    // <-- otherwise, we shall select the secondary object mapper...

        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception exception) {
            logger.log(Level.ERROR, "An exception occurred while serializing object as JSON.", exception);
        }

        return StringUtilities.getEmptyString();
    }

    private static <Type> Type _deserialize(String json, Class<Type> classOfType) throws Exception {
        return primaryObjectMapper.readValue(json, classOfType);
    }

    private static <Type> Type _deserialize(String json, TypeReference<Type> typeReference) throws Exception {
        return primaryObjectMapper.readValue(json, typeReference);
    }

    public static <Type> Type deserialize(String json, Class<Type> classOfType) {
        try {
            return _deserialize(json, classOfType);
        } catch (Exception exception) {
            logger.log(Level.ERROR, "An exception occurred while deserializing JSON as object.", exception);

            return null;
        }
    }

    public static <Type> Type deserialize(
            String json,
            Class<Type> classOfType,
            boolean throwException) throws Exception {
        // if exception shall be thrown...
        if (throwException) {
            // calls the private method directly...
            return _deserialize(json, classOfType);
        }

        // otherwise, calls the overloaded deserialize method
        // that does not throw exception (returns null instead)...
        return deserialize(json, classOfType);
    }

    private static <Type> Type deserialize(String json, TypeReference<Type> typeReference) {
        try {
            return _deserialize(json, typeReference);
        } catch (Exception exception) {
            logger.log(Level.ERROR, "An exception occurred while deserializing JSON as object.", exception);

            return null;
        }
    }

    private static <Type> Type deserialize(
            String json,
            TypeReference<Type> typeReference,
            boolean throwException) throws Exception {
        // if exception shall be thrown...
        if (throwException) {
            // calls the private method directly...
            return _deserialize(json, typeReference);
        }

        // otherwise, calls the overloaded deserialize method
        // that does not throw exception (returns null instead)...
        return deserialize(json, typeReference);
    }

    public static <ValueType> List<ValueType> deserializeAsList(String json) {
        var typeReference = new TypeReference<List<ValueType>>() { };
        var list = deserialize(json, typeReference);

        return list;
    }

    public static <ValueType> List<ValueType> deserializeAsList(
            String json, boolean throwException) throws Exception {
        var typeReference = new TypeReference<List<ValueType>>() { };
        var list = deserialize(json, typeReference, throwException);

        return list;
    }

    public static <KeyType, ValueType> Map<KeyType, ValueType> deserializeAsMap(String json) {
        var typeReference = new TypeReference<Map<KeyType, ValueType>>() { };
        var map = deserialize(json, typeReference);

        return map;
    }

    public static <KeyType, ValueType> Map<KeyType, ValueType> deserializeAsMap(
            String json, boolean throwException) throws Exception {
        var typeReference = new TypeReference<Map<KeyType, ValueType>>() { };
        var map = deserialize(json, typeReference, throwException);

        return map;
    }
}
