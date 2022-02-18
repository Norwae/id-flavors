package de.codecentric.javaland.idflavors;

import lombok.Data;

import java.util.List;

public class SimpleIds {

    void performPurchase(Simple.CustomerId userId, Simple.ProductId productId, long amount) {
        Simple api = null;

        var basket = api.getBasketForUser(userId);
        var alreadyIn = api.getProductsInShoppingCart(userId, basket.getId());
        var product = api.getProduct(productId);
        if (!alreadyIn.contains(product)) {
            api.addProductToShoppingCart(basket.getId(), product.getId(), amount);
            var paymentToken = api.getPaymentToken(basket.getId(), userId);
            api.confirmPaymentAndPlaceOrder(userId, paymentToken.getId(), basket.getId());
        }
    }
}

interface Simple {
    @Data final class ProductId {
        private long value;
    }
    @Data final class CustomerId{
        private long value;
    }
    @Data final class BasketId {
        private long value;
    }
    @Data final class OrderId {
        private long value;
    }
    @Data final class PaymentId {
        private long value;
    }

    Basket getBasketForUser(CustomerId userId);
    void addProductToShoppingCart(BasketId basketId, ProductId productId, long amount);
    Product getProduct(ProductId productId);
    List<Product> getProductsInShoppingCart(CustomerId userId, BasketId basketId);
    Payment getPaymentToken(BasketId basketId, CustomerId userId);
    Order confirmPaymentAndPlaceOrder(CustomerId userId, PaymentId paymentToken, BasketId basketId);


    @Data
    class Customer {
        CustomerId id;
    }

    @Data
    class Basket {
        BasketId id;
    }

    @Data
    class Product {
        ProductId id;
    }

    @Data
    class Payment {
        PaymentId id;
    }

    @Data
    class Order {
        OrderId id;
    }
}