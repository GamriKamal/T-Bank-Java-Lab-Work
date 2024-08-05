CREATE TABLE t_bank_translations
(
    id SERIAL PRIMARY KEY,
    ipAddress VARCHAR(45) NOT NULL,
    inputText TEXT NOT NULL,
    translatedText TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL
);

CREATE INDEX idx_translation_record_timestamp
    ON t_bank_Translations (timestamp);
