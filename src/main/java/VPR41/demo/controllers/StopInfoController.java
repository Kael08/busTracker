package VPR41.demo.controllers;

import VPR41.demo.models.StopInfo;
import VPR41.demo.services.StopInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "StopInfo API", description = "API для работы с остановками")
@RestController
@RequestMapping("/api/stops")
public class StopInfoController {

    private final StopInfoService stopService;

    @Autowired
    public StopInfoController(StopInfoService stopService) {
        this.stopService = stopService;
    }

    @Operation(summary = "Получить список всех остановок", description = "Возвращает список всех остановок из базы данных")
    @GetMapping
    public List<StopInfo> getAllStops() {
        return stopService.getAllStops();
    }

    @Operation(summary = "Обновить данные остановок", description = "Обновляет информацию об остановках из внешнего API и сохраняет в базу данных")
    @GetMapping("/update")
    public String updateStops() {
        stopService.updateStopsFromApi();
        return "Остановки успешно обновлены и сохранены в базу!";
    }
}

