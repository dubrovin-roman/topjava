package ru.javawebinar.topjava.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Locale;

@Component
public class ProfileUserValidator implements Validator {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageSource messageSource;
    private Locale locale = Locale.getDefault();

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserTo user = (UserTo) target;
        User userFromDB = userRepository.getByEmail(user.getEmail());

        if (userFromDB != null) {
            if (SecurityUtil.safeGet() != null && SecurityUtil.get().getUserTo().getEmail().equals(user.getEmail())) {
                return;
            }
            errors.rejectValue("email",
                    "email.exists",
                    messageSource.getMessage("error.email.exists", new Object[]{}, locale));
        }
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
