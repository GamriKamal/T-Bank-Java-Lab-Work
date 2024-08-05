package irmag.io.TbankTranslator.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Schema(description = "Запись перевода для бд (postgresql)")
public class TranslationRecord {
    @Schema(description = "ID записи перевода", example = "1")
    private long id;

    @Schema(description = "IP-адрес пользователя, который сделал запрос", example = "192.168.1.1")
    @NotBlank
    private String ipAddress;

    @Schema(description = "Текст для перевода", example = "Hello")
    @NotBlank
    private String inputText;

    @Schema(description = "Переведенный текст", example = "Привет")
    @NotBlank
    private String translatedText;

    @Schema(description = "Время запроса", example = "2024-08-05T12:34:56")
    @NotNull
    private LocalDateTime timestamp;

    public TranslationRecord(String ipAddress, String inputText, String translatedText, LocalDateTime timestamp) {
        this.ipAddress = ipAddress;
        this.inputText = inputText;
        this.translatedText = translatedText;
        this.timestamp = timestamp;
    }
}
