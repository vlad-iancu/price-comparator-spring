package com.example.price_comparator.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.price_comparator.entity.Discount;
import com.example.price_comparator.entity.Product;
import com.example.price_comparator.entity.ProductStore;
import com.example.price_comparator.entity.Store;
import com.example.price_comparator.entity.User;
import com.example.price_comparator.entity.ProductStoreId;
import com.example.price_comparator.entity.ShoppingList;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.price_comparator.entity.DiscountId;

@Component
@Profile("dev") // Run only in the 'dev' profile
public class DataInitializer {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;
    private final ProductStoreRepository productStoreRepository;
    private final DiscountRepository discountRepository;
    private final ShoppingListRepository shoppingListRepository;

    public DataInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            StoreRepository storeRepository,
            ProductRepository productRepository,
            ProductStoreRepository productStoreRepository,
            DiscountRepository discountRepository,
            ShoppingListRepository shoppingListRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.productStoreRepository = productStoreRepository;
        this.discountRepository = discountRepository;
        this.shoppingListRepository = shoppingListRepository;
    }

    public void seedUsers() {
        if (userRepository.count() == 0) {
            userRepository.save(new User("alice", passwordEncoder.encode("passwordAlice")));
            userRepository.save(new User("bob", passwordEncoder.encode("passwordBob")));
            userRepository.save(new User("charlie", passwordEncoder.encode("passwordCharlie")));
        }
    }

    public void seedStores() {
        try (Stream<Path> paths = Files.walk(Paths.get("src/main/resources/data"))) {
            Pattern pattern = Pattern.compile("([a-zA-Z0-9]+)_\\d{4}-\\d{2}-\\d{2}\\.csv");
            String[] storeNames = paths
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(name -> {
                        Matcher matcher = pattern.matcher(name);
                        return matcher.matches() ? matcher.group(1) : null;
                    })
                    .filter(storeName -> storeName != null)
                    .distinct()
                    .toArray(String[]::new);
            for (String storeName : storeNames) {
                Store store = new Store(storeName);
                System.out.println("Seeding store: " + storeName);
                storeRepository.save(store);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void seedProducts(String csvPath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String header = br.readLine(); // skip header
            if (header == null) {
                return;
            }

            // Extract store name from file name
            String fileName = csvPath.substring(csvPath.lastIndexOf('/') + 1);
            Pattern pattern = Pattern.compile("([a-zA-Z0-9]+)_(\\d{4}-\\d{2}-\\d{2})\\.csv");
            Matcher matcher = pattern.matcher(fileName);
            String storeName = null;
            String dateString = null;
            if (matcher.matches()) {
                storeName = matcher.group(1);
                dateString = matcher.group(2);
            } else {
                throw new RuntimeException("Could not extract store name and date from file: " + fileName);
            }

            Optional<Store> dbstore = storeRepository.findFirstByName(storeName);
            if (dbstore.isEmpty()) {
                throw new RuntimeException("Store not found: " + storeName);
            }

            Store store = dbstore.get();
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 8) {
                    continue;
                }

                Long productId = Long.valueOf(fields[0].substring(1));
                String productName = fields[1];
                String productCategory = fields[2];
                String brand = fields[3];
                String packageQuantity = fields[4];
                String packageUnit = fields[5];
                String price = fields[6];

                // Check if product exists
                Product product = productRepository.findById(productId).orElse(null);
                if (product == null) {
                    product = new Product();
                    product.setId(productId);
                    product.setProductName(productName);
                    product.setPackageUnit(packageUnit);
                    product.setProductCategory(productCategory);
                    product.setBrand(brand);
                    productRepository.save(product);
                }

                // Insert into product_store
                ProductStore productStore = new ProductStore();
                LocalDate date = LocalDate.parse(dateString);
                productStore.setId(new ProductStoreId(product.getId(), store.getId(), date));
                productStore.setProduct(product);
                productStore.setStore(store);
                productStore.setPackageQuantity(Double.valueOf(packageQuantity));
                productStore.setPrice(Double.valueOf(price));
                // System.out.println("Seeding product: " + product.getProductName() + " in
                // store: " + store.getName() + " product id: " + product.getId().toString());
                productStoreRepository.save(productStore);}
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void seedDiscounts(String csvPath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String header = br.readLine(); // skip header
            if (header == null) {
                return;
            }

            // Extract store name from file name using regex
            String fileName = csvPath.substring(csvPath.lastIndexOf('/') + 1);
            Pattern pattern = Pattern.compile("([a-zA-Z0-9]+)_discounts_\\d{4}-\\d{2}-\\d{2}\\.csv");
            Matcher matcher = pattern.matcher(fileName);
            String storeName = null;
            if (matcher.matches()) {
                storeName = matcher.group(1);
            } else {
                throw new RuntimeException("Could not extract store name from file: " + fileName);
            }

            Optional<Store> dbstore = storeRepository.findFirstByName(storeName);
            if (dbstore.isEmpty()) {
                throw new RuntimeException("Store not found: " + storeName);
            }

            Store store = dbstore.get();
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 9) {
                    continue;
                }

                // CSV columns:
                // product_id,product_name,brand,package_quantity,package_unit,product_category,from_date,to_date,percentage_of_discount
                String productIdStr = fields[0].substring(1); // Remove leading quote
                // String productName = fields[1];
                // String brand = fields[2];
                String fromDate = fields[6];
                String toDate = fields[7];
                String percentage = fields[8];

                Long productId = Long.valueOf(productIdStr);
                // Find product by name and brand
                Optional<Product> prod = productRepository.findById(productId);
                if (prod.isEmpty()) {
                    continue;
                }

                Product product = prod.get();
                // System.out.println("Seeding discount for product: " +
                // product.getProductName() + " in store: " + store.getName() + " product id: "
                // + product.getId().toString());

                // Find product_store for this product and store
                // Optional<ProductStore> productStore =
                // productStoreRepository.findByProductAndStore(product, store);

                // Create Discount
                Discount discount = new Discount();
                DiscountId discountId = new DiscountId(product.getId(), store.getId());

                discount.setId(discountId);
                discount.setDiscountPercentage(Double.valueOf(percentage));
                discount.getId().setFromDate(java.time.LocalDate.parse(fromDate));
                discount.setToDate(java.time.LocalDate.parse(toDate));
                // discount.setProductStore(productStore.get());
                discount.setProduct(product);
                discount.setStore(store);
                discountRepository.save(discount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void seedShoppingLists() {
        // Implement seeding logic for shopping lists if needed

        // Example: Create a shopping list for each user
        Optional<User> userAlice = userRepository.findByUsername("alice");
        Optional<User> userBob = userRepository.findByUsername("bob");
        Optional<User> userCharlie = userRepository.findByUsername("charlie");

        if (userAlice.isPresent()) {
            // Create a shopping list for Alice
            HashSet<Product> products = new HashSet<>();
            products.add(
                productRepository.findById(1L).orElseThrow(() -> new RuntimeException("Product not found")
            ));
            products.add(
                productRepository.findById(70L).orElseThrow(() -> new RuntimeException("Product not found")
            ));
            products.add(
                productRepository.findById(33L).orElseThrow(() -> new RuntimeException("Product not found")
            ));
            ShoppingList shoppingList = new ShoppingList("Alice's List", userAlice.get(),
            LocalDate.now(), products);
            shoppingListRepository.save(shoppingList);
        }
        if (userBob.isPresent()) {
            HashSet<Product> products = new HashSet<>();
            products.add(
                productRepository.findById(10L).orElseThrow(() -> new RuntimeException("Product not found")
            ));
            products.add(
                productRepository.findById(30L).orElseThrow(() -> new RuntimeException("Product not found")
            ));
            products.add(
                productRepository.findById(55L).orElseThrow(() -> new RuntimeException("Product not found")
            ));
            ShoppingList shoppingList = new ShoppingList("Bob's List", userBob.get(),
            LocalDate.now(), products);
            shoppingListRepository.save(shoppingList);
        }
        if (userCharlie.isPresent()) {
            HashSet<Product> products = new HashSet<>();
            products.add(
                productRepository.findById(24L).orElseThrow(() -> new RuntimeException("Product not found")
            ));
            products.add(
                productRepository.findById(26L).orElseThrow(() -> new RuntimeException("Product not found")
            ));
            products.add(
                productRepository.findById(54L).orElseThrow(() -> new RuntimeException("Product not found")
            ));
            ShoppingList shoppingList = new ShoppingList("Charlie's List", userCharlie.get(),
            LocalDate.now(), products);
            shoppingListRepository.save(shoppingList);
        }
    }

    @PostConstruct
    public void seedDatabase() {
        // Call the method to seed users
        seedUsers();
        seedStores();
        // Example usage:
        try (Stream<Path> paths = Files.walk(Paths.get("src/main/resources/data"))) {
            Pattern productPattern = Pattern.compile("([a-zA-Z0-9]+)_(\\d{4}-\\d{2}-\\d{2})\\.csv");
            Pattern discountPattern = Pattern.compile("([a-zA-Z0-9]+)_discounts_(\\d{4}-\\d{2}-\\d{2})\\.csv");

            // Collect product and discount file paths separately
            java.util.List<String> productFiles = new java.util.ArrayList<>();
            java.util.List<String> discountFiles = new java.util.ArrayList<>();

            paths.filter(Files::isRegularFile)
                    .map(Path::toString)
                    .forEach(filePath -> {
                        String fileName = Paths.get(filePath).getFileName().toString();
                        if (productPattern.matcher(fileName).matches()) {
                            productFiles.add(filePath);
                        } else if (discountPattern.matcher(fileName).matches()) {
                            discountFiles.add(filePath);
                        }
                    });

            productFiles.sort(String::compareTo);
            discountFiles.sort(String::compareTo);
            // Seed products first
            for (String productFile : productFiles) {
                System.out.println("Seeding products from file: " + productFile);
                seedProducts(productFile);
            }
            // Then seed discounts
            for (String discountFile : discountFiles) {
                System.out.println("Seeding discounts from file: " + discountFile);
                seedDiscounts(discountFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // seedProducts("src/main/resources/data/kaufland_2025-05-01.csv");
        // seedDiscounts("src/main/resources/data/kaufland_discounts_2025-05-01.csv");
        seedShoppingLists();
    }
}
