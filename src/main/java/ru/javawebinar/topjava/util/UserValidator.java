package ru.javawebinar.topjava.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.HasEmail;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Locale;
import java.util.Objects;

@Component
public class UserValidator implements Validator  {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageSource messageSource;
    private Locale locale = Locale.getDefault();
    private String requestURI;

    @Override
    public boolean supports(Class<?> clazz) {
        return HasEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasEmail user = (HasEmail) target;
        User userFromDB = userRepository.getByEmail(user.getEmail());

        if (userFromDB != null) {
            if (requestURI.contains("admin")) {
                if (!user.isNew()) {
                    if (SecurityUtil.authUserId() == user.getId()
                            || Objects.equals(user.getId(), userFromDB.getId())) {
                        return;
                    }
                }
            } else if (requestURI.contains("profile")) {
                if (SecurityUtil.safeGet() != null && SecurityUtil.get().getUserTo().getEmail().equals(user.getEmail())) {
                    return;
                }
            } else {
                throw new UnsupportedOperationException("Unsupported request URI by UserValidator: " + requestURI);
            }
            errors.rejectValue("email", "error.email.exists", "User with this email already exists");
        }
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
