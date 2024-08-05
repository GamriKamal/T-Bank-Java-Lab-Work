package irmag.io.TbankTranslator.CustomValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LanguageCodeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLanguageCode {
    String message() default "Invalid language code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
