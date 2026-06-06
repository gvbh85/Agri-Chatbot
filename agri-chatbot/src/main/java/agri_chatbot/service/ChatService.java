package agri_chatbot.service;

import agri_chatbot.util.IntentDetector;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final WeatherService weatherService;
    private final TranslationService translationService;
    private final GeminiService geminiService;

    public ChatService(WeatherService weatherService,
                       TranslationService translationService,
                       GeminiService geminiService) {
        this.weatherService = weatherService;
        this.translationService = translationService;
        this.geminiService = geminiService;
    }

    /* -------------------------------------------------
       FINAL METHOD: message + city + language
       Uses Gemini AI first, fallback to rule-based
    ------------------------------------------------- */
    public String getReply(String message, String city, String language) {

        // Translate user input to English
        String englishMsg = translationService.translateToEnglish(message, language);

        // Auto-detect city if not provided
        String detectedCity = (city == null || city.isEmpty())
                ? extractCity(englishMsg.toLowerCase())
                : city;

        String response;

        // If language is not English, try AI (Gemini) or intent detection
        if (!"en".equalsIgnoreCase(language)) {
            // Detect intent from translated English
            String intent = IntentDetector.detectIntent(englishMsg);
            switch (intent) {
                case "WEATHER":
                    response = weatherService.getWeatherAdvice(detectedCity);
                    break;
                case "CROP":
                    response = "Rice, maize, and pulses are suitable crops this season.";
                    break;
                case "FERTILIZER":
                    response = "Use compost or urea in recommended quantities.";
                    break;
                default:
                    // fallback to Gemini AI
                    response = geminiService.askGemini(englishMsg + " (City: " + detectedCity + ")");
            }
        } else {
            // English: old logic
        	response = getRuleBasedReply(englishMsg, detectedCity);

        }

        // Translate response back to user’s language
        return translationService.translateFromEnglish(response, language);
    }

    /* -------------------------------------------------
       Rule-based fallback logic
    ------------------------------------------------- */
    private String getRuleBasedReply(String message, String city) {
        message = message.toLowerCase();

        if (message.contains("weather") || message.contains("rain")) {
            return weatherService.getWeatherAdvice(city);
        } else if (message.contains("crop")) {
            return "Rice, maize, and pulses are suitable crops this season.";
        } else if (message.contains("fertilizer") || message.contains("fertiliser")) {
            return "Use compost or urea in recommended quantities.";
        } else {
            return "Please ask about weather, crops, or fertilizers.";
        }
    }

    /* -------------------------------------------------
       CITY EXTRACTION LOGIC
    ------------------------------------------------- */
    private String extractCity(String message) {

        String[][] cities = {
            {"hyderabad", "Hyderabad"},
            {"chennai", "Chennai"},
            {"delhi", "Delhi"},
            {"srikakulam", "Srikakulam"},
            {"vizag", "Visakhapatnam"}, // <-- OpenWeatherMap official name
            {"vishakapatnam", "Visakhapatnam"},
            {"vijayawada", "Vijayawada"}
        };

        for (String[] cityPair : cities) {
            if (message.contains(cityPair[0].toLowerCase())) {
                return cityPair[1];
            }
        }

        return "Hyderabad"; // default city
    }

}
