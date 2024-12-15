package VPR41.demo.controllers;


import VPR41.demo.models.BusInfo;
import VPR41.demo.services.BusInfoService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Получить информацию обо всех маршрутах",
            description = "Возвращает список всех маршрутов, сохраненных в кеше",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список маршрутов успешно получен")
            }
    )
    @GetMapping
    public List<BusInfo> getAllBusInfo() {
        return busInfoService.getCachedBusInfo();
    }

    @Operation(
            summary = "Получить список избранных автобусов",
            description = "Возвращает информацию об избранных автобусах",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список избранных автобусов успешно получен")
            }
    )
    @GetMapping("/favorites")
    public List<BusInfo> getFavoriteBuses() {
        return busInfoService.getFavoriteBuses();
    }

    @Operation(
            summary = "Добавить автобус в избранное",
            description = "Добавляет номер автобуса в список избранного",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Автобус успешно добавлен в избранное"),
                    @ApiResponse(responseCode = "400", description = "Ошибка: Номер автобуса не указан")
            }
    )
    @PostMapping("/favorites/add")
    public String addToFavorites(
            @Parameter(description = "Номер автобуса, который нужно добавить в избранное", example = "123")
            @RequestParam String busNumber
    ) {
        busInfoService.addToFavorites(busNumber);
        return "Автобус " + busNumber + " добавлен в избранное!";
    }

    @Operation(
            summary = "Удалить автобус из избранного",
            description = "Удаляет номер автобуса из списка избранного",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Автобус успешно удален из избранного"),
                    @ApiResponse(responseCode = "400", description = "Ошибка: Номер автобуса не указан")
            }
    )
    @DeleteMapping("/favorites/remove")
    public String removeFromFavorites(
            @Parameter(description = "Номер автобуса, который нужно удалить из избранного", example = "123")
            @RequestParam String busNumber
    ) {
        busInfoService.removeFromFavorites(busNumber);
        return "Автобус " + busNumber + " удален из избранного!";
    }
}