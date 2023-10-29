package ru.clevertec.product.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.service.ProductService;

@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;
    private final ProductRepository productRepository;

    @Override
    public InfoProductDto get(UUID uuid) {
        Optional<Product> productOptional = productRepository.findById(uuid);
        Product product = productOptional.orElseThrow(() -> new ProductNotFoundException(uuid));
        return mapper.toInfoProductDto(product);
    }

    @Override
    public List<InfoProductDto> getAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(mapper::toInfoProductDto)
                .toList();
    }

    @Override
    public UUID create(ProductDto productDto) {
        Product product = mapper.toProduct(productDto);
        Product saved = productRepository.save(product);
        return saved.getUuid();
    }

    @Override
    public void update(UUID uuid, ProductDto productDto) {
        Optional<Product> productOptional = productRepository.findById(uuid);
        productOptional.map(product -> {
            Product updatedProduct = mapper.merge(product, productDto);
            productRepository.save(updatedProduct);
            return product;
        }).orElseThrow(() -> new ProductNotFoundException(uuid));
    }

    @Override
    public void delete(UUID uuid) {
        productRepository.delete(uuid);
    }
}
