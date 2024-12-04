package com.example.vehicleService.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


public class VnpayResponseDTO {
    private String payLink;
    private String vnp_TxnRef;

    public VnpayResponseDTO() {
    }

    public VnpayResponseDTO(String payLink, String vnp_TxnRef) {
        this.payLink = payLink;
        this.vnp_TxnRef = vnp_TxnRef;
    }


    public String getPayLink() {
        return payLink;
    }

    public void setPayLink(String payLink) {
        this.payLink = payLink;
    }

    public String getVnp_TxnRef() {
        return vnp_TxnRef;
    }

    public void setVnp_TxnRef(String vnp_TxnRef) {
        this.vnp_TxnRef = vnp_TxnRef;
    }
}
