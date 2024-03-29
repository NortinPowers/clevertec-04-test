package ru.clevertec.product.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.product.util.TestConstant.NEW_PRODUCT_DESCRIPTION;
import static ru.clevertec.product.util.TestConstant.NEW_PRODUCT_NAME;
import static ru.clevertec.product.util.TestConstant.NEW_PRODUCT_PRICE;
import static ru.clevertec.product.util.TestConstant.PRODUCT_INCORRECT_UUID;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.util.InfoProductTestBuilder;
import ru.clevertec.product.util.ProductTestBuilder;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductMapper mapper;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImpl productService;
    @Captor
    private ArgumentCaptor<Product> captor;

    @Nested
    class GetTest {

        @Test
        void getShouldReturnInfoProductDto_whenCorrectUuid() {
            InfoProductDto expected = InfoProductTestBuilder.builder().build()
                    .buildInfoProductDto();
            Product product = ProductTestBuilder.builder().build()
                    .buildProduct();
            Optional<Product> optionalProduct = Optional.of(product);

            when(productRepository.findById(product.getUuid()))
                    .thenReturn(optionalProduct);
            when(mapper.toInfoProductDto(optionalProduct.get()))
                    .thenReturn(expected);

            InfoProductDto actual = productService.get(product.getUuid());

            assertThat(actual)
                    .hasFieldOrPropertyWithValue(Product.Fields.uuid, expected.uuid())
                    .hasFieldOrPropertyWithValue(Product.Fields.name, expected.name())
                    .hasFieldOrPropertyWithValue(Product.Fields.description, expected.description())
                    .hasFieldOrPropertyWithValue(Product.Fields.price, expected.price());
        }

        @Test
        void getShouldReturnProductNotFoundException_whenIncorrectUuid() {
            Product product = ProductTestBuilder.builder()
                    .withUuid(null)
                    .build()
                    .buildProduct();
            Optional<Product> empty = Optional.empty();

            when(productRepository.findById(product.getUuid()))
                    .thenReturn(empty);

            assertThrows(ProductNotFoundException.class, () -> productService.get(product.getUuid()));
        }
    }

    @Nested
    class GetAllTest {

        @Test
        void getAllShouldReturnInfoProductDtoList_whenProductsListIsNotEmpty() {
            InfoProductDto infoProductDto = InfoProductTestBuilder.builder().build()
                    .buildInfoProductDto();
            List<InfoProductDto> expected = List.of(infoProductDto);
            Product product = ProductTestBuilder.builder().build()
                    .buildProduct();
            List<Product> products = List.of(product);

            when(productRepository.findAll())
                    .thenReturn(products);
            when(mapper.toInfoProductDto(product))
                    .thenReturn(infoProductDto);

            List<InfoProductDto> actual = productService.getAll();

            assertThat(actual)
                    .hasSize(expected.size())
                    .isEqualTo(expected);
            verify(mapper, atLeastOnce()).toInfoProductDto(any(Product.class));
        }

        @Test
        void getAllShouldReturnEmptyList_whenProductsListIsEmpty() {
            List<InfoProductDto> expected = List.of();
            List<Product> products = List.of();

            when(productRepository.findAll())
                    .thenReturn(products);

            List<InfoProductDto> actual = productService.getAll();

            assertThat(actual)
                    .hasSize(expected.size())
                    .containsAll(expected);
            verify(mapper, never()).toInfoProductDto(any(Product.class));
        }
    }

    @Nested
    class CreateTest {

        @Test
        void createShouldReturnUuid_whenProductDtoIsCorrect() {
            ProductDto productDto = ProductTestBuilder.builder()
                    .build()
                    .buildProductDto();
            Product product = ProductTestBuilder.builder()
                    .withUuid(null).build()
                    .buildProduct();
            UUID generetedUuid = UUID.fromString("d3d75177-f087-4c70-ae30-05d947733c4e");
            Product createdProduct = ProductTestBuilder.builder()
                    .withUuid(generetedUuid).build()
                    .buildProduct();

            when(mapper.toProduct(productDto))
                    .thenReturn(product);
            when(productRepository.save(product))
                    .thenReturn(createdProduct);

            UUID actual = productService.create(productDto);

            assertEquals(createdProduct.getUuid(), actual);
        }

        @Test
        void createShouldSetProductUuid_whenInvokeRepository() {
            ProductDto productDto = ProductTestBuilder.builder()
                    .build()
                    .buildProductDto();
            Product productToSave = ProductTestBuilder.builder()
                    .withUuid(null).build()
                    .buildProduct();
            Product createdProduct = ProductTestBuilder.builder()
                    .build()
                    .buildProduct();

            when(mapper.toProduct(productDto))
                    .thenReturn(productToSave);
            when(productRepository.save(productToSave))
                    .thenReturn(createdProduct);

            productService.create(productDto);

            verify(productRepository, atLeastOnce()).save(captor.capture());
            assertThat(captor.getValue())
                    .hasFieldOrPropertyWithValue(Product.Fields.uuid, null);
        }

        @Test
        void createShouldLeaveProductUuid_whenProductDtoUuidExist() {
            ProductDto productDto = ProductTestBuilder.builder()
                    .build()
                    .buildProductDto();
            Product productToSave = ProductTestBuilder.builder()
                    .build()
                    .buildProduct();

            when(mapper.toProduct(productDto))
                    .thenReturn(productToSave);
            when(productRepository.save(productToSave))
                    .thenReturn(productToSave);

            productService.create(productDto);

            verify(productRepository, atLeastOnce()).save(captor.capture());
            assertThat(captor.getValue())
                    .hasFieldOrPropertyWithValue(Product.Fields.uuid, productToSave.getUuid());
        }

        @Test
        void createShouldReturnIllegalArgumentException_whenProductDtoIsNull() {
            ProductDto productDto = null;
            IllegalArgumentException exception = new IllegalArgumentException();

            when(productRepository.save(null))
                    .thenThrow(exception);

            assertThrows(IllegalArgumentException.class, () -> productService.create(productDto));
            verify(mapper, never()).toProduct(any(ProductDto.class));
            verify(productRepository, never()).save(any(Product.class));
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void updateShouldUpdateProduct_whenProductDtoAndUuidIsCorrect() {
            Product product = ProductTestBuilder.builder().build().buildProduct();
            UUID uuid = product.getUuid();
            Optional<Product> optionalProduct = Optional.of(product);
            ProductDto updatedProductDto = ProductTestBuilder.builder()
                    .withName(NEW_PRODUCT_NAME)
                    .withDescription(NEW_PRODUCT_DESCRIPTION)
                    .withPrice(NEW_PRODUCT_PRICE).build()
                    .buildProductDto();
            Product updatedProduct = ProductTestBuilder.builder()
                    .withUuid(uuid)
                    .withName(NEW_PRODUCT_NAME)
                    .withDescription(NEW_PRODUCT_DESCRIPTION)
                    .withPrice(NEW_PRODUCT_PRICE).build()
                    .buildProduct();

            when(productRepository.findById(uuid))
                    .thenReturn(optionalProduct);
            when(mapper.merge(product, updatedProductDto))
                    .thenReturn(updatedProduct);
            when(productRepository.save(updatedProduct))
                    .thenReturn(updatedProduct);

            productService.update(uuid, updatedProductDto);
        }

        @Test
        void updateShouldReturnProductNotFoundException_whenIncorrectUuid() {
            Product product = ProductTestBuilder.builder()
                    .withUuid(PRODUCT_INCORRECT_UUID)
                    .build()
                    .buildProduct();
            ProductNotFoundException exception = new ProductNotFoundException(product.getUuid());

            when(productRepository.findById(product.getUuid()))
                    .thenThrow(exception);

            assertThrows(ProductNotFoundException.class, () -> productService.update(product.getUuid(), any(ProductDto.class)));
            verify(mapper, never()).toInfoProductDto(product);
            verify(productRepository, never()).save(any(Product.class));
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void deleteShouldDeleteProduct_whenCorrectUuid() {
            UUID uuid = ProductTestBuilder.builder().build().getUuid();

            doNothing()
                    .when(productRepository).delete(uuid);

            productService.delete(uuid);
        }
    }
}
