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

                String num1;
                do {
                    System.out.print("Digite o primeiro número (ou 0 para sair): ");
                    num1 = scanner.nextLine().trim(); // Lê e remove espaços
                    if (num1.isEmpty() || !isNumeric(num1)) {
                        System.out.println("Entrada inválida. Por favor, insira um número válido.");
                    }
                } while (num1.isEmpty() || !isNumeric(num1)); // Repete se a entrada estiver vazia ou não for numérica

                if ("0".equals(num1)) { // Verifica se deve encerrar
                    socket.send(new DatagramPacket(num1.getBytes(), num1.length(), address, port)); // Envia sinal de encerramento
                    System.out.println("Encerrando o cliente.");
                    break; // Sai do loop
                }

                socket.send(new DatagramPacket(num1.getBytes(), num1.length(), address, port)); // Envia primeiro número

                String oper;
                do {
                    System.out.print("Digite o operador (+, -, *, /): ");
                    oper = scanner.nextLine().trim();
                    if (oper.isEmpty() || !isValidOperator(oper)) {
                        System.out.println("Entrada inválida. Por favor, insira um operador válido.");
                    }
                } while (oper.isEmpty() || !isValidOperator(oper)); // Repete se a entrada estiver vazia ou não for um operador válido

                socket.send(new DatagramPacket(oper.getBytes(), oper.length(), address, port)); // Envia operador

                String num2;
                do {
                    System.out.print("Digite o segundo número: ");
                    num2 = scanner.nextLine().trim();
                    if (num2.isEmpty() || !isNumeric(num2)) {
                        System.out.println("Entrada inválida. Por favor, insira um número válido.");
                    }
                } while (num2.isEmpty() || !isNumeric(num2)); // Repete se a entrada estiver vazia ou não for numérica

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

    // Método para verificar se uma string é numérica
    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Método para verificar se um operador é válido
    private static boolean isValidOperator(String oper) {
        return oper.equals("+") || oper.equals("-") || oper.equals("*") || oper.equals("/");
    }
}