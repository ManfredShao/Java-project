package Online;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.concurrent.Executors;

public class SocketServer extends SocketBase {

    private final int port;

    public SocketServer(int port) {
        this.port = port;
    }

    public void start() {
        var exec = Executors.newSingleThreadExecutor();
        exec.submit(() -> {
            try (ServerSocket server = new ServerSocket(port)) {
                System.out.println("服务器启动，等待连接...");
                handleConnection(server.accept());
            } catch (IOException e) {
                handleError(e);
            }
            exec.shutdown();
        });
    }

    public static String getIp() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            if (ni.isLoopback() || ni.isVirtual() || !ni.isUp()) continue;

            Enumeration<InetAddress> addresses = ni.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (addr instanceof Inet4Address) {
                    return addr.getHostAddress();
                }
            }
        }
        throw new RuntimeException("No valid IPv4 address found");
    }
}
