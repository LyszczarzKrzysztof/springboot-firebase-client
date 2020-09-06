package com.example.springbootfirebaseclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;

@Controller
public class FirebaseController {

    public City city;

    @Autowired
    public FirebaseController(City city) {
        this.city = city;
    }

    @GetMapping("la-info")
    public String displayData(Model model) {

        model.addAttribute("name", city.getName());
        model.addAttribute("state", city.getState());
        model.addAttribute("religion", city.getReligion());
        model.addAttribute("country", city.getCountry());
        model.addAttribute("regions", city.getRegions());

        return "firebase-gui";
    }
}
