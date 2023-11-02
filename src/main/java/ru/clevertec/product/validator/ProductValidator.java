package ru.clevertec.product.validator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ValidationException;

public class ProductValidator {

    public void validate(Product product) {
        List<String> validateErrors = new ArrayList<>();
        if (product.getName() == null) {
            validateErrors.add("null product name");
        }
        if (product.getName() != null) {
            if (product.getName().trim().isEmpty()) {
                validateErrors.add("empty product name");
            }
            if (!product.getName().matches("^[а-яА-Я\\s]{5,10}$")) {
                validateErrors.add("incorrect product name");
            }
        }
        if (product.getDescription() != null && !product.getDescription().matches("^[а-яА-Я\\s]{10,30}$")) {
            validateErrors.add("incorrect product description");
        }
        if (product.getPrice() == null) {
            validateErrors.add("null product price");
        } else {
            if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                validateErrors.add("product price less or equal than 0");
            }
        }
        if (product.getCreated() == null) {
            validateErrors.add("null product created time");
        }
        if (!validateErrors.isEmpty()) {
            throw new ValidationException(validateErrors);
        }
    }
}
