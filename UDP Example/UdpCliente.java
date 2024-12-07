import java.io.*;  // Importa classes para operações de entrada e saída.
import java.net.*; // Importa classes para operações de rede (sockets, pacotes, etc.).

public class UdpCliente { // Declaração da classe principal do cliente UDP.
    public static void main(String[] args) { // Método principal que executa o programa.
        DatagramSocket clientSocket = null; // Declaração de um socket UDP, inicialmente nulo.

        try {
            clientSocket = new DatagramSocket(); // Cria um socket UDP para enviar e receber pacotes.
            byte[] messageBytes = args[0].getBytes(); // Converte a primeira entrada (mensagem) passada por linha de comando em bytes.
            InetAddress serverAddress = InetAddress.getByName(args[1]); // Converte o segundo argumento (endereço do servidor) em um objeto InetAddress.
            int serverPort = 6789; // Define a porta do servidor para comunicação.

            DatagramPacket requestPacket = new DatagramPacket( // Cria um pacote de solicitação.
                messageBytes,           // Define os dados da mensagem (em bytes).
                messageBytes.length,    // Define o tamanho da mensagem.
                serverAddress,          // Define o endereço do servidor.
                serverPort              // Define a porta do servidor.
            );

            clientSocket.send(requestPacket); // Envia o pacote de solicitação para o servidor.

            byte[] buffer = new byte[100]; // Cria um buffer para armazenar a resposta do servidor (tamanho 100 bytes).
            DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length); // Cria um pacote para armazenar a resposta recebida.

            clientSocket.receive(responsePacket); // Aguarda a resposta do servidor e armazena no pacote responsePacket.

            System.out.println("Reply\n RESPOSTA DO SERVIDOR: " + new String(responsePacket.getData())); // Exibe a resposta recebida no console.
        } catch (SocketException e) { // Trata exceções relacionadas ao socket.
            System.out.println("Socket: " + e.getMessage()); // Exibe a mensagem de erro do socket.
        } catch (IOException e) { // Trata exceções de entrada/saída.
            System.out.println("IO: " + e.getMessage()); // Exibe a mensagem de erro de IO.
        } finally {
            if (clientSocket != null) { // Verifica se o socket foi criado.
                clientSocket.close(); // Fecha o socket para liberar os recursos.
            }
        }
    }
}