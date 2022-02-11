package de.codecentric.javaland.idflavors;


import lombok.Data;

import java.util.List;

public class UntypedIds {
    void performPurchase(long userId, long productId, long amount) {
        Untyped api = null;

        var basket = api.getBasketForUser(userId);
        var alreadyIn = api.getProductsInShoppingCart(userId, basket.getId());
        var product = api.getProduct(productId);
        if (!alreadyIn.contains(product)) {
            api.addProductToShoppingCart(amount, basket.getId(), product.getId());
            var paymentToken = api.getPaymentToken(basket.getId(), userId);
            api.confirmPaymentAndPlaceOrder(paymentToken.getId(), userId, basket.getId());
        }
    }
}

interface Untyped {

    Basket getBasketForUser(long userId);
    void addProductToShoppingCart(long basketId, long productId, long amount);
    Product getProduct(long productId);
    List<Product> getProductsInShoppingCart(long userId, long basketId);
    Payment getPaymentToken(long basketId, long userId);
    Order confirmPaymentAndPlaceOrder(long userId, long paymentToken, long basketId);
    
    @Data
    class Customer {
        long id;
    }

    @Data
    class Basket {
        long id;
    }

    @Data
    class Product {
        long id;
    }

    @Data
    class Payment {
        long id;
    }
    
    @Data
    class Order {
        long id;
    }
}
