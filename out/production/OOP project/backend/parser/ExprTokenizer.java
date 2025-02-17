package backend.parser;

import SyntaxErrorException.LexicalError;
import SyntaxErrorException.SyntaxError;

import java.util.List;
import java.util.NoSuchElementException;
import static java.lang.Character.isWhitespace;
import static java.lang.Character.isDigit;

public class ExprTokenizer implements Tokenizer {
    private String src, next;  private int pos;

    public ExprTokenizer(String src){
        if(src == null){
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
            throw new NoSuchElementException("no more tokens");
        }
    }

    @Override
    public String peek() {
        checkNextToken();
        return next;
    }

    @Override
    public String consume() {
        checkNextToken();
        String result = next;
        computeNext();
        return result;
    }

    private void computeNext() {
        while (pos < src.length() && isWhitespace(src.charAt(pos))) {
            pos++;
        }

        StringBuilder result = new StringBuilder();
        if (pos >= src.length()) {
            next = null;
            return;
        }

        char c = src.charAt(pos);

        if (Character.isDigit(c)) {
            int start = pos;
            while (pos < src.length() && isDigit(src.charAt(pos))) {
                pos++;
            }
            next = src.substring(start, pos);
        } else if (Character.isLetter(c)) {
            int start = pos;
            while (pos < src.length() && Character.isLetterOrDigit(src.charAt(pos))) {
                pos++;
            }
            next = src.substring(start, pos);
        }else if (c == '+' || c == '{' || c== '}'|| c == '(' || c == ')' || c == '-' || c == '*' || c == '/' || c == '%' || c == '='
        || c == '^') {
            next = Character.toString(c);
            pos++;
        } else {
            throw new LexicalError("Unknown character: " + c);
        }
    }

    @Override
    public boolean peek(String s) {
        if (!hasNextToken()) return false;
        return peek().equals(s);
    }

    @Override
    public void consume(String s){
        if (peek(s)){
            consume();
        }else{
            throw new SyntaxError(s + " expected");
        }
    }

    @Override
    public void setTokens(List<String> tokens) {

    }
}