import java.io.*; // Importa classes de E/S
import java.net.*; // Importa classes de rede

public class CalculadoraServidor {
    public static void main(String[] args) {
        try (DatagramSocket s = new DatagramSocket(6790)) { // Cria socket na porta 6790
            byte[] buf = new byte[100]; // Buffer para dados
            System.out.println("Servidor aguardando requisições..."); // Mensagem inicial
            while (true) { // Loop principal
                System.out.println("------------------------------"); // Divisão entre requisições
                DatagramPacket p = new DatagramPacket(buf, buf.length); // Pacote para receber dados
                s.receive(p); // Recebe primeiro número
                String n1Str = new String(p.getData(), 0, p.getLength()).trim(); // Converte para string
                System.out.println("Recebido primeiro número: " + n1Str); // Imprime número recebido
                if ("exit".equalsIgnoreCase(n1Str)) { // Verifica se deve encerrar
                    System.out.println("Servidor encerrando."); // Mensagem de encerramento
                    break; // Interrompe o loop
                }
                double n1 = Double.parseDouble(n1Str); // Converte primeiro número para double
                s.receive(p); // Recebe operador
                String op = new String(p.getData(), 0, p.getLength()).trim(); // Converte para string
                System.out.println("Recebido operador: " + op); // Imprime operador recebido
                s.receive(p); // Recebe segundo número
                String n2Str = new String(p.getData(), 0, p.getLength()).trim(); // Converte para string
                System.out.println("Recebido segundo número: " + n2Str); // Imprime número recebido
                double n2 = Double.parseDouble(n2Str); // Converte segundo número para double
                double res = 0; boolean val = true; // Inicializa resultado e flag de operação válida
                switch (op) { // Seleciona operação
                    case "+" -> res = n1 + n2; // Soma
                    case "-" -> res = n1 - n2; // Subtração
                    case "*" -> res = n1 * n2; // Multiplicação
                    case "/" -> { if (n2 != 0) res = n1 / n2; else { val = false; System.out.println("Erro: Divisão por zero."); } } // Divisão
                    default -> { val = false; System.out.println("Erro: Operador inválido."); } // Operador inválido
                }
                String resStr = val ? String.valueOf(res) : "Erro"; // Prepara resultado
                byte[] resBytes = resStr.getBytes(); // Converte resultado para bytes
                DatagramPacket resP = new DatagramPacket(resBytes, resBytes.length, p.getAddress(), p.getPort()); // Pacote de resposta
                s.send(resP); // Envia resultado ao cliente
                System.out.println("Resultado enviado: " + resStr); // Imprime resultado enviado
            }
        } catch (IOException e) { System.out.println("Erro: " + e.getMessage()); } // Trata exceções de E/S
    }
}