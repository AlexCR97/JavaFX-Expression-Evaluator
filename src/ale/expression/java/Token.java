package ale.expression.java;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Token {

    ARITHMETIC_OPERATOR_ADD("\\+"),
    ARITHMETIC_OPERATOR_SUB("\\-"),
    ARITHMETIC_OPERATOR_MUL("\\*"),
    ARITHMETIC_OPERATOR_DIV("\\/"),
    ARITHMETIC_OPERATOR_MOD("\\%"),
    ARITHMETIC_OPERATOR_POW("\\^"),

    DELIMITER_PARENTHESIS_OPEN("\\("),
    DELIMITER_PARENTHESIS_CLOSE("\\)"),

    NUMBER("\\d+(\\.\\d+)?"),

    IDENTIFIER("([a-zA-Z]|_*[a-zA-Z]){1}[a-zA-Z0-9_]*");

    public final Pattern pattern;

    Token(String regex) {
        pattern = Pattern.compile("^" + regex);
    }

    public int endOfMatch(String s) {
        Matcher m = pattern.matcher(s);

        if (m.find())
            return m.end();

        return -1;
    }

    @Override
    public String toString() {
        return pattern.toString();
    }

}
