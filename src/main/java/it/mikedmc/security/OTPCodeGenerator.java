package it.mikedmc.security;

import java.security.SecureRandom;
import java.time.Instant;

public class OTPCodeGenerator {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateOTPCode() {
        // Ottieni il timestamp corrente
        String timestampSegment = getTimestampSegment();

        // Genera una stringa casuale di 30 caratteri
        String randomSegment = generateRandomString(30, CHARACTERS);

        // Mescola il timestamp nel segmento casuale
        String mixedString = mixTimestampInString(randomSegment, timestampSegment);

        // Dividi la stringa risultante in tre gruppi di 10 caratteri
        return mixedString.substring(0, 10) + "-" + mixedString.substring(10, 20) + "-" + mixedString.substring(20, 30);
    }

    private static String generateRandomString(int length, String characters) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(RANDOM.nextInt(characters.length())));
        }
        return sb.toString();
    }

    private static String getTimestampSegment() {
        // Usa il tempo corrente in millisecondi e prendi i primi 10 caratteri numerici
        long timestamp = Instant.now().toEpochMilli();
        String timestampStr = Long.toString(timestamp);

        // Se il timestamp è più corto di 10 caratteri, riempi con zeri
        if (timestampStr.length() < 10) {
            timestampStr = String.format("%010d", Long.parseLong(timestampStr));
        }

        // Prendi i primi 10 caratteri del timestamp
        return timestampStr.substring(0, 10);
    }

    private static String mixTimestampInString(String randomSegment, String timestampSegment) {
        // Converte la stringa casuale in un array di caratteri
        char[] mixedArray = randomSegment.toCharArray();

        // Integra il timestamp nel array di caratteri
        for (int i = 0; i < timestampSegment.length(); i++) {
            mixedArray[i * 3 % mixedArray.length] = timestampSegment.charAt(i);
        }

        // Restituisce la stringa risultante
        return new String(mixedArray);
    }
    
}