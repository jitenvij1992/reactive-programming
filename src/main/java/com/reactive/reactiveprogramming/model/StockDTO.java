package com.reactive.reactiveprogramming.model;

import java.math.BigDecimal;
import java.util.Objects;

public class StockDTO {

    private String name;
    private BigDecimal price;

    public StockDTO(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockDTO stockDTO = (StockDTO) o;
        return Objects.equals(name, stockDTO.name) &&
                Objects.equals(price, stockDTO.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
