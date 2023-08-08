import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

        public void startServer () {
            try {
                while (!serverSocket.isClosed()) {
                    Socket socket1 = serverSocket.accept();
                    System.out.println("Um novo cliente entrou no chat");
                    Cliente clientes = new Cliente(socket1);

                    Thread thread = new Thread(clientes);
                    thread.start();
                }
            } catch (IOException exception){

            }
        }
public void fecharServerSocket(){
        try{
            if (serverSocket != null){
                serverSocket.close();
            }
        }catch (IOException exception){
            exception.printStackTrace();
        }
}

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2023);
        Server server = new Server(serverSocket);
        server.startServer();
    }
    }



