package de.codecentric.javaland.idflavors;

import lombok.Data;

import java.util.List;


public class GenericIds {

    void performPurchase(Generic.Id<Generic.Customer> userId, Generic.Id<Generic.Product> productId, long amount) {
        Generic api = null;

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

interface Generic {
    @Data
    class Id<Container> {
        long raw;
    }

    Basket getBasketForUser(Id<Customer> userId);
    void addProductToShoppingCart(Id<Basket> basketId, Id<Product> productId, long amount);
    Product getProduct(Id<Product> productId);
    List<Product> getProductsInShoppingCart(Id<Customer> userId, Id<Basket> basketId);
    Payment getPaymentToken(Id<Basket> basketId, Id<Customer> userId);
    Order confirmPaymentAndPlaceOrder(Id<Customer> userId, Id<Payment> paymentToken, Id<Basket> basketId);

    @Data
    class Customer {
        Id<Customer> id;
    }

    @Data
    class Basket {
        Id<Basket> id;
    }

    @Data
    class Product {
        Id<Product> id;
    }

    @Data
    class Payment {
        Id<Payment> id;
    }

    @Data
    class Order {
        Id<Order> id;
    }
}