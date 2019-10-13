package com.axiom.model;

import lombok.Data;

import java.net.URL;

@Data
public class Mobile {

    private Integer id;
    private String brand;
    private String phone;
    private URL picture;

    private Release release;

    private String sim;
    private String resolution;

    private Hardware hardware;

}
