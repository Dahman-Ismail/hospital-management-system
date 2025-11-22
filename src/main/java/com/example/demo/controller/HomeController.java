package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController extends BaseController {

    @GetMapping("/")
    public ModelAndView home(Model model, HttpSession session) {
        if (isAuthenticated(session)) {
            return view("home");
        } else {
            return view("login");
        }
    }
}