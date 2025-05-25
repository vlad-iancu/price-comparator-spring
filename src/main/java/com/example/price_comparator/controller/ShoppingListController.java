package com.example.price_comparator.controller;

import java.util.List;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.price_comparator.dto.ApiResponse;
import com.example.price_comparator.dto.BestBuyRequest;
import com.example.price_comparator.dto.BestBuyResponse;
import com.example.price_comparator.dto.ProductStatsRequest;
import com.example.price_comparator.dto.ProductStatsResponse;
import com.example.price_comparator.dto.ShoppingListCreateRequest;
import com.example.price_comparator.dto.ShoppingListResponse;
import com.example.price_comparator.dto.ShoppingListDeleteRequest;
import com.example.price_comparator.entity.User;
import com.example.price_comparator.service.ShoppingListService;
import static com.example.price_comparator.security.JWTUtils.getUserFromJwtUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class ShoppingListController {
    private final ShoppingListService shoppingListService;

    @PostMapping("/shopping_list")
    public ResponseEntity<ApiResponse<ShoppingListResponse>> createShoppingList(
            @AuthenticationPrincipal Jwt jwtUser,
            @RequestBody @Valid ShoppingListCreateRequest request) {
        User user = getUserFromJwtUser(jwtUser);
        var shoppingList = shoppingListService.createShoppingList(user, request.getProductIds(), request.getName());
        var response = ShoppingListResponse.fromEntity(shoppingList);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @PutMapping("/shopping_list_item")
    public ResponseEntity<ApiResponse<ShoppingListResponse>> addShoppingListItem(
        @AuthenticationPrincipal Jwt jwtUser,
        @RequestParam("shoppingListId") Long shoppingListId,
        @RequestParam("productId") Long productId) {
        User user = getUserFromJwtUser(jwtUser);
        var shoppingList = shoppingListService.addProductToShoppingList(user, shoppingListId, productId);
        var response = ShoppingListResponse.fromEntity(shoppingList);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @DeleteMapping("/shopping_list_item")
    public ResponseEntity<ApiResponse<ShoppingListResponse>> removeShoppingListItem(
        @AuthenticationPrincipal Jwt jwtUser,
        @RequestParam("shoppingListId") Long shoppingListId,
        @RequestParam("productId") Long productId) {
        User user = getUserFromJwtUser(jwtUser);
        var shoppingList = shoppingListService.removeProductFromShoppingList(user, shoppingListId, productId);
        var response = ShoppingListResponse.fromEntity(shoppingList);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @GetMapping("/shopping_list")
    public ResponseEntity<ApiResponse<ShoppingListResponse>> getShoppingList(
            @AuthenticationPrincipal Jwt jwt_user,
            @RequestParam("id") Long shoppingListId) {
        User user = getUserFromJwtUser(jwt_user);
        var shoppingList = shoppingListService.getShoppingList(user, shoppingListId);
        var response = ShoppingListResponse.fromEntity(shoppingList);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @GetMapping("/shopping_lists")
    public ResponseEntity<ApiResponse<List<ShoppingListResponse>>> getAllShoppingLists(
            @AuthenticationPrincipal Jwt jwtUser) {
        User user = getUserFromJwtUser(jwtUser);
        var lists = shoppingListService.getAllShoppingLists(user);
        var responses = lists.stream()
            .map(ShoppingListResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(new ApiResponse<>(responses));
    }

    @DeleteMapping("/shopping_list")
    public ResponseEntity<ApiResponse<Void>> deleteShoppingList(
            @AuthenticationPrincipal Jwt jwtUser,
            @RequestBody @Valid ShoppingListDeleteRequest request) {
        User user = getUserFromJwtUser(jwtUser);
        shoppingListService.deleteShoppingList(user, request.getShoppingListId());
        return ResponseEntity.ok(new ApiResponse<>(null));
    }

    @PostMapping("/best_buy")
    public ResponseEntity<ApiResponse<BestBuyResponse>> getBestBuy(
            @AuthenticationPrincipal Jwt jwtUser,
            @RequestBody @Valid BestBuyRequest request) {
        User user = getUserFromJwtUser(jwtUser);
        BestBuyResponse bestBuyList = shoppingListService.getBestBuy(user, request.getProductName());
        
        return ResponseEntity.ok(new ApiResponse<>(bestBuyList));
    }

    @PostMapping("product_stats")
    public ResponseEntity<ApiResponse<ProductStatsResponse>> getProductStats(
            @AuthenticationPrincipal Jwt jwtUser,
            @RequestBody @Valid ProductStatsRequest request) {
        User user = getUserFromJwtUser(jwtUser);
        ProductStatsResponse productStats = shoppingListService.getProductStats(
            user, request.getProductId(), request.getStoreId());
        
        return ResponseEntity.ok(new ApiResponse<>(productStats));
    }
}
