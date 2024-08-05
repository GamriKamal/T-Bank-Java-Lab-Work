package irmag.io.TbankTranslator.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import irmag.io.TbankTranslator.Configs.YandexCloudConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.*;

@Service
public class YandexTranslationService {
    private final Logger logger = LoggerFactory.getLogger(YandexTranslationService.class);
    private final YandexCloudConfig yandexConfig;
    private final RestTemplate restTemplate;

    @Autowired
    public YandexTranslationService(YandexCloudConfig yandexConfig, RestTemplate restTemplate) {
        this.yandexConfig = yandexConfig;
        this.restTemplate = restTemplate;
    }

    public String translate(String text, String sourceLanguageCode, String targetLanguageCode) {
        ExecutorService tempExecutorService = Executors.newFixedThreadPool(10);
        List<String> textParts = Arrays.stream(text.split("\\s+")).toList();

        List<Callable<String>> tasks = textParts.stream()
                .map(part -> (Callable<String>) () -> translateText(part, sourceLanguageCode, targetLanguageCode))
                .toList();

        List<String> results = new ArrayList<>();
        try {
            List<Future<String>> futures = tempExecutorService.invokeAll(tasks);
            for (Future<String> future : futures) {
                try {
                    results.add(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    logger.error(e.toString());
                    results.add("Error occurred during translation");
                }
            }
        } catch (InterruptedException e) {
            logger.error(e.toString());
            Thread.currentThread().interrupt();
        } finally {
            tempExecutorService.shutdown();
        }

        return getResult(results);
    }

    public String getSupportedLanguages() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Api-Key " + yandexConfig.getApiKey());

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(yandexConfig.getSupportedLanguages(), HttpMethod.POST, requestEntity, String.class);

            return response.getBody();
        } catch (Exception e) {
            logger.error(e.toString());
            return "Error occurred during translation";
        }
    }

    private String translateText(String text, String sourceLanguageCode, String targetLanguageCode) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("targetLanguageCode", targetLanguageCode);
            requestBody.put("sourceLanguageCode", sourceLanguageCode);
            requestBody.put("texts", new String[]{text});
            requestBody.put("folderId", yandexConfig.getFolderId());

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Api-Key " + yandexConfig.getApiKey());

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(yandexConfig.getUrl(), HttpMethod.POST, requestEntity, String.class);

            return response.getBody();
        } catch (Exception e) {
            logger.error(e.toString());
            return "Error occurred during translation";
        }
    }

    private String getResult(List<String> results) {
        ObjectMapper objectMapper = new ObjectMapper();
        StringBuilder builder = new StringBuilder();
        try {
            results.forEach(string -> {
                try {
                    JsonNode rootNode = objectMapper.readTree(string);
                    JsonNode translationsNode = rootNode.path("translations");
                    if (translationsNode.isArray() && !translationsNode.isEmpty()) {
                        JsonNode firstTranslationNode = translationsNode.get(0);
                        String text = firstTranslationNode.path("text").asText();
                        builder.append(text).append(" ");
                    }
                } catch (Exception e) {
                    logger.error(e.toString());
                }
            });

        } catch (Exception e) {
            logger.error(e.toString());
        }

        return builder.toString();
    }
}
