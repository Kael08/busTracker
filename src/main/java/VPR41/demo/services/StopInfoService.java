package VPR41.demo.services;


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
    private final String API_URL = "https://its-rnd.ru/pikasonline/rostov/stops.txt?1732284000000";

    public StopInfoService(StopInfoRepository stopRepository, RestTemplate restTemplate) {
        this.stopRepository = stopRepository;
        this.restTemplate = restTemplate;
    }

    public List<StopInfo> getAllStops() {
        return stopRepository.findAll();
    }

    public void updateStopsFromApi() {
        try {
            String response = restTemplate.getForObject(API_URL, String.class);
            if (response != null) {
                List<StopInfo> stops = parseResponse(response);
                stopRepository.saveAll(stops); // Сохраняем все остановки в базу
            }
        } catch (Exception e) {
            e.printStackTrace(); // Логируем ошибки
        }
    }

    private List<StopInfo> parseResponse(String response) {
        List<StopInfo> stopList = new ArrayList<>();
        String[] lines = response.split("\n"); // Разделяем строки

        for (String line : lines) {
            String[] parts = line.split(";");

            if (parts.length < 3) {
                System.err.println("Некорректная строка: " + line); // Логируем ошибки формата
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


}
