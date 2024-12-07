import java.io.*;
import java.net.*;

public class CalculadoraServidor {
    public static void main(String[] args) {
        try (DatagramSocket serverSocket = new DatagramSocket(6789)) {
            byte[] buffer = new byte[100];
            System.out.println("Servidor aguardando requisições...");

            while (true) {
                // Receber primeiro número
                DatagramPacket firstNumberPacket = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(firstNumberPacket);
                String firstNumberStr = new String(firstNumberPacket.getData(), 0, firstNumberPacket.getLength()).trim();

                if ("0".equals(firstNumberStr)) {
                    System.out.println("Servidor encerrando.");
                    break;
                }

                System.out.println("Recebido primeiro número: " + firstNumberStr);
                double firstNumber = Double.parseDouble(firstNumberStr);

                // Receber operador
                DatagramPacket operatorPacket = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(operatorPacket);
                String operator = new String(operatorPacket.getData(), 0, operatorPacket.getLength()).trim();
                System.out.println("Recebido operador: " + operator);

                // Receber segundo número
                DatagramPacket secondNumberPacket = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(secondNumberPacket);
                String secondNumberStr = new String(secondNumberPacket.getData(), 0, secondNumberPacket.getLength()).trim();
                System.out.println("Recebido segundo número: " + secondNumberStr);
                double secondNumber = Double.parseDouble(secondNumberStr);

                // Realizar cálculo
                double result;
                boolean validOperation = true;
                result = switch (operator) {
                    case "+" -> firstNumber + secondNumber;
                    case "-" -> firstNumber - secondNumber;
                    case "*" -> firstNumber * secondNumber;
                    case "/" -> {
                        if (secondNumber != 0) {
                            yield firstNumber / secondNumber;
                        } else {
                            System.out.println("Erro: Divisão por zero.");
                            validOperation = false;
                            yield 0;
                        }
                    }
                    default -> {
                        System.out.println("Operador inválido.");
                        validOperation = false;
                        yield 0;
                    }
                };

                if (!validOperation) {
                    continue;
                }

                // Enviar resultado
                String resultStr = Double.toString(result);
                byte[] resultBytes = resultStr.getBytes();
                DatagramPacket resultPacket = new DatagramPacket(resultBytes, resultBytes.length, firstNumberPacket.getAddress(), firstNumberPacket.getPort());
                serverSocket.send(resultPacket);

                System.out.println("Resultado enviado: " + resultStr);
                System.out.println("------------------------------");
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}