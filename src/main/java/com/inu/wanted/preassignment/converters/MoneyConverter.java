package com.inu.wanted.preassignment.converters;

import com.inu.wanted.preassignment.models.Money;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class MoneyConverter implements AttributeConverter<Money, Long> {
    @Override
    public Long convertToDatabaseColumn(Money money) {
        return money.amount();
    }

    @Override
    public Money convertToEntityAttribute(Long amount) {
        return new Money(amount);
    }
}
