package VPR41.demo.controllers;

import VPR41.demo.models.BusInfo;
import VPR41.demo.models.StopInfo;
import VPR41.demo.services.StopInfoService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @PostMapping("/update")
    public String updateStops() {
        stopService.updateStopsFromApi();
        return "Остановки успешно обновлены и сохранены в базу!";
    }

    @Operation(
            summary = "Добавить остановку в избранное",
            description = "Добавляет остановку в список избранных по имени",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Остановка успешно добавлена в избранное"),
            }
    )
    @PostMapping("/favorites/add")
    public String addFavoriteStop(
            @Parameter(description = "Имя остановки, которую нужно добавить в избранное", example = "Центральный рынок")
            @RequestParam String stopName
    ) {
        boolean result = stopService.addToFavoritesByName(stopName);
        return result
                ? "Остановка \"" + stopName + "\" добавлена в избранное!"
                : "Остановка \"" + stopName + "\" не найдена!";
    }

    @Operation(
            summary = "Удалить остановку из избранного",
            description = "Удаляет остановку из списка избранных по имени",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Остановка успешно удалена из избранного"),
            }
    )
    @DeleteMapping("/favorites/remove")
    public String removeFavoriteStop(
            @Parameter(description = "Имя остановки, которую нужно удалить из избранного", example = "Центральный рынок")
            @RequestParam String stopName
    ) {
        boolean result = stopService.removeFromFavoritesByName(stopName);
        return result
                ? "Остановка \"" + stopName + "\" удалена из избранного!"
                : "Остановка \"" + stopName + "\" не найдена в избранном!";
    }

    @Operation(
            summary = "Получить избранные остановки",
            description = "Возвращает список всех остановок, добавленных в избранное",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список избранных остановок успешно получен")
            }
    )
    @GetMapping("/favorites")
    public List<StopInfo> getFavoriteStops() {
        return stopService.getFavoriteStops();
    }

    @GetMapping("/{stopId}/buses")
    @Operation(
            summary = "Получить список автобусов для остановки",
            description = "Возвращает список автобусов, которые прибывают на указанную остановку по её ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список автобусов успешно получен"),
    })
    public List<BusInfo> getBusesForStop(
            @Parameter(description = "Уникальный идентификатор остановки", example = "177652")
            @PathVariable String stopId
    ) {
        return stopService.getBusesForStop(stopId);
    }
}
