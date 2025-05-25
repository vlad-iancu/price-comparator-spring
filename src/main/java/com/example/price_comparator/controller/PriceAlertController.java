package com.example.price_comparator.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.example.price_comparator.dto.ApiResponse;
import com.example.price_comparator.dto.PriceAlertCreateRequest;
import com.example.price_comparator.dto.PriceAlertListResponse;
import com.example.price_comparator.dto.PriceAlertResponse;
import com.example.price_comparator.entity.PriceAlert;
import com.example.price_comparator.entity.User;
import com.example.price_comparator.service.PriceAlertService;
import static com.example.price_comparator.security.JWTUtils.getUserFromJwtUser;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class PriceAlertController {
    private final PriceAlertService priceAlertService;

    @GetMapping("/price_alert")
    public ResponseEntity<ApiResponse<PriceAlertResponse>> getPriceAlert(
            @AuthenticationPrincipal Jwt jwtUser,
            @RequestParam("id") Long id) {
        User user = getUserFromJwtUser(jwtUser);
        var alert = priceAlertService.getPriceAlertById(user, id);
        return ResponseEntity.ok(new ApiResponse<>(alert));
    }

    @GetMapping("/price_alerts")
    public ResponseEntity<ApiResponse<PriceAlertListResponse>> getAllPriceAlerts(
            @AuthenticationPrincipal Jwt jwtUser) {
        User user = getUserFromJwtUser(jwtUser);
        List<PriceAlertResponse> alerts = priceAlertService.getAllPriceAlerts(user);
        return ResponseEntity.ok(new ApiResponse<>(new PriceAlertListResponse(alerts)));
    }

    @GetMapping("/active_price_alerts")
    public ResponseEntity<ApiResponse<PriceAlertListResponse>> getActivePriceAlerts(@AuthenticationPrincipal Jwt jwtUser) {
        User user = getUserFromJwtUser(jwtUser);
        List<PriceAlert> activeAlerts = priceAlertService.getTriggeredPriceAlerts(user);
        var responses = activeAlerts.stream()
                .map(PriceAlertResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>(new PriceAlertListResponse(responses)));
    }
    

    @PostMapping("/price_alert")
    public ResponseEntity<ApiResponse<PriceAlertResponse>> createPriceAlert(
            @AuthenticationPrincipal Jwt jwtUser,
            @RequestBody @Valid PriceAlertCreateRequest request) {
        User user = getUserFromJwtUser(jwtUser);
        var alert = priceAlertService.createPriceAlert(user, request);
        return ResponseEntity.ok(new ApiResponse<>(alert));
    }

    @PutMapping("/price_alert")
    public ResponseEntity<ApiResponse<PriceAlertResponse>> updatePriceAlert(
            @AuthenticationPrincipal Jwt jwtUser,
            @RequestParam("id") Long id,
            @RequestBody @Valid PriceAlertCreateRequest request) {
        User user = getUserFromJwtUser(jwtUser);
        var alert = priceAlertService.updatePriceAlert(user, id, request);
        return ResponseEntity.ok(new ApiResponse<>(alert));
    }

    @DeleteMapping("/price_alert")
    public ResponseEntity<ApiResponse<Void>> deletePriceAlert(
            @AuthenticationPrincipal Jwt jwtUser,
            @RequestParam("id") Long id) {
        User user = getUserFromJwtUser(jwtUser);
        priceAlertService.deletePriceAlert(user, id);
        return ResponseEntity.ok(new ApiResponse<>(null));
    }


}
