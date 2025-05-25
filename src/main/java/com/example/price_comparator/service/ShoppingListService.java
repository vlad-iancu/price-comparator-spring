package com.example.price_comparator.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

import com.example.price_comparator.dto.BestBuyResponse;
import com.example.price_comparator.dto.ProductStatsResponse;
import com.example.price_comparator.dto.ShoppingListResponse.ProductResponse;
import com.example.price_comparator.entity.Discount;
import com.example.price_comparator.entity.Product;
import com.example.price_comparator.entity.ProductStore;
import com.example.price_comparator.entity.ShoppingList;
import com.example.price_comparator.entity.User;
import com.example.price_comparator.exception.types.ShoppingListEmptyException;
import com.example.price_comparator.exception.types.ShoppingListNotFoundException;
import com.example.price_comparator.exception.types.UnauthorizedShoppingListException;
import com.example.price_comparator.repository.DiscountRepository;
import com.example.price_comparator.repository.ProductRepository;
import com.example.price_comparator.repository.ProductStoreRepository;
import com.example.price_comparator.repository.ShoppingListRepository;
import com.example.price_comparator.repository.StoreRepository;
import com.example.price_comparator.exception.types.ProductAlreadyExistsException;
import com.example.price_comparator.exception.types.ProductNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShoppingListService {
    private final ProductRepository productRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final StoreRepository storeRepository;
    private final ProductStoreRepository productStoreRepository;
    private final DiscountRepository discountRepository;
    // This service would typically interact with a repository to manage shopping
    // lists.
    // For simplicity, we are not implementing the actual logic here.

    public ShoppingList getShoppingList(User user, Long shoppingListId) {
        // Logic to retrieve a shopping list for the user by its ID

        return shoppingListRepository.findById(shoppingListId)
                .map(list -> {
                    if (!list.getUser().getId().equals(user.getId())) {
                        throw new UnauthorizedShoppingListException("Shopping list does not belong to user");
                    }
                    return list;
                })
                .orElseThrow(() -> new ShoppingListNotFoundException("Shopping list not found"));
    }

    public ShoppingList createShoppingList(User user, List<Long> productIds, String name) {
        if (productIds == null || productIds.isEmpty()) {
            throw new ShoppingListEmptyException("Product IDs cannot be null or empty");
        }
        List<Product> products = productRepository.findAllById(productIds);
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setUser(user);
        shoppingList.setProducts(new HashSet<>(products));
        shoppingList.setName(name);
        shoppingList.setCreatedAt(java.time.LocalDate.now());
        return shoppingListRepository.save(shoppingList);
    }

    public void deleteShoppingList(User user, Long shoppingListId) {
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId)
                .orElseThrow(() -> new ShoppingListNotFoundException("Shopping list not found"));
        if (!shoppingList.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedShoppingListException("Shopping list does not belong to user");
        }
        shoppingListRepository.delete(shoppingList);
    }

    public List<ShoppingList> getAllShoppingLists(User user) {
        // Logic to retrieve all shopping lists
        return shoppingListRepository.findAllByUserId(user.getId());
    }

    public ShoppingList addProductToShoppingList(User user, Long shoppingListId, Long productId) {
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId)
                .orElseThrow(() -> new ShoppingListNotFoundException("Shopping list not found"));
        if (!shoppingList.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedShoppingListException("Shopping list does not belong to user");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        if (shoppingList.getProducts().contains(product)) {
            // Optionally, you can throw an exception or simply return if product already
            // exists
            throw new ProductAlreadyExistsException("Product already exists in the shopping list");
        }
        shoppingList.getProducts().add(product);
        return shoppingListRepository.save(shoppingList);

    }

    public ShoppingList removeProductFromShoppingList(User user, Long shoppingListId, Long productId) {
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId)
                .orElseThrow(() -> new ShoppingListNotFoundException("Shopping list not found"));
        if (!shoppingList.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedShoppingListException("Shopping list does not belong to user");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        boolean hadElement = shoppingList.getProducts().remove(product);
        if (!hadElement) {
            throw new ProductNotFoundException("Product not found in the shopping list");
        }
        return shoppingListRepository.save(shoppingList);
    }

    public List<Product> getProductsInShoppingList(User user, Long shoppingListId) {
        // Logic to retrieve products in a specific shopping list
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId)
                .orElseThrow(() -> new ShoppingListNotFoundException("Shopping list not found"));
        if (!shoppingList.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedShoppingListException("Shopping list does not belong to user");
        }
        if (shoppingList.getProducts() == null || shoppingList.getProducts().isEmpty()) {
            throw new ShoppingListEmptyException("Shopping list is empty");
        }
        return new ArrayList<>(shoppingList.getProducts());
    }

    public BestBuyResponse getBestBuy(User user, String productName) {
        List<Product> products = productRepository.findByProductName(productName);

        AtomicReference<BestBuyResponse> bestBuyResponse = new java.util.concurrent.atomic.AtomicReference<>(null);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }

        products.stream()
                .filter(product -> product.getProductStores() != null && !product.getProductStores().isEmpty())
                .forEach(product -> {
                    var bestStore = productStoreRepository.findLatestProductStoresByProductId(product.getId()).stream()
                            .min((store1, store2) -> {
                                Discount discount1 = discountRepository.findActiveDiscount(product.getId(),
                                        store1.getStore().getId(), java.time.LocalDate.now()).orElse(null);
                                Discount discount2 = discountRepository.findActiveDiscount(product.getId(),
                                        store2.getStore().getId(), java.time.LocalDate.now()).orElse(null);
                                Double price1 = store1.getPrice();
                                Double price2 = store2.getPrice();
                                if (discount1 != null) {
                                    price1 -= price1 * (discount1.getDiscountPercentage() / 100);
                                }
                                if (discount2 != null) {
                                    price2 -= price2 * (discount2.getDiscountPercentage() / 100);
                                }
                                return Double.compare(getPricePerUnit(store1), getPricePerUnit(store2));
                            })
                            .orElseThrow(() -> new ProductNotFoundException("No stores found for product"));

                    if (bestBuyResponse.get() == null || bestStore.getPrice() < bestBuyResponse.get().getPrice()) {
                        Discount discount = discountRepository.findActiveDiscount(product.getId(),
                                bestStore.getStore().getId(), java.time.LocalDate.now()).orElse(null);
                        if (discount != null) {
                            bestStore.setPrice(bestStore.getPrice() - bestStore.getPrice()
                                    * (discount.getDiscountPercentage() / 100));
                        }
                        bestBuyResponse.set(new BestBuyResponse(
                                product.getProductName(),
                                bestStore.getStore().getName(),
                                product.getBrand(),
                                bestStore.getPrice()));
                    }
                });

        if (bestBuyResponse.get() == null) {
            throw new ProductNotFoundException("No suitable store found for product");
        }
        return bestBuyResponse.get();
    }

    public Double getPricePerUnit(ProductStore productStore) {
        String unit = productStore.getProduct().getPackageUnit().toLowerCase();
        double quantity = productStore.getPackageQuantity();
        double price = productStore.getPrice();

        if (unit.equals("ml")) {
            quantity = quantity / 1000.0; // convert ml to liters
            unit = "l";
        } else if (unit.equals("grams") || unit.equals("g")) {
            quantity = quantity / 1000.0; // convert grams to kilograms
            unit = "kg";
        }
        // If not a mass or volume unit, assume per piece (no conversion)

        if (quantity == 0) {
            throw new IllegalArgumentException("Quantity cannot be zero");
        }

        return price / quantity;
    }

    public ProductStatsResponse getProductStats(User user, Long productId, Long storeId) {
        // TODO Auto-generated method stub
        List<ProductStore> productStores = productStoreRepository.findByProductIdOrderedByUpdatedAt(productId, storeId);
        if (productStores.isEmpty()) {
            throw new ProductNotFoundException("No stores found for product");
        }
        ProductStatsResponse productStatsResponse = new ProductStatsResponse();
        productStatsResponse.setProductId(productId);
        productStatsResponse.setProductName(productStores.get(0).getProduct().getProductName());
        List<ProductStatsResponse.DataPoint> dataPoints = new ArrayList<>();
        productStores.forEach(ps -> {
            ProductStatsResponse.DataPoint dataPoint = productStatsResponse.new DataPoint();
            dataPoint.setDate(ps.getId().getUpdatedAt());
            dataPoint.setPrice(ps.getPrice());
            dataPoints.add(dataPoint);
        });
        productStatsResponse.setDataPoints(dataPoints);
        // You can now use productStores as needed, e.g., build and return a ProductStatsResponse
        return productStatsResponse;
    }
}
