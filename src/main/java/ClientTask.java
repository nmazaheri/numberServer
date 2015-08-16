import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

/**
 * Created by navid.mazaheri on 8/16/15.
 */
public class ClientTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ClientTask.class);
    private List<Socket> clientSocketList;
    private final Socket clientSocket;

    public ClientTask(List<Socket> clientSocketList, Socket clientSocket) {
        this.clientSocketList = clientSocketList;
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String clientData = "";
            while ((clientData = reader.readLine()) != null) {
                logger.info("Client Data: {}", clientData);
            }

        } catch (IOException e) {
            logger.error("Unable to read data from client socket. ", e);
        }

        logger.debug("client disconnected; port={}", clientSocket.getPort());
        clientSocketList.remove(clientSocket);
    }
}
