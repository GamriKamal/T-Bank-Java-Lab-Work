package irmag.io.TbankTranslator.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import irmag.io.TbankTranslator.CustomValidator.ValidLanguageCode;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Schema(description = "Запрос на перевод")
public class TranslationRequest {
    @Schema(description = "Изначальный текст, который нужно перевести.", example = "Hello")
    @NotBlank
    private String text;

    @Schema(description = "Язык с которого надо перевести текст.", example = "en")
    @NotBlank
    @ValidLanguageCode
    private String sourceLanguageCode;

    @Schema(description = "Язык на который надо перевести текст.", example = "ru")
    @NotBlank
    @ValidLanguageCode
    private String targetLanguageCode;
}
