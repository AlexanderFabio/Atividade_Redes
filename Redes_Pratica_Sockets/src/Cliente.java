import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Cliente implements Runnable {

    public static ArrayList<Cliente> Clientes = new ArrayList<>();
    private Socket socket;
    private BufferedReader leitor;
    private BufferedWriter escritor;
    private String nomeDoCliente;
    public Cliente(Socket socket){
        try{
            this.socket = socket;
            this.escritor = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.leitor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.nomeDoCliente = leitor.readLine();
            Clientes.add(this);
            broadcastMessage("Server: " + nomeDoCliente + " entrou no chat!");
        }catch (IOException exception){
            fecharTudo (socket, leitor, escritor);
        }
    }

    @Override
    public void run() {
String mensagemDoCliente;
while(socket.isConnected()){
    try{
        mensagemDoCliente = leitor.readLine();
        broadcastMessage(mensagemDoCliente);
    }catch (IOException exception){
        fecharTudo(socket, leitor, escritor);
        break;
    }
}

    }
    public  void broadcastMessage(String mensagemAEnviar){
        for(Cliente cliente : Clientes){
            try {
                if(!cliente.nomeDoCliente.equals(nomeDoCliente)){
                    cliente.escritor.write(mensagemAEnviar);
                    cliente.escritor.newLine();
                    cliente.escritor.flush();
                }
            }catch (IOException exception){
                fecharTudo(socket, leitor, escritor);
            }
        }
    }
    public void removerCliente(){
        Clientes.remove(this);
        broadcastMessage("Server: " + nomeDoCliente + "saiu do chat");
    }
    public void fecharTudo(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removerCliente();
        try{
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }
}
