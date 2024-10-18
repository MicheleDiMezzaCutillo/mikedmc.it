package it.mikedmc.util;

import java.util.Random;

public class CodeGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String generateCode(int length) {
        StringBuilder code = new StringBuilder(length);
        Random random = new Random();

        // Genera 'length' caratteri casuali (lettere maiuscole o numeri)
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }

    public static void main(String[] args) {
        int length = 8;  // Puoi passare qualunque lunghezza desideri
        String generatedCode = generateCode(length);
        System.out.println("Generated Code: " + generatedCode);
    }
}
