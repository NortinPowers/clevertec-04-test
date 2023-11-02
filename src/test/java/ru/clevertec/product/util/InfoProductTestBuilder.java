package ru.clevertec.product.util;

import static ru.clevertec.product.util.TestConstant.PRODUCT_DESCRIPTION;
import static ru.clevertec.product.util.TestConstant.PRODUCT_NAME;
import static ru.clevertec.product.util.TestConstant.PRODUCT_PRICE;
import static ru.clevertec.product.util.TestConstant.PRODUCT_UUID;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import ru.clevertec.product.data.InfoProductDto;

@Data
@Builder(setterPrefix = "with")
public class InfoProductTestBuilder {

    @Builder.Default
    private UUID uuid = PRODUCT_UUID;
    @Builder.Default
    private String name = PRODUCT_NAME;
    @Builder.Default
    private String description = PRODUCT_DESCRIPTION;
    @Builder.Default
    private BigDecimal price = PRODUCT_PRICE;

    public InfoProductDto buildInfoProductDto() {
        return new InfoProductDto(uuid, name, description, price);
    }
}
