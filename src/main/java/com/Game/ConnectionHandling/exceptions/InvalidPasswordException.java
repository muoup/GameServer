package com.Game.ConnectionHandling.exceptions;


/**
 * The {@code Exception} {@code InvalidPasswordException} is thrown
 * when a password input does not match an existing account.
 *
 * <p>The class {@code InvalidPasswordExcepton}, like other {@code RuntimeException}s, are
 * <em>unchecked exceptions.</em> They need not be declared in a method signature when thrown, and
 * usually are not intended to crash an application, but simply to throw an issue up the class
 * structure.</p>
 *
 * @author Connor McDermid
 * @see java.lang.RuntimeException
 * @since 1.0
 */
public class InvalidPasswordException extends RuntimeException {

    /**
     * Constructs a new InvalidPasswordException with {@code null} as
     * its detail message. The cause is not initialised, and may subsequently
     * be initialised with a call to {@link #initCause}.
     * @since 1.4
     */
    public InvalidPasswordException() {
        super();
    }

    /**
     * Constructs a new invalid password exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     * @since 1.4
     */
    public InvalidPasswordException(String message) {
        super(message);
    }

    /**
     * Constructs a new invalid password exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     * @param message the detail message, which is saved for later
     *                retrieval by the {@link #getMessage()} method.
     * @param cause the cause, (which is saved for later retrieval by the
     *              {@link #getCause()} method.
     *              A {@code null} value is permitted, and indicates the cause is unknown or nonexistent.
     * @since 1.4
     */
    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    /** Constructs a new runtime exception with the specified cause and a
     * detail message of {@code (cause==null ? null : cause.toString())}
     * (which typically contains the class and detail message of
     * {@code cause}).  This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwables.
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A {@code null} value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since  1.4
     */
    public InvalidPasswordException(Throwable cause) {
        super(cause);
    }
    /**
     * Constructs a new runtime exception with the specified detail
     * message, cause, suppression enabled or disabled, and writable
     * stack trace enabled or disabled.
     *
     * @param  message the detail message.
     * @param cause the cause.  (A {@code null} value is permitted,
     * and indicates that the cause is nonexistent or unknown.)
     * @param enableSuppression whether or not suppression is enabled
     *                          or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     *
     * @since 1.7
     */
    public InvalidPasswordException(String message, Throwable cause,
                                    boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
