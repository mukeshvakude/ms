package com.brightpath.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.brightpath.model.Member;


public interface MemberRepository extends JpaRepository<Member, Long> {
  
	// Custom query to find by mobile number
    Member findByMobileNumber(String mobileNumber);
 // Custom query method to find members by joinDate and lastDate
    List<Member> findByJoinDateBetween(String joinDate, String lastDate);
    
 // Custom query method to find members by joinDate and lastDate
    List<Member> findByMembershipType(String membershipType);
    
   
    @Query("SELECT m FROM Member m WHERE m.joinDate >= :joinDateFrom")
    List<Member> findByJoinDateGreaterThanEqual(@Param("joinDateFrom") String joinDateFrom);

    @Query("SELECT m FROM Member m WHERE m.lastDate <= :joinDateTo")
    List<Member> findByJoinDateLessThanEqual(@Param("joinDateTo") String joinDateTo);
    
 // Custom query to find members by payment status
    @Query("SELECT m FROM Member m LEFT JOIN m.payments p WHERE p.status = :status")
    List<Member> findByPaymentStatus(String status);
    
    


}
