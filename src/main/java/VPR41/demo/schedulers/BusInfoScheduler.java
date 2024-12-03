package VPR41.demo.schedulers;

import VPR41.demo.services.BusInfoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BusInfoScheduler {

    private final BusInfoService busInfoService;

    public BusInfoScheduler(BusInfoService busInfoService) {
        this.busInfoService = busInfoService;
    }

    @Scheduled(fixedRate = 5000) // Каждые 5 секунд
    public void fetchBusInfo() {
        busInfoService.updateBusInfo();
    }
}
