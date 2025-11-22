package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController extends BaseController {

    @Autowired
    private UserRepository userRepository;

    // Show login form
    @GetMapping("/login")
    public ModelAndView showLogin(HttpSession session) {
        if (isAuthenticated(session)) {
            return view("home"); // login.jsp
        }
        return view("login"); // login.jsp
    }

    // Process login
    @PostMapping("/login")
    public ModelAndView login(@RequestParam String email,
            @RequestParam String password,
            HttpSession session) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            // Save user in session
            session.setAttribute("user", user);
            // Redirect based on role
            return redirect("/");
            // switch (user.getRole().toUpperCase()) {
            // case "ADMIN":
            // return new ModelAndView("redirect:/admin/dashboard");
            // case "DOCTOR":
            // return new ModelAndView("redirect:/doctors");
            // case "PATIENT":
            // return new ModelAndView("redirect:/patients");
            // default:
            // return new ModelAndView("redirect:/login");
            // }
        } else {
            return view("login", attrs(
                    "error", "Invalid email or password"));
        }
    }

    // Logout
    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        return new ModelAndView("redirect:/login");
    }
}
