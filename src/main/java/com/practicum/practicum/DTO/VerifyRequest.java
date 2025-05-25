package com.practicum.practicum.DTO;

import lombok.Data;

@Data
public class VerifyRequest {
    private String userId;
    private String code;

}
