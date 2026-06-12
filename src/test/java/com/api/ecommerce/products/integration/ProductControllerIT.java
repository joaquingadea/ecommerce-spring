package com.api.ecommerce.products.integration;
import com.api.ecommerce.products.domain.Product;
import com.api.ecommerce.products.domain.ProductCategory;
import com.api.ecommerce.products.infrastructure.persistence.IProductCategoryRepository;
import com.api.ecommerce.products.infrastructure.persistence.IProductRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IProductCategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class CreateProducts{

        @Test
        void shouldCreateProductWhenRequestIsValid() throws Exception {

            ProductCategory categoryTest = new ProductCategory();
            categoryTest.setName("categoryTest");
            categoryRepository.save(categoryTest);

            MockMultipartFile image1 = new MockMultipartFile(
                    "images",
                    "image1.jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    "fake image".getBytes()
            );
            MockMultipartFile image2 = new MockMultipartFile(
                    "images",
                    "image2.jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    "fake image".getBytes()
            );

            mockMvc.perform(multipart("/admin/create-product")
                    .param("name","New Product")
                    .param("description","New description's product")
                    .param("price","1000")
                    .param("stock","10")
                    .param("categoryId","1")
                    .file(image1)
                    .file(image2)
            ).andExpect(status().isCreated());

            Product product = productRepository.findByName("New Product")
                    .orElseThrow();

            assertEquals("New Product", product.getName());
            assertEquals(new BigDecimal("1000"), product.getUnitPrice());
            assertEquals(10, product.getStock());
        }

        @Nested
        class ProductCategoryTests{
            @Test
            void shouldEditCategoryIfRequestIsValid() throws Exception {

                ProductCategory existingCategory = new ProductCategory();
                existingCategory.setName("Antiguo nombre de categoria");

                String newName = "Nuevo nombre de categoria";
                mockMvc.perform(patch("/edit-product-category/{id}",existingCategory.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newName)))
                        .andExpect(status().isOk());

                ProductCategory categoryRepo = categoryRepository.findById(existingCategory.getId()).orElseThrow();
                assertEquals(newName,categoryRepo.getName());
            }
        }
    }
}
