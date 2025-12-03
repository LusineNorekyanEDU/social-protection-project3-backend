package com.example.minister_dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ApplicationFunnelDto {
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Item {
        private String status;
        private long count;
        private double percentage;
    }

    private long total;
    private List<Item> items;
}
