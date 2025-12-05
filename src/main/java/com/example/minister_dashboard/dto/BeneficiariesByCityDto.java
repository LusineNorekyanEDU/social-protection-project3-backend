package com.example.minister_dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BeneficiariesByCityDto {

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Item{
        private String city;
        private long beneficiaryCount;
    }

    private List<Item> items;
}
