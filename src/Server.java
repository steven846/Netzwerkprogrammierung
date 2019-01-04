import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {
    ServerSocket server;
    ArrayList<PrintWriter> list_clientWriter;



    public static void main(String[] args) {
        Server s = new Server();
        if (s.runServer()){
            s.listenToClient();
        }else {
            // nichts passiert
        }
    }


    public class ClientHandler implements Runnable {
        Socket client;
        BufferedReader reader;

        public ClientHandler(Socket client) {
            try {
                this.client = client;
                reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            try {
                while (true) {
                    String nachricht = reader.readLine();
                    System.out.println("von Client: \n" + nachricht);
                    sendToAllClients(nachricht);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    public void sendToAllClients(String message) {
        Iterator it = list_clientWriter.iterator();
        while (it.hasNext()) {
            PrintWriter writer = (PrintWriter) it.next();
            writer.println(message);
            writer.flush();
        }
    }

    public void listenToClient() {
        while (true) {
            try {

                Socket client = server.accept();
                PrintWriter writer = new PrintWriter(client.getOutputStream());
                list_clientWriter.add(writer);
                Thread clientThread = new Thread(new ClientHandler(client));
                clientThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean runServer(){
        try {
            server = new ServerSocket(20);
            list_clientWriter = new ArrayList<PrintWriter>();
            System.out.println("Server wurde gestartet...");
            return true;
        } catch (IOException e) {
            System.out.println("Server konnte nicht gestartet werden!");
            e.printStackTrace();
            return false;
        }
    }
}









