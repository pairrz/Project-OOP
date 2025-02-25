package backend.parser;

import SyntaxErrorException.*;
import java.util.NoSuchElementException;
import static java.lang.Character.isWhitespace;
import static java.lang.Character.isDigit;

public class ExprTokenizer implements Tokenizer {
    private final String src;
    private String next;
    private int pos;

    public ExprTokenizer(String src) {
        if (src == null) {
            throw new IllegalArgumentException("src is null");
        }
        this.src = src;
        this.pos = 0;
        computeNext();
    }

    @Override
    public boolean hasNextToken() {
        return next != null;
    }

    public void checkNextToken() {
        if (!hasNextToken()) {
            throw new NoSuchElementException("No more tokens");
        }
    }

    @Override
    public String peek() {
        //System.out.println("Tokenizer peek: " + next);  // Debugging
        checkNextToken();
        return next;
    }

    @Override
    public String consume() {
        checkNextToken();
        String result = next;
        //System.out.println("Tokenizer consume: " + result);  // Debugging
        computeNext();
        return result;
    }

    private void computeNext() {
        while (pos < src.length() && isWhitespace(src.charAt(pos))) {
            pos++;
        }

        if (pos >= src.length()) {
            next = null;
            return;
        }

        char c = src.charAt(pos);

        if (isDigit(c)) {
            int start = pos;
            while (pos < src.length() && isDigit(src.charAt(pos))) {
                pos++;
            }
            next = src.substring(start, pos);
            return;
        }

        if (Character.isLetter(c)) {
            int start = pos;
            while (pos < src.length() && (Character.isLetterOrDigit(src.charAt(pos)) || src.charAt(pos) == '_')) {
                pos++;
            }
            next = src.substring(start, pos);
            return;
        }

        if ("+-*/%^=(){}".indexOf(c) != -1) {
            next = Character.toString(c);
            pos++;
            return;
        }

        throw new LexicalError("Unknown character: " + c + " at position " + pos);
    }

    @Override
    public boolean peek(String s) {
        if (!hasNextToken()) return false;
        return next != null && next.equals(s);
    }

    @Override
    public void consume(String s) {
        if (next != null && peek(s)) {
            consume();
        } else {
            throw new SyntaxError(s + " expected at position " + pos);
        }
    }
}