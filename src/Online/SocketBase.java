package Online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

public abstract class SocketBase {
    protected Consumer<Socket> connectCallback = socket -> {
    };
    protected Consumer<String> receiveCallback = line -> {
    };
    protected Consumer<IOException> errorCallback = e -> {
    };
    Socket conn;

    public void addConnectListener(Consumer<Socket> listener) {
        connectCallback = listener;
    }

    public void addMessageListener(Consumer<String> listener) {
        receiveCallback = listener;
    }

    public void addErrorListener(Consumer<IOException> listener) {
        errorCallback = listener;
    }

    protected void handleConnection(Socket socket) throws IOException {
        try (Socket autoCloseSocket = socket) {  // 确保socket最终关闭
            conn = autoCloseSocket;
            connectCallback.accept(conn);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg;
            while ((msg = in.readLine()) != null) {
                receiveCallback.accept(msg);
            }
        } finally {
            handleError(new IOException("Connection closed"));
        }
    }

    protected void handleError(IOException e) {
        conn = null;
        errorCallback.accept(e);
    }

    public boolean sendLine(String line) {
        if (conn == null) {
            errorCallback.accept(new IOException("Not connected"));
            return false;
        }
        try {
            PrintWriter out = new PrintWriter(conn.getOutputStream(), true);
            out.println(line);
            return true;
        } catch (IOException e) {
            handleError(e);
        }
        return false;
    }
}
