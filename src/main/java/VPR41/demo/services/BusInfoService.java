package VPR41.demo.services;


import VPR41.demo.models.BusInfo;
import VPR41.demo.repository.BusInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BusInfoService {

    private final RestTemplate restTemplate;
    private final BusInfoRepository busInfoRepository;  // Add the repository
    private final String API_URL = "https://its-rnd.ru/pikasonline/p04ktwt0.txt?1732284969269";

    private List<BusInfo> cachedData = new ArrayList<>(); // Cache for data
    private final Set<String> favoriteBusNumbers = new HashSet<>(); // Избранное

    public BusInfoService(RestTemplate restTemplate, BusInfoRepository busInfoRepository) {
        this.restTemplate = restTemplate;
        this.busInfoRepository = busInfoRepository;  // Initialize repository
    }

    public List<BusInfo> getCachedBusInfo() {
        return cachedData; // Return the latest cached data
    }

    // Получить список избранных автобусов
    public List<BusInfo> getFavoriteBuses() {
        if (cachedData.isEmpty()) {
            updateBusInfo();
        }
        return cachedData.stream()
                .filter(bus -> favoriteBusNumbers.contains(bus.getBusNumber()))
                .toList();
    }

    // Добавить номер автобуса в избранное
    public void addToFavorites(String busNumber) {
        favoriteBusNumbers.add(busNumber);
    }

    // Удалить номер автобуса из избранного
    public void removeFromFavorites(String busNumber) {
        favoriteBusNumbers.remove(busNumber);
    }

    public void updateBusInfo() {
        try {
            String response = restTemplate.getForObject(API_URL, String.class);
            if (response != null) {
                // Parse the response data
                List<BusInfo> parsedData = parseResponse(response);
                cachedData = parsedData;

                // Save the parsed data into the database
                busInfoRepository.saveAll(parsedData);  // Save all the parsed data to the DB
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log errors
        }
    }

    private List<BusInfo> parseResponse(String response) {
        List<BusInfo> busInfoList = new ArrayList<>();
        String[] lines = response.split("\n"); // Split the response by newlines
        for (String line : lines) {
            String[] parts = line.split(",");
            try {
                BusInfo busInfo = new BusInfo();
                busInfo.setIdRoute(parts[0]);
                busInfo.setBusNumber(parts[1]);
                busInfo.setLatitude(Double.parseDouble(parts[3]) / 1000000);
                busInfo.setLongitude(Double.parseDouble(parts[2]) / 1000000);
                busInfo.setSpeed(parts[4].isEmpty() ? null : Integer.parseInt(parts[4]));
                busInfo.setAzimuth(parts[5].isEmpty() ? null : Integer.parseInt(parts[5]));
                busInfo.setRegNumber(parts.length > 6 ? parts[6] : null);
                busInfoList.add(busInfo);
            } catch (Exception e) {
                e.printStackTrace(); // Log errors in parsing
            }
        }
        return busInfoList;
    }

    public List<BusInfo> getBusesForStop(String stopId) {
        List<BusInfo> buses = cachedData.stream()
                .filter(bus -> bus.getIdRoute().equalsIgnoreCase(stopId))
                .toList();

        buses.forEach(bus -> bus.setEstimatedArrivalTime((int) (Math.random() * 16)));
        return buses;
    }
}