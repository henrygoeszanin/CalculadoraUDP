import java.io.*; // Importa classes de E/S
import java.net.*; // Importa classes de rede
import java.util.Scanner; // Importa Scanner para leitura

public class CalculadoraCliente {
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) { // Cria socket
            Scanner scanner = new Scanner(System.in); // Inicializa Scanner
            InetAddress address = InetAddress.getByName("localhost"); // Endereço do servidor
            int port = 6789; // Porta do servidor

            while (true) {
                System.out.println("------------------------------");

                System.out.print("Digite o primeiro número (ou 0 para sair): ");
                String num1 = scanner.nextLine(); // Lê primeiro número

                if ("0".equals(num1)) { // Verifica se deve encerrar
                    socket.send(new DatagramPacket(num1.getBytes(), num1.length(), address, port)); // Envia sinal de encerramento
                    System.out.println("Encerrando o cliente.");
                    break; // Sai do loop
                }

                socket.send(new DatagramPacket(num1.getBytes(), num1.length(), address, port)); // Envia primeiro número

                System.out.print("Digite o operador (+, -, *, /): ");
                String oper = scanner.nextLine(); // Lê operador
                socket.send(new DatagramPacket(oper.getBytes(), oper.length(), address, port)); // Envia operador

                System.out.print("Digite o segundo número: ");
                String num2 = scanner.nextLine(); // Lê segundo número
                socket.send(new DatagramPacket(num2.getBytes(), num2.length(), address, port)); // Envia segundo número

                byte[] buffer = new byte[100]; // Buffer para resposta
                DatagramPacket response = new DatagramPacket(buffer, buffer.length); // Pacote de resposta
                socket.receive(response); // Recebe resultado

                String result = new String(response.getData(), 0, response.getLength()).trim(); // Converte resultado
                System.out.println("Resultado: " + result); // Exibe resultado
            }
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage()); // Trata erros
        }
    }
}