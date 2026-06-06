package agri_chatbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getWeatherAdvice(String city) {
        if (city == null || city.isEmpty()) city = "Hyderabad";

        String url = apiUrl + "?q=" + city + "&appid=" + apiKey + "&units=metric";
        System.out.println("Weather URL: " + url);

        try {
            String response = restTemplate.getForObject(url, String.class);
            JSONObject json = new JSONObject(response);
            String weatherMain = json.getJSONArray("weather").getJSONObject(0).getString("main");
            double temp = json.getJSONObject("main").getDouble("temp");

            if (weatherMain.equalsIgnoreCase("Rain")) return "Rain expected today in " + city + ". Avoid irrigation.";
            if (temp > 35) return "Hot weather in " + city + ". Water crops properly.";
            return "Weather is favorable in " + city + ".";
        } catch (Exception e) {
            return "Unable to fetch weather for " + city;
        }
    }

    public String getWeatherAdvice() { return getWeatherAdvice("Hyderabad"); }
}
