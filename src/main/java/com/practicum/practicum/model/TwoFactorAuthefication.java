package com.practicum.practicum.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class TwoFactorAuthefication {
    @Id
    private String userId;
    private String secret;
    private String method;
    private boolean enadled;
}
