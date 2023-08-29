package com.jarawin.issuer.dto.composite;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestId implements Serializable {
    private String transactionId;
    private String requestId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TransactionRequestId))
            return false;
        TransactionRequestId that = (TransactionRequestId) o;
        return Objects.equals(transactionId, that.transactionId) &&
                Objects.equals(requestId, that.requestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, transactionId);
    }
}