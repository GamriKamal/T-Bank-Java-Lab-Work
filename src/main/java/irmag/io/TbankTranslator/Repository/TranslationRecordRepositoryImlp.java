package irmag.io.TbankTranslator.Repository;

import irmag.io.TbankTranslator.CustomException.NoRecordsFoundException;
import irmag.io.TbankTranslator.Entity.TranslationRecord;
import irmag.io.TbankTranslator.PostgreSQL.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TranslationRecordRepositoryImlp implements TranslationRecordRepository {

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private final Logger logger = LoggerFactory.getLogger(TranslationRecordRepositoryImlp.class);

    @Override
    public List<TranslationRecord> findAll() {
        List<TranslationRecord> records = new ArrayList<>();
        String query = "SELECT * FROM t_bank_translations";

        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                records.add(mapResultSetToRecord(resultSet));
            }
        } catch (SQLException e) {
            logger.error(RED + e + RESET);
        }

        if (records.isEmpty()) {
            throw new NoRecordsFoundException("No records found in the database");
        }
        return records;
    }

    @Override
    public void save(TranslationRecord translationRecord) {
        String query = "INSERT INTO t_bank_translations (ipAddress, inputText, translatedText, timestamp) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, translationRecord.getIpAddress());
            preparedStatement.setString(2, translationRecord.getInputText());
            preparedStatement.setString(3, translationRecord.getTranslatedText());
            preparedStatement.setObject(4, translationRecord.getTimestamp());

            preparedStatement.executeUpdate();
            logger.info(GREEN + "Successfully saved translation record" + RESET);
        } catch (SQLException e) {
            logger.error(RED + e + RESET);
        }
    }

    @Override
    public void editRecord(TranslationRecord translationRecord) {
        String query = "UPDATE t_bank_translations SET ipAddress=?, inputText=?, translatedText=?, timestamp=? WHERE id=?";

        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, translationRecord.getIpAddress());
            preparedStatement.setString(2, translationRecord.getInputText());
            preparedStatement.setString(3, translationRecord.getTranslatedText());
            preparedStatement.setObject(4, translationRecord.getTimestamp());

            preparedStatement.executeUpdate();
            logger.info(BLUE + "Successfully edited translation record" + RESET);
        } catch (SQLException e) {
            logger.error(RED + e + RESET);
        }
    }

    @Override
    public void deleteById(long recordId) {
        String query = "DELETE FROM t_bank_translations WHERE id=?";

        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(query)) {
            preparedStatement.setLong(1, recordId);
            preparedStatement.executeUpdate();
            logger.info(YELLOW + "Successfully deleted translation record" + RESET);
        } catch (SQLException e) {
            logger.error(RED + e + RESET);
        }
    }

    @Override
    public void delete(TranslationRecord translationRecord) {
        deleteById(translationRecord.getId());
    }

    private TranslationRecord mapResultSetToRecord(ResultSet resultSet) throws SQLException {
        TranslationRecord translationRecord = new TranslationRecord();
        translationRecord.setId(resultSet.getLong("id"));
        translationRecord.setIpAddress(resultSet.getString("ipAddress"));
        translationRecord.setInputText(resultSet.getString("inputText"));
        translationRecord.setTranslatedText(resultSet.getString("translatedText"));
        translationRecord.setTimestamp(resultSet.getObject("timestamp", LocalDateTime.class));
        return translationRecord;
    }
}
