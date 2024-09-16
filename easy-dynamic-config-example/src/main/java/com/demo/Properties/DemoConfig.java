package com.demo.Properties;

import java.util.List;

public class DemoConfig {

    private String productName;

    private List<Integer> size;


    private List<Sku> skus;

    public class Sku {

        public Sku() {
        }

        public Sku(String color, Double sellPrice) {
            this.color = color;
            this.sellPrice = sellPrice;
        }

        private String color;
        private Double sellPrice;

        @Override
        public String toString() {
            return "Sku{" +
                    "color='" + color + '\'' +
                    ", sellPrice=" + sellPrice +
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
