package com.example.isa.library.validator;

import com.example.isa.library.dao.UserDao;
import com.example.isa.library.dto.CreateUserDto;
import lombok.Getter;

public class LoginUserValidator implements Validator<CreateUserDto> {

    @Getter
    private static final LoginUserValidator INSTANCE = new LoginUserValidator();
    private static final UserDao userDao = UserDao.getInstance();
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Override
    public ValidationResult isValid(CreateUserDto object) {
        ValidationResult validationResult = new ValidationResult();

        if (!object.getEmail().matches(EMAIL_PATTERN)) {
            validationResult.add(Error.of("invalid.email", "Email is invalid"));
        }
        if (userDao.findByEmail(object.getEmail()).isEmpty()) {
            validationResult.add(Error.of("invalid.email", "User with this email address doesn't exist"));
        }
        if (object.getPassword().length() < 8) {
            validationResult.add(Error.of("invalid.password", "Password is too short"));
        }

        return validationResult;
    }
}
