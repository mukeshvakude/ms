package com.brightpath.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brightpath.model.Diet;
import com.brightpath.model.Member;
import com.brightpath.repository.DietRepository;
import com.brightpath.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class DietService {

    @Autowired
    private DietRepository dietRepository; // Autowire your repository

    @Autowired 
    MemberRepository memberRepository;
    public List<Diet> getAllDietPlans() {
        return dietRepository.findAll(); // Retrieve all diet plans
    }

    public void addDietPlan(Diet diet) {
        dietRepository.save(diet); // Save the new diet plan
    }

    public Diet getDietPlanById(Long id) {
        return dietRepository.findById(id).orElse(null); // Find diet plan by ID
    }

    public void updateDietPlan(Diet diet) {
        dietRepository.save(diet); // Update the existing diet plan
    }
    
    public List<Diet> getRecommendedDiets(Double height, Double weight, Integer age) {
        List<Diet> recommendedDiets = new ArrayList<>();
        
        // Example logic to find diets based on criteria
        List<Diet> allDiets = dietRepository.findAll(); // Get all diets

        for (Diet diet : allDiets) {
            // Check age range
            if (age >= diet.getMinAge() && age <= diet.getMaxAge()) {
                // Check height and weight criteria (you can adjust the logic as needed)
                if (height >= diet.getHeightCm() && weight >= diet.getWeightKg()) {
                    recommendedDiets.add(diet);
                }
            }
        }
        
        return recommendedDiets;
    }
    
    public Diet getDietsByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("Member not found"));
        return member.getDiet(); // Return the diet associated with the member
    }

}
