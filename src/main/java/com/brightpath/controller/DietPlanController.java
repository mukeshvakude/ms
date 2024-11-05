package com.brightpath.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.brightpath.model.Diet;
import com.brightpath.services.DietService;

import java.util.List;

@Controller
public class DietPlanController {

    @Autowired
    private DietService dietService; // Autowire your service

    @GetMapping("/diet/view")
    @ResponseBody
    public ResponseEntity<Diet> viewDietPlans(@RequestParam Long memberId) {
        Diet dietPlans = dietService.getDietsByMemberId(memberId);
        if (dietPlans == null) {
        
            return ResponseEntity.ok(new Diet());
        }
        return ResponseEntity.ok(dietPlans);
    }



   
}
