package com.example.it_iap.dto.notification.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminCreateNotificationRequest {
    @NotBlank(message = "INVALID_NOTIFICATION_TITLE")
    String title;

    @NotBlank(message = "INVALID_NOTIFICATION_CONTENT")
    String content;

    String link;
}
