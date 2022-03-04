import java.io.IOException;

public class ntpServer {
    public static void main(String args[]){
        int port = 1023;
        final NTPServerImpl timeServer = new NTPServerImpl(port);
        try {
            timeServer.start();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
