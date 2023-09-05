package org.avokado2.sp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.avokado2.sp.manager.UserDetailsManagerImpl;
import org.avokado2.sp.manager.UserManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Controller
public class RegistrationController {
    private UserDetailsManagerImpl userDetailsManager;

    private final UserManager userManager;
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView openRegister() {
        return new ModelAndView("registration");
    }
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView register(@RequestParam(value = "login") String login,
                                 @RequestParam(value = "password")String password,
                                 @RequestParam(value = "confirmPassword") String confirmPassword,
                                 HttpServletRequest httpRequest) {
        log.info("User registration. Login: {}. Password: {}. Confirm password: {}", login, password, confirmPassword);
        Map model = new HashMap<>();
        if (password.isEmpty()){
            model.put("errorMessage", "password is not set");
            return new ModelAndView("registration", model);
        }
        if (confirmPassword.isEmpty()){
            model.put("errorMessage", "confirm password is not set");
            return new ModelAndView("registration", model);
        }
        if (!password.equals(confirmPassword)){
            model.put("errorMessage", "passwords don't match");
            return new ModelAndView("registration", model);
        }
        if (login.isEmpty()){
            model.put("errorMessage", "login is not set");
            return new ModelAndView("registration", model);
        }
        if (login.length() < 3 ){
            model.put("errorMessage", "login is too short");
            return new ModelAndView("registration", model);
        }
        if (login.length() > 50 ){
            model.put("errorMessage", "login is too long");
            return new ModelAndView("registration", model);
        }
        if (password.length() > 50 ){
            model.put("errorMessage", "password is too long");
            return new ModelAndView("registration", model);
        }
        if (password.length() < 6 ){
            model.put("errorMessage", "password is too short");
            return new ModelAndView("registration", model);
        }

        try {
            if (!userManager.registerUser(login, password)){
                model.put("errorMessage", "the login is already taken");
                return new ModelAndView("registration", model);
            }
        } catch (Exception e) {
            model.put("errorMessage", "error, please repeat");
            return new ModelAndView("registration", model);
        }
        try {
            httpRequest.login(login, password);
        } catch (ServletException e) {
            model.put("errorMessage", "Error during auto-login");
            log.error("Error during auto-login", e);
            return new ModelAndView("registration", model);
        }
        return new ModelAndView("redirect:/");
    }

}
