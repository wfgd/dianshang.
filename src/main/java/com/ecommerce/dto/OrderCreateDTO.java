package com.ecommerce.dto;

import java.util.List;

public class OrderCreateDTO {
    private List<Integer> cartItemIds;

    public List<Integer> getCartItemIds() { return cartItemIds; }
    public void setCartItemIds(List<Integer> cartItemIds) { this.cartItemIds = cartItemIds; }
}