package com.example.warehouse_service.converter;

import com.example.warehouse_service.entity.Product.ProductStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ProductStatusConverter implements AttributeConverter<ProductStatus, String> {

    @Override
    public String convertToDatabaseColumn(ProductStatus status) {
        return (status == null) ? null : status.getValue();
    }

    @Override
    public ProductStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
for (ProductStatus status : ProductStatus.values()) {
    if (status.getValue().equalsIgnoreCase(dbData)) {
        return status;
    }
}
throw new IllegalArgumentException("Unknown status: " + dbData);
    }
}