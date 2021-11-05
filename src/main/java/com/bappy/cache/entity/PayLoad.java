package com.bappy.cache.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PayLoad {
    private int id;
    private String payLoad;
    private String type;
}
