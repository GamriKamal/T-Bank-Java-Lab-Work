package irmag.io.TbankTranslator.Repository;

import irmag.io.TbankTranslator.Entity.TranslationRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("translationRecordService")
public interface TranslationRecordRepository {
    List<TranslationRecord> findAll();

    void save(TranslationRecord record);

    void editRecord(TranslationRecord record);

    void deleteById(long recordId);

    void delete(TranslationRecord record);
}
