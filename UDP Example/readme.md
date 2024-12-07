# Exemplo de Cliente-Servidor UDP

Este é um exemplo de aplicação cliente-servidor que utiliza sockets UDP para comunicação. O servidor aguarda requisições de clientes e responde com a mesma mensagem recebida.

## Pré-requisitos

- Java Development Kit (JDK) instalado
- Um terminal ou prompt de comando

## Como compilar

1. Abra um terminal ou prompt de comando.
2. Navegue até o diretório onde os arquivos `UdpCliente.java` e `UdpServidor.java` estão localizados.
3. Compile os arquivos Java com os seguintes comandos:

```sh
javac UdpCliente.java UdpServidor.java
```

4. Execute os arquivos em terminais separados

```sh
java UdpServidor
java UdpCliente <mensagem> <endereço_do_servidor>
```

Substitua `<mensagem>` pela mensagem que deseja enviar e `<endereço_do_servidor>` pelo endereço do servidor (por exemplo, `localhost`).