import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CalculadoraCliente {
    public static void main(String[] args) {
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            Scanner scanner = new Scanner(System.in);

            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 6789;

            while (true) {
                System.out.println("------------------------------");

                // Receber primeiro número
                System.out.print("Digite o primeiro número (ou 0 para sair): ");
                String firstNumber = scanner.nextLine();

                if ("0".equals(firstNumber)) {
                    // Enviar sinal para o servidor encerrar
                    byte[] exitBytes = "0".getBytes();
                    DatagramPacket exitPacket = new DatagramPacket(exitBytes, exitBytes.length, serverAddress, serverPort);
                    clientSocket.send(exitPacket);
                    System.out.println("Encerrando o cliente.");
                    break;
                }

                System.out.println("Enviando primeiro número: " + firstNumber);
                byte[] firstNumberBytes = firstNumber.getBytes();
                DatagramPacket firstNumberPacket = new DatagramPacket(firstNumberBytes, firstNumberBytes.length, serverAddress, serverPort);
                clientSocket.send(firstNumberPacket);

                // Receber operador
                System.out.print("Digite o operador (+, -, *, /): ");
                String operator = scanner.nextLine();
                System.out.println("Enviando operador: " + operator);
                byte[] operatorBytes = operator.getBytes();
                DatagramPacket operatorPacket = new DatagramPacket(operatorBytes, operatorBytes.length, serverAddress, serverPort);
                clientSocket.send(operatorPacket);

                // Receber segundo número
                System.out.print("Digite o segundo número: ");
                String secondNumber = scanner.nextLine();
                System.out.println("Enviando segundo número: " + secondNumber);
                byte[] secondNumberBytes = secondNumber.getBytes();
                DatagramPacket secondNumberPacket = new DatagramPacket(secondNumberBytes, secondNumberBytes.length, serverAddress, serverPort);
                clientSocket.send(secondNumberPacket);

                // Receber resposta
                byte[] buffer = new byte[100];
                DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
                clientSocket.receive(responsePacket);

                System.out.println("Resultado: " + new String(responsePacket.getData()).trim());
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}