package functions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class MeosErrorHandler implements error_handler_fn {

    private static final Logger logger = LoggerFactory.getLogger(MeosErrorHandler.class);

    public static final int NOTICE  = 18;
    public static final int WARNING = 19;
    public static final int ERROR   = 21;

    // MEOS error codes (from meos.h)
    public static final int MEOS_ERR_INTERNAL_ERROR      = 1;
    public static final int MEOS_ERR_INTERNAL_TYPE_ERROR = 2;
    public static final int MEOS_ERR_VALUE_OUT_OF_RANGE  = 3;
    public static final int MEOS_ERR_DIVISION_BY_ZERO    = 4;
    public static final int MEOS_ERR_MEMORY_ALLOC_ERROR  = 5;
    public static final int MEOS_ERR_AGGREGATION_ERROR   = 6;
    public static final int MEOS_ERR_DIRECTORY_ERROR     = 7;
    public static final int MEOS_ERR_FILE_ERROR          = 8;

    public static final int MEOS_ERR_INVALID_ARG         = 10;
    public static final int MEOS_ERR_INVALID_ARG_TYPE    = 11;
    public static final int MEOS_ERR_INVALID_ARG_VALUE   = 12;
    public static final int MEOS_ERR_FEATURE_NOT_SUPPORTED = 13;

    public static final int MEOS_ERR_MFJSON_INPUT        = 20;
    public static final int MEOS_ERR_MFJSON_OUTPUT       = 21;
    public static final int MEOS_ERR_TEXT_INPUT          = 22;
    public static final int MEOS_ERR_TEXT_OUTPUT         = 23;
    public static final int MEOS_ERR_WKB_INPUT           = 24;
    public static final int MEOS_ERR_WKB_OUTPUT          = 25;
    public static final int MEOS_ERR_GEOJSON_INPUT       = 26;
    public static final int MEOS_ERR_GEOJSON_OUTPUT      = 27;

    private static final Map<Integer, BiFunction<String, Integer, MeosException>> EXCEPTION_MAP = new HashMap<>();
    static {
        EXCEPTION_MAP.put(MEOS_ERR_INTERNAL_ERROR,      MeosInternalError::new);
        EXCEPTION_MAP.put(MEOS_ERR_INTERNAL_TYPE_ERROR, MeosInternalTypeError::new);
        EXCEPTION_MAP.put(MEOS_ERR_VALUE_OUT_OF_RANGE,  MeosValueOutOfRangeError::new);
        EXCEPTION_MAP.put(MEOS_ERR_DIVISION_BY_ZERO,    MeosDivisionByZeroError::new);
        EXCEPTION_MAP.put(MEOS_ERR_MEMORY_ALLOC_ERROR,  MeosMemoryAllocError::new);
        EXCEPTION_MAP.put(MEOS_ERR_AGGREGATION_ERROR,   MeosAggregationError::new);
        EXCEPTION_MAP.put(MEOS_ERR_DIRECTORY_ERROR,     MeosDirectoryError::new);
        EXCEPTION_MAP.put(MEOS_ERR_FILE_ERROR,          MeosFileError::new);
        EXCEPTION_MAP.put(MEOS_ERR_INVALID_ARG,         MeosInvalidArgError::new);
        EXCEPTION_MAP.put(MEOS_ERR_INVALID_ARG_TYPE,    MeosInvalidArgTypeError::new);
        EXCEPTION_MAP.put(MEOS_ERR_INVALID_ARG_VALUE,   MeosInvalidArgValueError::new);
        EXCEPTION_MAP.put(MEOS_ERR_FEATURE_NOT_SUPPORTED, MeosFeatureNotSupported::new);
        EXCEPTION_MAP.put(MEOS_ERR_MFJSON_INPUT,        MeosMfJsonInputError::new);
        EXCEPTION_MAP.put(MEOS_ERR_MFJSON_OUTPUT,       MeosMfJsonOutputError::new);
        EXCEPTION_MAP.put(MEOS_ERR_TEXT_INPUT,          MeosTextInputError::new);
        EXCEPTION_MAP.put(MEOS_ERR_TEXT_OUTPUT,         MeosTextOutputError::new);
        EXCEPTION_MAP.put(MEOS_ERR_WKB_INPUT,           MeosWkbInputError::new);
        EXCEPTION_MAP.put(MEOS_ERR_WKB_OUTPUT,          MeosWkbOutputError::new);
        EXCEPTION_MAP.put(MEOS_ERR_GEOJSON_INPUT,       MeosGeoJsonInputError::new);
        EXCEPTION_MAP.put(MEOS_ERR_GEOJSON_OUTPUT,      MeosGeoJsonOutputError::new);
    }

    private static int    lastCode    = 0;
    private static int    lastLevel   = 0;
    private static String lastMessage = null;

    @Override
    public void apply(int errorLevel, int errorCode, String errorMessage) {
        lastCode    = errorCode;
        lastLevel   = errorLevel;
        lastMessage = errorMessage;
    }

    private static void reportMeosException(int level, int code, String message) {
        MeosException exception = EXCEPTION_MAP
                .getOrDefault(code, MeosException::new)
                .apply(message, code);

        if (level == NOTICE) {
            logger.info("MEOS NOTICE: {}", exception.toString());
        } else if (level == WARNING) {
            logger.warn("MEOS WARNING: {}", exception.toString());
        } else if (level == ERROR) {
            logger.error("MEOS ERROR: {}", exception.toString());
            throw exception;
        } else {
            logger.error("Error raised with unknown level {}: {}", level, exception.toString());
            throw exception;
        }
    }

    public static void checkError() {
        if (lastCode != 0) {
            int    code    = lastCode;
            int    level   = lastLevel;
            String message = lastMessage;
            lastCode = 0;
            lastLevel = 0;
            lastMessage = null;
            reportMeosException(level, code, message);
        }
    }
}