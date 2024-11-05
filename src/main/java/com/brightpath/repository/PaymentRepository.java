package com.brightpath.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brightpath.model.Member;
import com.brightpath.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	 List<Payment> findByMember(Member member);
	 List<Payment> findByStatus(String status);
	
	  
}
