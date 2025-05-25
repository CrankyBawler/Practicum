package com.practicum.practicum.model;



import lombok.Data;
import javax.persistence.*;

    @Entity
    @Data
    public class TwoFactorAuth {
        @Id
        private String userId;
        private String secret;
        private String method; // SMS, EMAIL, AUTHENTICATOR_APP
        private boolean enabled;
    }