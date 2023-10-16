package com.inu.wanted.preassignment.models.jobopening;

import com.inu.wanted.preassignment.converters.MoneyConverter;
import com.inu.wanted.preassignment.models.Money;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class JobOpeningRewards {
    @Convert(converter = MoneyConverter.class)
    @Column(name = "rewards")
    private Money rewards;

    private JobOpeningRewards() {

    }

    public JobOpeningRewards(Money money) {
        this.rewards = money;
    }

    public Long value() {
        return rewards.amount();
    }
}
