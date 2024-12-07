import java.io.*; // Importa classes de E/S
import java.net.*; // Importa classes de rede

public class CalculadoraServidor {
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(6789)) { // Cria socket na porta 6789
            byte[] buffer = new byte[100]; // Buffer para dados
            System.out.println("Servidor aguardando requisições...");

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length); // Pacote para receber
                socket.receive(packet); // Recebe primeiro número
                String num1Str = new String(packet.getData(), 0, packet.getLength()).trim(); // Converte primeiro número

                if ("0".equals(num1Str)) { // Verifica sinal de encerramento
                    System.out.println("Servidor encerrando.");
                    break; // Sai do loop
                }

                double num1 = Double.parseDouble(num1Str); // Converte para double

                socket.receive(packet); // Recebe operador
                String oper = new String(packet.getData(), 0, packet.getLength()).trim(); // Converte operador

                socket.receive(packet); // Recebe segundo número
                double num2 = Double.parseDouble(new String(packet.getData(), 0, packet.getLength()).trim()); // Converte segundo número

                double result = 0; // Inicializa resultado
                boolean valid = true; // Flag de operação válida

                switch (oper) { // Realiza operação
                    case "+":
                        result = num1 + num2; // Soma
                        break;
                    case "-":
                        result = num1 - num2; // Subtração
                        break;
                    case "*":
                        result = num1 * num2; // Multiplicação
                        break;
                    case "/":
                        if (num2 != 0) result = num1 / num2; // Divisão
                        else valid = false; // Divisão por zero
                        break;
                    default:
                        valid = false; // Operador inválido
                }

                String resStr = valid ? String.valueOf(result) : "Erro"; // Prepara resultado
                byte[] resBytes = resStr.getBytes(); // Converte resultado
                DatagramPacket resPacket = new DatagramPacket(resBytes, resBytes.length, packet.getAddress(), packet.getPort()); // Pacote de resposta
                socket.send(resPacket); // Envia resultado
            }
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage()); // Trata erros
        }
    }
}