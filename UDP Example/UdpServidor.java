import java.io.*;  // Importa classes para operações de entrada e saída.
import java.net.*; // Importa classes para operações de rede (sockets, pacotes, etc.).

public class UdpServidor { // Declaração da classe principal do servidor UDP.
    public static void main(String[] args) { // Método principal que executa o programa.
        DatagramSocket serverSocket = null; // Declaração de um socket UDP, inicialmente nulo.

        try {
            serverSocket = new DatagramSocket(6789); // Cria um socket UDP na porta 6789.
            byte[] buffer = new byte[100]; // Cria um buffer para armazenar os dados recebidos (tamanho 100 bytes).
            System.out.println("AGUARDANDO REQUISICAO"); // Exibe mensagem no console indicando que o servidor está aguardando.

            while (true) { // Loop infinito para processar múltiplas requisições.
                DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length); // Cria um pacote para receber dados.
                serverSocket.receive(requestPacket); // Aguarda o recebimento de uma mensagem no socket.

                DatagramPacket responsePacket = new DatagramPacket( // Cria um pacote de resposta.
                    requestPacket.getData(),         // Copia os dados recebidos.
                    requestPacket.getLength(),       // Copia o tamanho dos dados recebidos.
                    requestPacket.getAddress(),      // Define o endereço do remetente.
                    requestPacket.getPort()          // Define a porta do remetente.
                );

                serverSocket.send(responsePacket); // Envia o pacote de resposta de volta para o remetente.
                System.out.println("REQUISICAO RECEBIDA: " + new String(responsePacket.getData())); // Exibe a mensagem recebida no console.
            }
        } catch (SocketException e) { // Trata exceções relacionadas ao socket.
            System.out.println("Socket: " + e.getMessage()); // Exibe a mensagem de erro do socket.
        } catch (IOException e) { // Trata exceções de entrada/saída.
            System.out.println("IO: " + e.getMessage()); // Exibe a mensagem de erro de IO.
        } finally {
            if (serverSocket != null) { // Verifica se o socket foi criado.
                serverSocket.close(); // Fecha o socket para liberar os recursos.
            }
        }
    }
}