package agri_chatbot.util;

public class IntentDetector {

    public static String detectIntent(String message) {
        message = message.toLowerCase();
        if (message.contains("crop") || message.contains("plant")) return "CROP";
        if (message.contains("fertilizer")) return "FERTILIZER";
        if (message.contains("weather") || message.contains("rain")) return "WEATHER";
        return "UNKNOWN";
    }
}
