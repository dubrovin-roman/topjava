package ru.javawebinar.topjava.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.UserHasEmail;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Locale;
import java.util.Objects;

@Component
public class AdminUserValidator implements Validator {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageSource messageSource;
    private Locale locale = Locale.getDefault();

    @Override
    public boolean supports(Class<?> clazz) {
        return UserHasEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserHasEmail user = (UserHasEmail) target;
        User userFromDB = userRepository.getByEmail(user.getEmail());

        if (userFromDB != null) {
            if (!user.isNew()) {
                if ((isAdmin(userFromDB) && SecurityUtil.authUserId() == user.getId())
                        || Objects.equals(user.getId(), userFromDB.getId())) {
                    return;
                }
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

    private boolean isAdmin(User user) {
        return user.getRoles().contains(Role.ADMIN);
    }
}
