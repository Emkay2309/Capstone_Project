package com.capstone.backend.dtos;


import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ApiResponseMessage {
    private String message;
    private boolean success;
    private HttpStatus status;
}
