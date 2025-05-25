package com.example.price_comparator.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.price_comparator.dto.PriceAlertCreateRequest;
import com.example.price_comparator.dto.PriceAlertResponse;
import com.example.price_comparator.entity.User;
import org.springframework.stereotype.Service;

import com.example.price_comparator.entity.Discount;
import com.example.price_comparator.entity.PriceAlert;
import com.example.price_comparator.entity.Product;
import com.example.price_comparator.exception.types.PriceAlertConflictException;
import com.example.price_comparator.exception.types.PriceAlertNotFoundException;
import com.example.price_comparator.exception.types.UnauthorizedPriceAlertException;
import com.example.price_comparator.repository.DiscountRepository;
import com.example.price_comparator.repository.PriceAlertRepository;
import com.example.price_comparator.repository.ProductRepository;

import java.util.stream.Collectors;

@Service
public class PriceAlertService {

    private final PriceAlertRepository priceAlertRepository;
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;

    public PriceAlertService(
            PriceAlertRepository priceAlertRepository,
            ProductRepository productRepository,
            DiscountRepository discountRepository) {
        this.priceAlertRepository = priceAlertRepository;
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
    }

    public PriceAlertResponse getPriceAlertById(User user, Long id) {
        PriceAlert alert = priceAlertRepository.findById(id)
                .orElseThrow(() -> new PriceAlertNotFoundException("Price alert not found"));
        if (!alert.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedPriceAlertException("Unauthorized access to price alert");
        }
        return PriceAlertResponse.fromEntity(alert);
    }

    public List<PriceAlertResponse> getAllPriceAlerts(User user) {
        List<PriceAlert> alerts = priceAlertRepository.findAllByUserId(user.getId());
        return alerts.stream()
                .map(PriceAlertResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public PriceAlertResponse createPriceAlert(User user, PriceAlertCreateRequest request) {
        PriceAlert alert = new PriceAlert();
        if (priceAlertRepository.existsByProductNameAndUserId(request.getProductName(), user.getId())) {
            throw new PriceAlertConflictException("Price alert for this product already exists");
        }
        alert.setUser(user);
        alert.setProductName(request.getProductName());
        alert.setTargetPrice(request.getTargetPrice());
        // set other fields as needed
        PriceAlert saved = priceAlertRepository.save(alert);
        return PriceAlertResponse.fromEntity(saved);
    }

    public PriceAlertResponse updatePriceAlert(User user, Long id, PriceAlertCreateRequest request) {
        PriceAlert alert = priceAlertRepository.findById(id)
                .orElseThrow(() -> new PriceAlertNotFoundException("Price alert not found"));
        if (!alert.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedPriceAlertException("Unauthorized access to price alert");
        }
        if (priceAlertRepository.existsByProductNameAndUserId(request.getProductName(), user.getId())) {
            throw new PriceAlertConflictException("Price alert for this product already exists");
        }
        alert.setProductName(request.getProductName());
        alert.setTargetPrice(request.getTargetPrice());
        // update other fields as needed
        PriceAlert updated = priceAlertRepository.save(alert);
        return PriceAlertResponse.fromEntity(updated);
    }

    public void deletePriceAlert(User user, Long id) {
        PriceAlert alert = priceAlertRepository.findById(id)
                .orElseThrow(() -> new PriceAlertNotFoundException("Price alert not found"));
        if (!alert.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedPriceAlertException("Unauthorized access to price alert");
        }
        priceAlertRepository.delete(alert);
    }

    public List<PriceAlert> getTriggeredPriceAlerts(User user) {
        // Retrieve all price alerts
        List<PriceAlert> alerts = priceAlertRepository.findAllByUserId(user.getId());
        
        ArrayList<PriceAlert> triggeredAlerts = new ArrayList<>();
        for (PriceAlert alert : alerts) {
            // Check if the alert belongs to the user
            List<Product> targetedProducts = productRepository.findByProductName(alert.getProductName());
            if (!targetedProducts.isEmpty()) {
                // Find the minimum price for the product, considering discounts
                double minPrice = targetedProducts.stream()
                    .mapToDouble(product -> {
                        // Get all stores for this product
                        return product.getProductStores().stream()
                            .mapToDouble(productStore -> {
                                double price = productStore.getPrice();
                                // Apply discount if available
                                Discount discount = discountRepository
                                .findActiveDiscount(productStore.getId().getProduct(), productStore.getId().getStore(), LocalDate.now())
                                    .orElse(null);
                                if (discount != null) {
                                    price = price - (price * discount.getDiscountPercentage() / 100.0);
                                }
                                return price;
                            })
                            .min()
                            .orElse(Double.MAX_VALUE);
                    })
                    .min()
                    .orElse(Double.MAX_VALUE);

                // If the minimum price is less than or equal to the target price, add to triggered
                if (minPrice <= alert.getTargetPrice()) {
                    triggeredAlerts.add(alert);
                }
            }
        }
        

        return triggeredAlerts;
    }
}
