package agri_chatbot.model;

public class ChatRequest {

    private String message;
    private String city;
    private String language;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}
