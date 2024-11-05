package com.brightpath.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "diet")
public class Diet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    @Column(name = "min_age", nullable = false)
    private int minAge;

    @Column(name = "max_age", nullable = false)
    private int maxAge;

    @Column(name = "height_cm", nullable = false)
    private int heightCm;

    @Column(name = "weight_kg", nullable = false)
    private int weightKg;

    @Column(name = "recommended_diet", nullable = false)
    private String recommendedDiet;

    @Column(name = "ideal_weight_kg", nullable = false)
    private int idealWeightKg;

    @Column(name = "bmr_men", nullable = false)
    private int bmrMen;

    @Column(name = "bmr_women", nullable = false)
    private int bmrWomen;

    @Column(name = "diet_plan", nullable = false, columnDefinition = "TEXT")
    private String dietPlan;
 

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public int getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(int heightCm) {
        this.heightCm = heightCm;
    }

    public int getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(int weightKg) {
        this.weightKg = weightKg;
    }

    public String getRecommendedDiet() {
        return recommendedDiet;
    }

    public void setRecommendedDiet(String recommendedDiet) {
        this.recommendedDiet = recommendedDiet;
    }

    public int getIdealWeightKg() {
        return idealWeightKg;
    }

    public void setIdealWeightKg(int idealWeightKg) {
        this.idealWeightKg = idealWeightKg;
    }

    public int getBmrMen() {
        return bmrMen;
    }

    public void setBmrMen(int bmrMen) {
        this.bmrMen = bmrMen;
    }

    public int getBmrWomen() {
        return bmrWomen;
    }

    public void setBmrWomen(int bmrWomen) {
        this.bmrWomen = bmrWomen;
    }

    public String getDietPlan() {
        return dietPlan;
    }

    public void setDietPlan(String dietPlan) {
        this.dietPlan = dietPlan;
    }
}
