package com.poc.model.dto;

public class ResponseDTO {
    public String cod;
    public String msg;

    public ResponseDTO() {
    }

    public ResponseDTO(String cod, String msg){
        this.cod = cod;
        this.msg = msg;
    }
}
