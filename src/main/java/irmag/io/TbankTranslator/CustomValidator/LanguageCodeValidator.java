package irmag.io.TbankTranslator.CustomValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class LanguageCodeValidator implements ConstraintValidator<ValidLanguageCode, String> {

    private static final List<String> EXCEPTION_CODES = Arrays.asList("ceb", "emj", "kazlat", "mhr", "mrj", "pap", "pt-BR", "sah", "sr-Latn", "udm", "uzbcyr");

    @Override
    public void initialize(ValidLanguageCode constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.length() == 2 || EXCEPTION_CODES.contains(value);
    }
}
