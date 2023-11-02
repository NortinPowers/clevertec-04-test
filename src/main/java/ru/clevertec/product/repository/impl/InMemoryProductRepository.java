package ru.clevertec.product.repository.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.validator.ProductValidator;

public class InMemoryProductRepository implements ProductRepository {

    private final List<Product> products = new CopyOnWriteArrayList<>();
    private final ProductValidator productValidator = new ProductValidator();

    @Override
    public Optional<Product> findById(UUID uuid) {
        return products.stream()
                .filter(product -> product.getUuid().equals(uuid))
                .findAny();
    }

    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public Product save(Product product) {
        if (product.getUuid() == null) {
            product.setUuid(UUID.randomUUID()); //заглушка, пока не подключена БД
        }
        if (product.getCreated() == null) {
            product.setCreated(LocalDateTime.now());
        }
        productValidator.validate(product);
        products.add(product);
        return product;
    }

    @Override
    public void delete(UUID uuid) {
        products.removeIf(product -> product.getUuid().equals(uuid));
    }
}
