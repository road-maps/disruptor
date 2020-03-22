package com.byf.disruptor.advance.cheesefactory.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Cheese {
    private Date productionDate;
    private long sequence;
    private String address;
}
