package top.lingyuzhao.diskMirror.core.ioStream;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 带有 socket 的数据流装饰器，其可以将一个 socket 数据流直接封装为 一个 AutoCloseableInputStream
 * <p>
 * A data stream decorator with a socket that can directly encapsulate a socket data stream into an AutoCloseableInputStream
 *
 * @author zhao
 */
public class AutoCloseableInputStream extends InputStream implements AutoCloseable {
    private final DataInputStream stream;
    private final Socket socket;

    public AutoCloseableInputStream(Socket socket) throws IOException {
        this(socket, socket.getInputStream());
    }

    public AutoCloseableInputStream(Socket socket, InputStream inputStream) {
        this.stream = new DataInputStream(inputStream);
        this.socket = socket;
    }

    // Delegate read methods to the underlying DataInputStream
    @Override
    public int read() throws IOException {
        return stream.read();
    }

    // ... implement other read methods from InputStream as needed

    @Override
    public void close() throws IOException {
        try {
            stream.close();
        } finally {
            socket.close();
        }
    }
}