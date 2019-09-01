package ale.expression.java;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lexer {

    private static final Set<Character> BLANK_CHARACTERS = new HashSet<>();
    static {
        BLANK_CHARACTERS.add('\r');
        BLANK_CHARACTERS.add('\n');
        BLANK_CHARACTERS.add((char) 8);
        BLANK_CHARACTERS.add((char) 9);
        BLANK_CHARACTERS.add((char) 11);
        BLANK_CHARACTERS.add((char) 12);
        BLANK_CHARACTERS.add((char) 32);
    }

    private StringBuilder input = new StringBuilder();
    private String currentToken;
    private boolean exhausted = false;
    private String errorMessage = "";

    private List<String> tokens = new ArrayList<>();

    public Lexer(String input) {
        this.input.append(input);
    }

    public List<String> lex() {
        while (hasNext()) {}

        if (!isSuccessful())
            return null;

        return tokens;
    }

    public boolean hasNext() {
        if (exhausted)
            return false;

        if (input.length() == 0) {
            exhausted = true;
            return false;
        }

        ignoreBlankCharacters();

        if (findNextToken())
            return true;

        exhausted = true;

        if (input.length() > 0)
            errorMessage = "Unexpected symbol: '" + input.charAt(0) + "'";

        return false;
    }

    private void ignoreBlankCharacters() {
        int charsToDelete = 0;

        while (BLANK_CHARACTERS.contains(input.charAt(charsToDelete)))
            charsToDelete++;

        if (charsToDelete > 0)
            input.delete(0, charsToDelete);
    }

    private boolean findNextToken() {
        for (Token t : Token.values()) {
            int end = t.endOfMatch(input.toString());

            if (end != -1) {
                currentToken = input.substring(0, end);

                String currentToken = currentToken();

                tokens.add(currentToken);

                input.delete(0, end);

                return true;
            }
        }

        return false;
    }

    public String currentToken() {
        return currentToken;
    }

    public boolean isSuccessful() {
        return errorMessage.isEmpty();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
