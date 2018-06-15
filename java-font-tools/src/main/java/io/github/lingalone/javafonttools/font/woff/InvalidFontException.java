package io.github.lingalone.javafonttools.font.woff;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/6/14
 */

@SuppressWarnings("serial")
public class InvalidFontException extends RuntimeException {

    public InvalidFontException() {
        super();
    }

    public InvalidFontException(String message) {
        super(message);
    }

}