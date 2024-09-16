package com.demo.Properties;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class DemoConfig {

    private String productName;

    private List<Integer> size;


    private List<Sku> skus;

    public static class Sku {

        public Sku() {
        }

        public Sku(String color, BigDecimal sellPrice) {
            this.color = color;
            this.sellPrice = sellPrice;
        }

        private String color;
//        private Double sellPrice;
        private BigDecimal sellPrice;

        private LocalDateTime online;

        @Override
        public String toString() {
            return "Sku{" +
                    "color='" + color + '\'' +
                    ", sellPrice=" + sellPrice +
                    ", online=" + online +
                    '}';
        }
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<Integer> getSize() {
        return size;
    }

    public void setSize(List<Integer> size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "DemoConfig{" +
                "productName='" + productName + '\'' +
                ", size=" + size +
                ", skus=" + skus +
                '}';
    }
}
