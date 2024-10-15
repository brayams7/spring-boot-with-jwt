package com.brayck.library_api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorMessage {
   private int statusCode;
   private Date timestamp;
   private String message;
   private String description;
   private Set<String> validationErrors;
   private Map<String, String> errors;
}
