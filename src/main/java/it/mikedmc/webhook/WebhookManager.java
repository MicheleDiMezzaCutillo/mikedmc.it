package it.mikedmc.webhook;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebhookManager {
	public static boolean sendWebhook(String webhookUrl, String jsonPayload) throws Exception {
        URL url = new URL(webhookUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        // Imposta la richiesta come POST
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        
        // Invia il payload JSON
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        
        // Verifica la risposta
        int responseCode = connection.getResponseCode();
        connection.disconnect();
        if (responseCode == HttpURLConnection.HTTP_NO_CONTENT || responseCode == HttpURLConnection.HTTP_OK) {
            return true;
        } else {
            return false;
        }        
    }
}
