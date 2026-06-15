package com.enviro.assessment.junior.asandile.mapper;

import com.enviro.assessment.junior.asandile.dto.response.PortfolioResponseDTO;
import com.enviro.assessment.junior.asandile.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MapStruct mapper for Product entity to DTO conversion
 *
 * @author Asandile
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    /**
     * Convert Product entity to ProductDTO
     *
     * @param product product entity
     * @return product DTO
     */
    @Mapping(target = "productId", source = "id")
    @Mapping(target = "productName", source = "productName")
    @Mapping(target = "productType.displayName", source = "productType.displayName")
    @Mapping(target = "balance", source = "balance")
    @Mapping(target = "isRetirementProduct", expression = "java(product.getProductType().isRetirement())")
    PortfolioResponseDTO.ProductDTO toDto(Product product);

    /**
     * Convert list of products to DTOs
     *
     * @param products list of products
     * @return list of product DTOs
     */
    default List<PortfolioResponseDTO.ProductDTO> toDtoList(List<Product> products) {
        if (products == null) return List.of();
        return products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}