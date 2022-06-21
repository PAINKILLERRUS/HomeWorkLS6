package ru.geekbrains.homework;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {
    private static DataInputStream in;
    private static DataOutputStream out;

    public static void main(String[] args) {
            try (ServerSocket serverSocket = new ServerSocket(8190)) {
                System.out.println("Ждем подключения клиета...");

                Socket socket = serverSocket.accept();

                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                System.out.println("Клиент подключился.");

                Runnable runnable = () -> {
                    while(true) {
                        Scanner scanner = new Scanner(System.in);

                        while(true) {
                            String msg = scanner.nextLine();

                            if(msg.equals("/end"))
                                break;

                            sendMessage(msg);
                        }
                    }
                };

                Thread thread = new Thread(runnable);
                thread.start();

                while (true) {
                    String message = in.readUTF();
                    if ("/end".equalsIgnoreCase(message)) {
                        out.writeUTF("/end");
                        break;
                    }
                    System.out.println("Сообщение от клиента: " + message);
                    out.writeUTF("[echo]" + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private static void sendMessage(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

