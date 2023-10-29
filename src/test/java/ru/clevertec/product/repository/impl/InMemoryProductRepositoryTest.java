package ru.clevertec.product.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.clevertec.product.util.TestConstant.PRODUCT_INCORRECT_UUID;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.util.ProductTestBuilder;

@ExtendWith(MockitoExtension.class)
class InMemoryProductRepositoryTest {

    @InjectMocks
    private InMemoryProductRepository inMemoryProductRepository;

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
    void deleteShouldNotReturnError_whenCalled() {
        UUID uuid = ProductTestBuilder.builder().build()
                .buildProduct()
                .getUuid();

        assertDoesNotThrow(() -> inMemoryProductRepository.delete(uuid));
    }
}
