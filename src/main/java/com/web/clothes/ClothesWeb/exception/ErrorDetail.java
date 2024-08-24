package com.web.clothes.ClothesWeb.exception;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ErrorDetail {
    private int errorCode;
    private String message;
}
