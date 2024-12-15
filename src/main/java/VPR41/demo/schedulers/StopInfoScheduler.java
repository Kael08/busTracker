package VPR41.demo.schedulers;

import VPR41.demo.services.StopInfoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StopInfoScheduler {

    private final StopInfoService stopInfoService;

    public StopInfoScheduler(StopInfoService stopInfoService) {
        this.stopInfoService = stopInfoService;
    }

    @Scheduled(fixedRate = 150000) // Каждые 150 секунд
    public void fetchBusInfo() {
        stopInfoService.updateStopsFromApi();
    }
}