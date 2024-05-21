package ru.javawebinar.topjava.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.HasEmail;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Component
public class UserValidator implements Validator {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HttpServletRequest request;

    @Override
    public boolean supports(Class<?> clazz) {
        return HasEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasEmail user = (HasEmail) target;
        User userFromDB = userRepository.getByEmail(user.getEmail());
        String requestURI = request.getRequestURI();

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
            errors.rejectValue("email", "error.email.exists");
        }
    }
}
