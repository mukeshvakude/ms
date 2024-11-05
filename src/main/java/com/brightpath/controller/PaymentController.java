package com.brightpath.controller;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import com.brightpath.model.Member;
import com.brightpath.model.Payment;
import com.brightpath.model.PaymentRequest;
import com.brightpath.repository.MemberRepository;
import com.brightpath.repository.PaymentRepository;

@Controller
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MemberRepository memberRepository; // Assuming Member entity and repository exist

    @GetMapping("/payments")
    public String listPayments(Model model) {
        model.addAttribute("payments", paymentRepository.findAll());
        model.addAttribute("members", memberRepository.findAll());
        return "payments";
    }

    @PostMapping("/payments")
    public String makePayment(@ModelAttribute Payment payment) {
        paymentRepository.save(payment);
        return "redirect:/payments";
    }
    @PostMapping("/updatePayment")
    public ResponseEntity<Map<String, Object>> updatePayment(@RequestBody PaymentRequest paymentRequest) {
        Map<String, Object> response = new HashMap<>();
        
        // Retrieve the member by memberId
        Member member = memberRepository.findById(paymentRequest.getId()).orElse(null);

        if (member == null) {
            response.put("message", "Member not found.");
            return ResponseEntity.ok(response);
        }

        // Find the payment record for this member
        List<Payment> payments = paymentRepository.findByMember(member);

        if (payments.isEmpty()) {
            response.put("message", "No payment records found.");
            return ResponseEntity.ok(response);
        }

        // Update the first payment record found
        Payment payment = payments.get(0);
        payment.setPendingprice(payment.getPendingprice() - paymentRequest.getPendingprice());
        
        if(payment.getPendingprice() == 0) {
            payment.setStatus("paid");
        }
     // Set today's date as the update date
        payment.setUpdateDate(LocalDateTime.now());
        paymentRepository.save(payment);

        // Return updated data
        response.put("message", "Payment updated successfully.");
        response.put("updatedPendingAmount", payment.getPendingprice());
        response.put("updatedPrice", payment.getPrice());
        response.put("updatedTotalAmount", payment.getPrice() - payment.getPendingprice());

        
        return ResponseEntity.ok(response);
    }


}
