package com.example.minispring.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Opts {
    private Integer optId;  
    private String optCode;
    private LocalDateTime issuedAt;
    private LocalDateTime expiredAt;
    private Boolean verify;
    private Integer userId;
}
