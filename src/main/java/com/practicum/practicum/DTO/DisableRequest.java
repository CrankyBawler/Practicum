package com.practicum.practicum.DTO;



import lombok.Data;

@Data
public class DisableRequest {
    private String userId;
    private String code;
}