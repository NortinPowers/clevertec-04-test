package ru.clevertec.product.util;

import static ru.clevertec.product.util.TestConstant.PRODUCT_CREATED_DATE;
import static ru.clevertec.product.util.TestConstant.PRODUCT_DESCRIPTION;
import static ru.clevertec.product.util.TestConstant.PRODUCT_NAME;
import static ru.clevertec.product.util.TestConstant.PRODUCT_PRICE;
import static ru.clevertec.product.util.TestConstant.PRODUCT_UUID;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;

@Builder(setterPrefix = "with")
@Data
public class ProductTestBuilder {

    @Builder.Default
    private UUID uuid = PRODUCT_UUID;
    @Builder.Default
    private String name = PRODUCT_NAME;
    @Builder.Default
    private String description = PRODUCT_DESCRIPTION;
    @Builder.Default
    private BigDecimal price = PRODUCT_PRICE;
    @Builder.Default
    private LocalDateTime created = PRODUCT_CREATED_DATE;

    public Product buildProduct() {
        return new Product(uuid, name, description, price, created);
    }

    public ProductDto buildProductDto() {
        return new ProductDto(name, description, price);
    }
}
