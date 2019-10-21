package ru.lagoshny.task.manager.utils;

/**
 * Utility class to work with {@link String}.
 * Extends {@link org.apache.commons.lang3.StringUtils}.
 */
public abstract class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * Some useful constants.
     */
    @SuppressWarnings({"unused", "CheckStyle"})
    public interface Const {

        String EMPTY = org.apache.commons.lang3.StringUtils.EMPTY;

        String DOT = ".";

        String HYPHEN = "-";

        String COMMA = ",";

        String SPACE = org.apache.commons.lang3.StringUtils.SPACE;

        String UNDERSCORE = "_";

        String QUESTION = "?";

        String FWD_SLASH = "/";

        String TAB = "\t";

        String NEW_LINE = "\n";

        String STAR = "*";

        String PLUS = "+";

        String MINUS = HYPHEN;

        String SEMICOLON = ";";

        String COLON = ":";

        String LEFT_SQ_BRACKET = "[";

        String RIGHT_SQ_BRACKET = "]";

        String LEFT_R_BRACKET = "(";

        String RIGHT_R_BRACKET = ")";

        String LEFT_CURL_BRACKET = "{";

        String RIGHT_CURL_BRACKET = "}";

        String DEFAULT_DELIMITER = "\\s*,\\s*";

        String COLON_DELIMITER = "\\s*:\\s*";

        String HTTP_POSTFIX = "://";
    }

}
