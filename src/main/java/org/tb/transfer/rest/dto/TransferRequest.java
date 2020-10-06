package org.tb.transfer.rest.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransferRequest implements Serializable {
    private Long sourceAccountId;
    private Long targetAccountId;
    private BigDecimal amount;

    public Long getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(Long sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public Long getTargetAccountId() {
        return targetAccountId;
    }

    public void setTargetAccountId(Long targetAccountId) {
        this.targetAccountId = targetAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
