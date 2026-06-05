package edu.barnett.gearwood_puzzle_store.dtos;

import edu.barnett.gearwood_puzzle_store.entities.ShippingInfo;

public record ShippingInfoDto(String name,
                              String address,
                              String address2,
                              String city,
                              String state,
                              String zip) {
    public ShippingInfoDto(ShippingInfo shippingInfo) {
        this(shippingInfo.getName(),
                shippingInfo.getAddress(),
                shippingInfo.getAddress2(),
                shippingInfo.getCity(),
                shippingInfo.getState(),
                shippingInfo.getZip());
    }
}
