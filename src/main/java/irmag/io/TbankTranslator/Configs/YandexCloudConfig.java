package irmag.io.TbankTranslator.Configs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class YandexCloudConfig {
    @Value("${yandex.cloud.api.key}")
    private String apiKey;

    @Value("${yandex.cloud.translate.url}")
    private String url;

    @Value("${yandex.cloud.supportedLanguages.url}")
    private String supportedLanguages;

    @Value("${yandex.cloud.folderId}")
    private String folderId;
}
