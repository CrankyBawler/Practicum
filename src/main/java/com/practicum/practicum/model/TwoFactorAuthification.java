package com.practicum.practicum.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class TwoFactorAuthification {
    @Id
    private String userId;
    private String secret;
    private String method;
    private boolean enadled;
}
