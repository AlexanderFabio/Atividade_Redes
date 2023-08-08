import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClienteFisico {
    private Socket socket;
    private BufferedReader leitor1;
    private BufferedWriter escritor1;
    private String nomeDoUsuario;

    public ClienteFisico(Socket socket, String nomeDoUsuario){
        try{
            this.socket = socket;
            this.escritor1 = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.leitor1 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.nomeDoUsuario = nomeDoUsuario;
        }catch (IOException exception){
            fecharTudo(socket, leitor1, escritor1);
        }
    }
    public void enviarMensagem(){
        try{
            escritor1.write(nomeDoUsuario);
            escritor1.newLine();
            escritor1.flush();

            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String mensagemParaEnviar = scanner.nextLine();
                escritor1.write(nomeDoUsuario + ": " + mensagemParaEnviar);
                escritor1.newLine();
                escritor1.flush();
            }
        }catch (IOException exception){
            fecharTudo(socket, leitor1, escritor1);
        }
    }
    public void lerAMensagem(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String mensagemDoChat;
                while(socket.isConnected()){
                    try{
                        mensagemDoChat = leitor1.readLine();
                        System.out.println(mensagemDoChat);
                    }catch (IOException exception){
                        fecharTudo(socket, leitor1, escritor1);
                    }
                }
            }
        }).start();
    }
    public void fecharTudo(Socket socket, BufferedReader bufferedReader,BufferedWriter bufferedWriter){
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

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entre seu nome de usuario: ");
        String nomeDeUsuario = scanner.nextLine();
        Socket socket = new Socket("localhost", 2023);
        ClienteFisico clienteFisico = new ClienteFisico(socket, nomeDeUsuario);
        clienteFisico.lerAMensagem();
        clienteFisico.enviarMensagem();
    }
}
