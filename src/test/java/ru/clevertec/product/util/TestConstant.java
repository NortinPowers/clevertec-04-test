package ru.clevertec.product.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestConstant {

    public static final UUID PRODUCT_UUID = UUID.fromString("76dbb74c-2f08-4bc0-8029-aed02147e737");
    public static final UUID PRODUCT_INCORRECT_UUID = UUID.fromString("76dbb74c-2f08-4bc0-8029-aed02147e738");
    public static final String PRODUCT_NAME = "Plumbus";
    public static final String NEW_PRODUCT_NAME = "Portal Gun";
    public static final String PRODUCT_DESCRIPTION = "all-purpose home device";
    public static final String NEW_PRODUCT_DESCRIPTION = "gadget that allows the user(s) to travel between different universes/dimensions/realities";
    public static final BigDecimal PRODUCT_PRICE = BigDecimal.TEN;
    public static final BigDecimal NEW_PRODUCT_PRICE = BigDecimal.valueOf(11);
    public static final LocalDateTime PRODUCT_CREATED_DATE = LocalDateTime.of(2023, 10, 28, 11, 17, 0);
}
