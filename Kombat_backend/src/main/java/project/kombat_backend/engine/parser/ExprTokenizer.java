package project.kombat_backend.engine.parser;

import project.kombat_backend.engine.exception.SyntaxError;

import java.io.IOException;
import java.util.NoSuchElementException;
import static java.lang.Character.isWhitespace;
import static java.lang.Character.isDigit;

public class ExprTokenizer implements Tokenizer {
    private String src, next;  private int pos;

    public ExprTokenizer(String src) throws IOException {
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
    public String consume() throws IOException {
        checkNextToken();
        String result = next;
        computeNext();
        return result;
    }

    private void computeNext() throws IOException {
        while (pos < src.length() && isWhitespace(src.charAt(pos))) {
            pos++;
        }

        if (pos >= src.length()) {
            next = null;
            return;
        }

        StringBuilder result = new StringBuilder();
        char c = src.charAt(pos);

        if (Character.isDigit(c)) { // อ่านตัวเลขทั้งหมด
            while (pos < src.length() && isDigit(src.charAt(pos))) {
                result.append(src.charAt(pos));
                pos++;
            }
        } else if (Character.isLetter(c)) { // อ่านตัวแปรหรือคำสั่ง
            while (pos < src.length() && Character.isLetterOrDigit(src.charAt(pos))) {
                result.append(src.charAt(pos));
                pos++;
            }
        } else if ("{}()+-*/^%=;".indexOf(c) != -1) { // เครื่องหมายพิเศษ
            result.append(c);
            pos++;
        } else {
            throw new IOException("Unknown character: " + c);
        }

        next = result.toString();
    }

    @Override
    public boolean peek(String s) {
        if (!hasNextToken()) return false;
        return peek().equals(s);
    }

    @Override
    public void consume(String s) throws IOException {
        if (peek(s)){
            consume();
        }else{
            throw new SyntaxError(s + " expected" + pos + peek());
        }
    }
}