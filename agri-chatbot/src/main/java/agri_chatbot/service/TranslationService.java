package agri_chatbot.service;

import org.springframework.stereotype.Service;

@Service
public class TranslationService {

	public String translateToEnglish(String text, String lang) {
	    if ("te".equalsIgnoreCase(lang)) {
	        // naive transliteration / translation demo
	        if (text.contains("వర్షం") || text.contains("వాతావరణం")) {
	            return "rain";
	        }
	        if (text.contains("పంట")) {
	            return "crop";
	        }
	        if (text.contains("ఎరువు")) {
	            return "fertilizer";
	        }
	    }
	    if ("hi".equalsIgnoreCase(lang)) {
	        if (text.contains("बारिश") || text.contains("मौसम")) {
	            return "rain";
	        }
	        if (text.contains("फसल")) {
	            return "crop";
	        }
	        if (text.contains("उर्वरक")) {
	            return "fertilizer";
	        }
	    }
	    return text; // English or unknown
	}

    public String translateFromEnglish(String text, String lang) {
        if ("te".equalsIgnoreCase(lang)) return translateToTelugu(text);
        if ("hi".equalsIgnoreCase(lang)) return translateToHindi(text);
        return text;
    }

    private String translateToTelugu(String text) {
        text = text.toLowerCase();
        if (text.contains("rice") || text.contains("maize")) return "ప్రస్తుత కాలానికి వరి మరియు మక్కజొన్న పంటలు అనుకూలంగా ఉంటాయి.";
        if (text.contains("fertilizer") || text.contains("urea")) return "యూరియా మరియు డీఏపీ సరైన పరిమాణంలో ఉపయోగించండి.";
        if (text.contains("rain") || text.contains("weather")) return "ఈరోజు వర్షం పడే అవకాశం ఉంది. నీటిపారుదల ఆలస్యం చేయండి.";
        return "క్షమించండి, మీ ప్రశ్న అర్థం కాలేదు.";
    }

    private String translateToHindi(String text) {
        text = text.toLowerCase();
        if (text.contains("rice") || text.contains("maize")) return "वर्तमान मौसम में धान और मक्का की फसल उपयुक्त है।";
        if (text.contains("fertilizer") || text.contains("urea")) return "उचित मात्रा में यूरिया और डीएपी का उपयोग करें।";
        if (text.contains("rain") || text.contains("weather")) return "आज बारिश की संभावना है। सिंचाई टालें।";
        return "माफ़ कीजिए, मैं आपका प्रश्न समझ नहीं पाया।";
    }
    
    
}
