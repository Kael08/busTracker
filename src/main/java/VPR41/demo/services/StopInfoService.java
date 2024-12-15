package VPR41.demo.services;

import VPR41.demo.models.BusInfo;
import VPR41.demo.models.StopInfo;
import VPR41.demo.repository.StopInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class StopInfoService {

    private final StopInfoRepository stopRepository;
    private final RestTemplate restTemplate;

    private final BusInfoService busInfoService;
    private final String API_URL = "https://its-rnd.ru/pikasonline/rostov/stops.txt?1732284000000";

    private List<String> favoriteStopNames = new ArrayList<>(); // Список имен избранных остановок

    public StopInfoService(StopInfoRepository stopRepository, RestTemplate restTemplate, BusInfoService busInfoService) {
        this.stopRepository = stopRepository;
        this.restTemplate = restTemplate;
        this.busInfoService = busInfoService;
    }

    public List<StopInfo> getAllStops() {
        return stopRepository.findAll();
    }

    public void updateStopsFromApi() {
        try {
            String response = restTemplate.getForObject(API_URL, String.class);
            List<StopInfo> stops = parseResponse(response);
            stopRepository.saveAll(stops); // Сохраняем все остановки в базу
        } catch (Exception e) {
            e.printStackTrace(); // Логируем ошибки
        }
    }

    public boolean addToFavoritesByName(String stopName) {
        List<StopInfo> allStops = stopRepository.findAll();
        for (StopInfo stop : allStops) {
            if (stop.getName() != null && stop.getName().equalsIgnoreCase(stopName)) {
                if (!favoriteStopNames.contains(stopName)) {
                    favoriteStopNames.add(stopName); // Добавляем имя остановки в избранное
                }
                return true;
            }
        }
        return false; // Остановка не найдена
    }

    public boolean removeFromFavoritesByName(String stopName) {
        return favoriteStopNames.removeIf(name -> name.equalsIgnoreCase(stopName));
    }

    public List<StopInfo> getFavoriteStops() {
        return stopRepository.findAll()
                .stream()
                .filter(stop -> stop.getName() != null && favoriteStopNames.contains(stop.getName()))
                .toList();
    }

    private List<StopInfo> parseResponse(String response) {
        List<StopInfo> stopList = new ArrayList<>();
        String[] lines = response.split("\n");

        for (String line : lines) {
            String[] parts = line.split(";");

            if (parts.length < 3) {
                System.err.println("Некорректная строка: " + line);
                continue;
            }

            try {
                StopInfo stop = new StopInfo();
                stop.setIdStop(parts[0]);

                if (isNumeric(parts[1]) && isNumeric(parts[2])) {
                    stop.setLat(Double.parseDouble(parts[1]));
                    stop.setLng(Double.parseDouble(parts[2]));
                } else {
                    System.err.println("Некорректные координаты: " + parts[1] + ", " + parts[2]);
                    continue;
                }

                stop.setStops(parts[3]);
                stop.setName(parts.length > 4 ? parts[4] : null);
                stopList.add(stop);

            } catch (Exception e) {
                System.err.println("Ошибка обработки строки: " + line);
                e.printStackTrace();
            }
        }
        return stopList;
    }

    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public List<BusInfo> getBusesForStop(String stopId) {
        return busInfoService.getBusesForStop(stopId);
    }
}