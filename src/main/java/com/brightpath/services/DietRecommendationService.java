package com.brightpath.services;
import org.springframework.stereotype.Service;

@Service
public class DietRecommendationService {

    public String recommendDiet(int age, int height, int weight, String gender) {
        // Ideal weight and height based on age and gender
        double idealWeight = calculateIdealWeight(age, height, gender);
        String dietRecommendation = "Balanced Diet"; // Default recommendation

        if (weight < idealWeight) {
            dietRecommendation = "High-Protein Diet"; // For muscle gain
        } else if (weight > idealWeight) {
            dietRecommendation = "Low-Carb Diet"; // For weight loss
        } else {
            dietRecommendation = "Maintain Balanced Diet"; // For maintenance
        }

        return dietRecommendation;
    }

    public int calculateDailyCaloricNeeds(int age, int height, int weight, String gender, String activityLevel) {
        double bmr;
        if (gender.equalsIgnoreCase("male")) {
            bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        } else {
            bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
        }

        double activityMultiplier = getActivityMultiplier(activityLevel);
        return (int) (bmr * activityMultiplier);
    }

    private double getActivityMultiplier(String activityLevel) {
        switch (activityLevel.toLowerCase()) {
            case "sedentary": return 1.2;
            case "lightly active": return 1.375;
            case "moderately active": return 1.55;
            case "very active": return 1.725;
            case "super active": return 1.9;
            default: return 1.2; // Default to sedentary
        }
    }

    private double calculateIdealWeight(int age, int height, String gender) {
        // Ideal weight based on age and gender guidelines
        if (age >= 18 && age <= 24) {
            return gender.equalsIgnoreCase("male") ? 70 : 65; // Adjusted example values
        } else if (age >= 25 && age <= 34) {
            return gender.equalsIgnoreCase("male") ? 75 : 70;
        } else if (age >= 35 && age <= 44) {
            return gender.equalsIgnoreCase("male") ? 80 : 75;
        } else if (age >= 45 && age <= 54) {
            return gender.equalsIgnoreCase("male") ? 85 : 80;
        } else {
            return gender.equalsIgnoreCase("male") ? 90 : 85;
        }
    }
}
