package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

import static java.lang.Integer.parseInt;

public class UDPClient {
    private DatagramSocket udpSocket;
    private byte[] receiveData = new byte[50];
    private byte[] sendData = new byte[50];

    /**
     * Creates a DatagramSocket and sets its reception timeout
     *
     * @param timeout Timeout set for packet reception
     */
    public UDPClient(int timeout) {
        try{
            udpSocket = new DatagramSocket();
            udpSocket.setSoTimeout(timeout);
        }catch (SocketException e) {
            System.err.println("Socket error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Repeatedly reads a line from terminal, sends it to a server living at hostname:port, and waits for a reply
     * Use CTRL + D to exit
     *
     *  @param hostname Name of the UDP server
     *  @param port Port binded to the UDP server living at hostname
     */
    public int sendData(String message, String hostname, int port) {
        int counter = 0;
        int beginningIndex = 0;

        try {
            InetAddress address = InetAddress.getByName(hostname);
            String received;

            // Sending the length of the message to the server
            int messageLength = message.length();
            sendPacket(address, port, messageLength + "");

            int numberOfPackets = (int) Math.ceil((double) messageLength / 50);
            System.out.println("Number of packets -> " + numberOfPackets);

            for (int i = 0; i < numberOfPackets; i++) {
                // Extracting a chunk of 50 bytes
                int chunkSize = Math.min(50, message.length() - beginningIndex);
                String chunk = message.substring(beginningIndex, beginningIndex + chunkSize);
                System.out.println(chunk);

                boolean sentSuccessfully = false;
                for (int attempt = 0; attempt < 3; attempt++) {
                    // Sending the chunk to the server
                    sendPacket(address, port, chunk);

                    // Receiving ACK from the server
                    received = receivePacket();

                    System.out.println(received); // TODO DELETE

                    if (received.equals("ACK")) {
                        sentSuccessfully = true;
                        break;
                    }
                }

                if (!sentSuccessfully) {
                    // Handle the case where the chunk couldn't be sent after three attempts
                    System.out.println("Failed to send the chunk. Terminating!");
                    return -1;
                }

                beginningIndex += chunkSize;
            }

        } catch (SocketTimeoutException e) {
            System.err.println("Timeout reached: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }

        return 0;
    }



    public String receiveData(String hostname, int port) {
        String received;
        StringBuilder receivedBuilder = new StringBuilder();
        try{

            //Size Receive - ask mariana can t I use the receiveFunction??
            DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
            udpSocket.receive(packet);
            int size = Integer.parseInt(new String(packet.getData(), 0, packet.getLength()));
           receivedBuilder = new StringBuilder();
            for (int i = 0; i < Math.ceil((double)size/50); i++) {

                udpSocket.receive(packet);
                received = new String(packet.getData(), 0, packet.getLength());
                receivedBuilder.append(received);
                sendPacket(InetAddress.getByName(hostname),port, "ACK");
            }

        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
        return receivedBuilder.toString().trim();
    }

    private void sendPacket(InetAddress address, int port, String message) throws IOException {
        sendData = message.getBytes();
        DatagramPacket packet = new DatagramPacket(sendData, sendData.length, address, port);
        udpSocket.send(packet);
    }

    private String receivePacket() throws IOException {
        DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
        udpSocket.receive(packet);
        return new String(packet.getData(), 0, packet.getLength());
    }

    /**
     * Closes the DatagramSocket
     */
    public void close() {
        udpSocket.close();
    }

    public static void main(String[] args) throws IOException {
        String hostname = null; String portString = null; String phrase = null; String keyword = null;

        try(BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))){

            System.out.print("Enter server name or IP address: ");
            hostname = stdin.readLine();
            System.out.print("Enter port: ");
            portString = stdin.readLine();
            System.out.print("Enter string: ");
            phrase = stdin.readLine();
            System.out.print("Enter keyword: ");
            keyword = stdin.readLine();

        }catch (IOException e){
            System.err.println("I/O error: " + e.getMessage());
        }

        //Input validation
        int port = parseInt(portString);
        if(!inputDataValid(hostname,portString,phrase,keyword)) {
            System.err.println("Invalid input format. Terminating!");
            System.exit(1);
        }
        //Port validation
        if(port < 1024 || port > 49151){
            System.err.println("Invalid port number. Terminating!");
            System.exit(1);
        }

        UDPClient client = new UDPClient(1000);

        if(client.sendData(phrase,hostname,port) == -1){
            client.close();
            System.exit(1);
        }

        if(client.sendData(keyword,hostname,port) == -1){
            client.close();
            System.exit(1);
        }


        System.out.println(client.receiveData(hostname,port));

        int numeroDeVezesArepetir = Integer.parseInt(client.receiveData(hostname,port));
        for(int i = 0; i < numeroDeVezesArepetir; i++){

            System.out.println(client.receiveData(hostname,port));
        }
        client.close();
    }

    private static boolean inputDataValid(String hostname,String port, String phrase, String keyword){
        return !hostname.isEmpty() && !port.isEmpty() && !phrase.isEmpty() && !keyword.isEmpty();
    }
}
