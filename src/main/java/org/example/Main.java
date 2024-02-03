package org.example;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {


        /*String example = "Ola e adeus! Ola e adeus! Ola e adeus! Ola e adeus! Ola e adeus! Ola e adeus! Ola e adeus! Ola e adeus!";
        int offset = 0;
        StringBuilder appendedString = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            int chunkSize = Math.min(50, example.length() - offset); // Ensure chunkSize does not exceed remaining length
            String chunkOfString = example.substring(offset, offset + chunkSize);
            appendedString.append(chunkOfString);
            offset = offset + chunkSize;
        }

        System.out.println(appendedString);


        /*System.out.println("wwwwwwwwwwwwwwwwwwwww wwwwwwwwwwwwwwwwwwwwwwwwwwww wwwwwwwwwwwwwwwwwwwwwwwwwww word word word".length());

        System.out.println(example);

        System.out.println(example.length());

        System.out.println(example.getBytes().length);*/

        double numberOfPackets = Math.ceil((double) 74 / 50);
        System.out.println(numberOfPackets);

       int example = "Este pertence ao chunk 1.Este pertence ao chunk 1.".length();
        System.out.println(example);

        System.out.println("Este eh o primeiro pedaço de chunk! Este eh o primeiro pedço de chunk ! Este eh o primeiro pedaço de chunk !".length());



    }
}