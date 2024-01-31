package ru.clevertec.product.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.clevertec.product.util.TestConstant.INVALID_TEXT_VALUE;
import static ru.clevertec.product.util.TestConstant.PRODUCT_INCORRECT_UUID;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ValidationException;
import ru.clevertec.product.util.ProductTestBuilder;

class InMemoryProductRepositoryTest {

    private final InMemoryProductRepository inMemoryProductRepository = new InMemoryProductRepository();

    @Test
    void findByIdShouldReturnNotEmptyOptional_whenCorrectUuid() {
        UUID uuid = PRODUCT_INCORRECT_UUID;

        Optional<Product> actual = inMemoryProductRepository.findById(uuid);

        assertNotNull(actual);
        assertFalse(actual.isPresent());
    }

    @Test
    void findAllShouldReturnList_whenCalled() {
        List<Product> actual = inMemoryProductRepository.findAll();

        assertEquals(0, actual.size());
    }

    @Nested
    class SaveTest{

        @Test
        void saveShouldReturnProductWithUuidAndCreated_whenCalled() {
            Product expected = ProductTestBuilder.builder()
                    .withUuid(null).build()
                    .buildProduct();

            Product actual = inMemoryProductRepository.save(expected);

            assertThat(actual)
                    .hasNoNullFieldsOrPropertiesExcept(Product.Fields.uuid)
                    .hasFieldOrPropertyWithValue(Product.Fields.name, expected.getName())
                    .hasFieldOrPropertyWithValue(Product.Fields.description, expected.getDescription())
                    .hasFieldOrPropertyWithValue(Product.Fields.price, expected.getPrice())
                    .hasNoNullFieldsOrPropertiesExcept(Product.Fields.created);

        }

        @Test
        void saveShouldReturnValidatedException_whenProductNameIsNull() {
            Product product = ProductTestBuilder.builder()
                    .withUuid(null)
                    .withName(null).build()
                    .buildProduct();

            assertThrows(ValidationException.class, () -> inMemoryProductRepository.save(product));
        }

        @Test
        void saveShouldReturnValidatedException_whenInvalidProductName() {
            Product product = ProductTestBuilder.builder()
                    .withUuid(null)
                    .withName(INVALID_TEXT_VALUE).build()
                    .buildProduct();

            assertThrows(ValidationException.class, () -> inMemoryProductRepository.save(product));
        }

        @Test
        void saveShouldReturnValidatedException_whenInvalidProductDescription() {
            Product product = ProductTestBuilder.builder()
                    .withUuid(null)
                    .withDescription(INVALID_TEXT_VALUE).build()
                    .buildProduct();

            assertThrows(ValidationException.class, () -> inMemoryProductRepository.save(product));
        }

        @Test
        void saveShouldReturnValidatedException_whenInvalidProductPrice() {
            Product product = ProductTestBuilder.builder()
                    .withUuid(null)
                    .withPrice(BigDecimal.ZERO).build()
                    .buildProduct();

            assertThrows(ValidationException.class, () -> inMemoryProductRepository.save(product));
        }

        @Test
        void saveShouldReturnValidatedException_whenProductPriceIsNull() {
            Product product = ProductTestBuilder.builder()
                    .withUuid(null)
                    .withPrice(null).build()
                    .buildProduct();

            assertThrows(ValidationException.class, () -> inMemoryProductRepository.save(product));
        }

        @Test
        void saveShouldReturnProductWithNotUpdatedUuid_whenUpdatedProductHasUuid() {
            Product expected = ProductTestBuilder.builder()
                    .withCreated(null).build()
                    .buildProduct();

            Product actual = inMemoryProductRepository.save(expected);

            assertThat(actual)
                    .hasFieldOrPropertyWithValue(Product.Fields.uuid, expected.getUuid())
                    .hasFieldOrPropertyWithValue(Product.Fields.name, expected.getName())
                    .hasFieldOrPropertyWithValue(Product.Fields.description, expected.getDescription())
                    .hasFieldOrPropertyWithValue(Product.Fields.price, expected.getPrice())
                    .hasNoNullFieldsOrPropertiesExcept(Product.Fields.created);
        }

        @Test
        void saveShouldReturnProductWithNotUpdatedCreated_whenUpdatedProductHasCreated() {
            Product expected = ProductTestBuilder.builder().build()
                    .buildProduct();

            Product actual = inMemoryProductRepository.save(expected);

            assertThat(actual)
                    .hasFieldOrPropertyWithValue(Product.Fields.uuid, expected.getUuid())
                    .hasFieldOrPropertyWithValue(Product.Fields.name, expected.getName())
                    .hasFieldOrPropertyWithValue(Product.Fields.description, expected.getDescription())
                    .hasFieldOrPropertyWithValue(Product.Fields.price, expected.getPrice())
                    .hasFieldOrPropertyWithValue(Product.Fields.created, expected.getCreated());
        }
    }

    @Test
    void deleteShouldNotReturnError_whenCalled() {
        UUID uuid = ProductTestBuilder.builder().build()
                .buildProduct()
                .getUuid();

        assertDoesNotThrow(() -> inMemoryProductRepository.delete(uuid));
    }
}
