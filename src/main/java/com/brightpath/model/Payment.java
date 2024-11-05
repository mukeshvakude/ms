package com.brightpath.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "pending_price", nullable = false)
    private Double pendingprice = 0.0;

    @Column(name = "status", nullable = false)
    private String status; // e.g., "paid", "pending"

    @Column(name = "update_date", nullable = true)
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // Getters and setters
    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Double getPendingprice() {
        return pendingprice;
    }

    public void setPendingprice(Double pendingprice) {
        this.pendingprice = pendingprice;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    // Lifecycle callback to set updateDate before updating the entity
    @PreUpdate
    protected void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }
}
