package app.converter.utils;


import app.converter.models.User;
import app.converter.services.UserDetailsServiceImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public record UserValidator(UserDetailsServiceImpl userDetailsService) implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        try {
            userDetailsService.loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException e) {
            return; //user doesn't exist, ok
        }
        errors.rejectValue("username", "", "User with the same name already exists");
    }
}
