package top.lingyuzhao.diskMirror.exception;

/**
 * 当参数检查不通过的时候抛出此异常
 *
 * @author zhao
 */
public class CheckException extends UnsupportedOperationException {
    public CheckException() {
        super();
    }

    public CheckException(String message) {
        super(message);
    }

    public CheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckException(Throwable cause) {
        super(cause);
    }
}
