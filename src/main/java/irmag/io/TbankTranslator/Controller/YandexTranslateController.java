package irmag.io.TbankTranslator.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import irmag.io.TbankTranslator.CustomException.NoRecordsFoundException;
import irmag.io.TbankTranslator.DTO.TranslationRequest;
import irmag.io.TbankTranslator.Entity.TranslationRecord;
import irmag.io.TbankTranslator.Repository.TranslationRecordRepository;
import irmag.io.TbankTranslator.Services.YandexTranslationService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("t-bank/")
@Tag(name = "Yandex Translate Controller", description = "API для перевода текстов и управления записями переводов")
public class YandexTranslateController {

    private final YandexTranslationService translationService;
    private final TranslationRecordRepository recordService;
    private final Logger logger = LoggerFactory.getLogger(YandexTranslateController.class);

    @Autowired
    public YandexTranslateController(YandexTranslationService translationService,
                                     @Qualifier("translationRecordRepositoryImlp") TranslationRecordRepository recordService) {
        this.translationService = translationService;
        this.recordService = recordService;
    }

    @Operation(summary = "Перевод текста", description = "Переводит текст с одного языка на другой и сохраняет запись перевода в бд (postgresql).")
    @PostMapping("/translate")
    public ResponseEntity<String> translate(@RequestBody TranslationRequest translationRequest, HttpServletRequest request) {
        try {
            String translatedText = translationService.translate(
                    translationRequest.getText(),
                    translationRequest.getSourceLanguageCode(),
                    translationRequest.getTargetLanguageCode()
            );
            recordService.save(new TranslationRecord(request.getRemoteAddr(), translationRequest.getText(), translatedText, LocalDateTime.now()));
            return ResponseEntity.ok(translatedText);
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Translation failed");
        }
    }

    @Operation(summary = "Поддерживаемые языки", description = "Возвращает список поддерживаемых языков для перевода.")
    @GetMapping("/supportedLanguages")
    public ResponseEntity<String> supportedLanguages() {
        try {
            return ResponseEntity.ok(translationService.getSupportedLanguages());
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to retrieve supported languages");
        }
    }

    @Operation(summary = "Проверка записей", description = "Возвращает список всех записей переводов.")
    @GetMapping("/checkRecords")
    public ResponseEntity<String> checkRecords() {
        try {
            List<TranslationRecord> recordList = recordService.findAll();
            return ResponseEntity.ok(recordList.toString());
        } catch (NoRecordsFoundException e) {
            logger.error(e.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to retrieve records");
        }
    }
}
