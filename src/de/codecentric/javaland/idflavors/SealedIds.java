package de.codecentric.javaland.idflavors;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public class SealedIds {

    void performPurchase(Sealed.Id<Sealed.CustomerId> userId, Sealed.Id<Sealed.ProductId> productId, long amount) {
        Sealed api = null;

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

interface Sealed {
    sealed interface IdType { }
    final class ProductId implements IdType {}
    final class CustomerId implements IdType {}
    final class BasketId implements IdType {}
    final class OrderId implements IdType {}
    final class PaymentId implements IdType {}



    Basket getBasketForUser(Id<CustomerId> userId);
    void addProductToShoppingCart(Id<BasketId> basketId, Id<ProductId> productId, long amount);
    Product getProduct(Id<ProductId> productId);
    List<Product> getProductsInShoppingCart(Id<CustomerId> userId, Id<BasketId> basketId);
    Payment getPaymentToken(Id<BasketId> basketId, Id<CustomerId> userId);
    Order confirmPaymentAndPlaceOrder(Id<CustomerId> userId, Id<PaymentId> paymentToken, Id<BasketId> basketId);

    @Data
    @JsonSerialize(using = SealedIdFeatures.Serialize.class)
    @JsonDeserialize(using = SealedIdFeatures.Deserialize.class)
    class Id<T extends IdType> {
        final long raw;
    }

    @Data
    class Customer {
        Id<CustomerId> id;
    }

    @Data
    class Basket {
        Id<BasketId> id;
    }

    @Data
    class Product {
        Id<ProductId> id;
    }

    @Data
    class Payment {
        Id<PaymentId> id;
    }

    @Data
    class Order {
        Id<OrderId> id;
    }
}