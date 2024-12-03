package VPR41.demo.controllers;


import VPR41.demo.models.BusInfo;
import VPR41.demo.services.BusInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "BusInfo API", description = "API для работы с информацией о маршрутах автобусов")
@RestController
@RequestMapping("/api/buses")
public class BusInfoController {

    private final BusInfoService busInfoService;

    @Autowired
    public BusInfoController(BusInfoService busInfoService) {
        this.busInfoService = busInfoService;
    }

    @Operation(summary = "Получить информацию обо всех маршрутах", description = "Возвращает список всех маршрутов, сохраненных в кеше")
    @GetMapping
    public List<BusInfo> getAllBusInfo() {
        return busInfoService.getCachedBusInfo();
    }
}

