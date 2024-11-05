package com.brightpath.controller;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.brightpath.model.Diet;
import com.brightpath.model.Member;
import com.brightpath.model.Payment;
import com.brightpath.repository.MemberRepository;
import com.brightpath.repository.PaymentRepository;
import com.brightpath.repository.RolesRepository;
import com.brightpath.services.DietService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private RolesRepository rolesRepository; 
    
    @Autowired
    private DietService dietService; 
    
    

    private final String uploadDir = "static/images"; // Uploads directory
    
    @Value("${gym.image.path}")
	private String gymImagePath;
    

    @GetMapping("/members")
    public String listMembers(Model model, HttpSession session) {
        List<Member> members = memberRepository.findAll();
        
        // Iterate through each member and update payment status based on pending price
        for (Member member : members) {
            for (Payment payment : member.getPayments()) {
                if (payment.getPendingprice() > 0) {
                    payment.setStatus("pending");
                } else {
                    payment.setStatus("paid");
                }
             // Fetch diet plans based on member's height, weight, and age
                List<Diet> recommendedDiets = dietService.getRecommendedDiets(member.getHeight(), member.getWeight(), member.getAge());
                for (Diet diet : recommendedDiets) {
                   
                    member.setDiet(diet); // Add the diet to the member's list
                    memberRepository.save(member);
                }
               
                // Optionally, save the payment to the database if needed
                 paymentRepository.save(payment); // Uncomment if you want to save changes
            }
        }
        
        getAllCollection(model,members);

        // Fetch username from session
        String username = (String) session.getAttribute("username");
        String mobileNumber = (String) session.getAttribute("mobileNumber");
        if (username != null) {
            model.addAttribute("username", username);
            if(mobileNumber != null) {
                Member member = memberRepository.findByMobileNumber(mobileNumber);
                model.addAttribute("user", member);
            }
        } else {
            // Redirect to login/sign-in page if the session is missing
            return "redirect:/";
        }
        
       

        return "members-list"; // Thymeleaf template
    }


    

    @PostMapping("/add-member")
    public String addMember(@ModelAttribute Member member, 
                            @RequestParam("image") MultipartFile file, 
                            @RequestParam int membershipDuration,
                            RedirectAttributes redirectAttributes) {
        try {
            // Check if a member with the same mobile number already exists
            Member existingMember = memberRepository.findByMobileNumber(member.getMobileNumber());
            if (existingMember != null) {
                redirectAttributes.addFlashAttribute("errorMessage", "User already a member of the gym!");
                System.out.println("User already a member of the gym!");
                return "redirect:/";  // Redirect to home with error message
            }

            String contentType = file.getContentType();
            if (file != null && !file.isEmpty() && isValidImage(contentType)) {
                System.out.println("Valid image file received");

                // Handle existing image deletion logic
                String oldImageUrl = member.getImageUrl();
                if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                    String oldImageFileName = oldImageUrl.substring(oldImageUrl.lastIndexOf("/") + 1);
                    Path oldImagePath = Paths.get(uploadDir, oldImageFileName);

                    // Delete the existing image if it exists
                    if (Files.exists(oldImagePath)) {
                        Files.delete(oldImagePath);
                        System.out.println("Old image deleted: " + oldImageFileName);
                    }
                }

                // Upload the new image
                String fileName = file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.createDirectories(Paths.get(uploadDir));
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                member.setImageUrl("/images/" + fileName);
                member.setLastDate(convertToYYYYMMDD(member.getLastDate()));
                
                // Calculate price based on membership type and duration
                double price = calculatePrice(member.getMembershipType(), membershipDuration);

                // Create a new payment instance
                Payment payment = new Payment();
                payment.setPrice(price);
                payment.setPendingprice(price);
                payment.setStatus("pending"); // Set to "paid" or "pending" based on your logic
                payment.setMember(member); // Link payment to the member

                
             // Fetch diet plans based on member's height, weight, and age
                List<Diet> recommendedDiets = dietService.getRecommendedDiets(member.getHeight(), member.getWeight(), member.getAge());
                for (Diet diet : recommendedDiets) {
                   
                    member.setDiet(diet); // Add the diet to the member's list
                }
                // Save the member and associated payment
                memberRepository.save(member); // Save member first to get the ID for the payment
                paymentRepository.save(payment); // Save the payment directly

                System.out.println("Member saved with new image URL: /uploads/" + fileName);
                redirectAttributes.addFlashAttribute("message", "Member added successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid image format. Supported formats are: jpg, jpeg, png, gif, bmp, svg.");
            }
        } catch (Exception e) {
            System.out.println("Error while processing image: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while uploading the image.");
        }

        return "redirect:/"; // Redirect after processing
    }

    // Helper method to calculate price based on membership type and duration
    private double calculatePrice(String membershipType, int membershipDuration) {
        if ("regular".equals(membershipType)) {
            switch (membershipDuration) {
                case 1: return 700;
                case 3: return 1500;
                case 6: return 3000;
                case 12: return 5000;
                default: return 0;
            }
        } else if ("personal".equals(membershipType)) {
            switch (membershipDuration) {
                case 1: return 5000;
                case 3: return 12000;
                case 6: return 20000;
                case 12: return 30000;
                default: return 0;
            }
        }
        return 0; // Return 0 if no valid type or duration is found
    }


    private boolean isValidImage(String contentType) {
        return contentType.equals("image/jpeg") || 
               contentType.equals("image/png") || 
               contentType.equals("image/gif") || 
               contentType.equals("image/bmp") || 
               contentType.equals("image/svg+xml");
    }
    
    
    
    @PostMapping("/signIn")
    public String signIn(@RequestParam("mobileSignIn") String mobileNumber, Model model, RedirectAttributes redirectAttributes,HttpSession session) {
        
        // Validate the mobile number format
        if (mobileNumber.length() != 10 || !mobileNumber.matches("\\d{10}")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please enter a valid 10-digit mobile number.");
            return "redirect:/"; // Redirect back to the sign-in page if invalid
        }

        
        // Check if the mobile number exists in the member table
        Member member = checkUserValidity(mobileNumber);
        
        
        if (member != null) {
        	 boolean isAdmin = rolesRepository.existsByAdminAndActive(member.getId() , true);
        	 if(!isAdmin) {
        		 redirectAttributes.addFlashAttribute("errorMessage", "Unauthorized member..!");
                 return "redirect:/"; 
        	 }else {
            // Store the mobile number (or username) in the session
        	session.setAttribute("username", member.getFirstName() + " " + member.getLastName());
        	session.setAttribute("mobileNumber", member.getMobileNumber());

            // Redirect to the members list on successful sign-in
            return "redirect:/members"; 
        	 }
        } else {
            // Add an error message to redirect attributes and return to the sign-in page if invalid
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid mobile number! Please Sign up.");
            return "redirect:/"; 
        }
    }


    // Method to check user validity
    private Member checkUserValidity(String mobileNumber) {
        // Check if a member with the given mobile number exists
        return memberRepository.findByMobileNumber(mobileNumber);
    }
    
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate the session
        return "redirect:/"; // Redirect to sign-in page
    }

    
    
    public static String convertToYYYYMMDD(String dateStr) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = "";

        try {
            Date date = inputFormat.parse(dateStr);
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }
    










    @GetMapping("/filterMembers")
    public String filterMembers(
            @RequestParam(required = false) String memberType,
            @RequestParam(required = false) String paymentStatus,
            @RequestParam(required = false) String joinDateFrom,
            @RequestParam(required = false) String joinDateTo,
            Model model, HttpSession session) {

        // Fetch username from session
        String username = (String) session.getAttribute("username");
        String mobileNumber = (String) session.getAttribute("mobileNumber");
        if (username != null) {
            model.addAttribute("username", username);
            if (mobileNumber != null) {
                Member member = memberRepository.findByMobileNumber(mobileNumber);
                model.addAttribute("user", member);
            }
        } else {
            // Redirect to login/sign-in page if the session is missing
            return "redirect:/";
        }

        // Initialize the member list
        List<Member> members = memberRepository.findAll();

        // Filter by payment status if specified
        if (paymentStatus != null && !"all".equalsIgnoreCase(paymentStatus)) {
            members = members.stream()
                    .filter(member -> member.getPayments().stream()
                            .anyMatch(payment -> paymentStatus.equalsIgnoreCase(payment.getStatus())))
                    .collect(Collectors.toList());
        }

        // Filter by membership type if specified
        if (memberType != null && !"all".equalsIgnoreCase(memberType)) {
            members = members.stream()
                    .filter(member -> memberType.equalsIgnoreCase(member.getMembershipType()))
                    .collect(Collectors.toList());
        }

        // Filter by join date range if specified
        if (joinDateFrom != null && !joinDateFrom.isEmpty() && joinDateTo != null && !joinDateTo.isEmpty()) {
            members = members.stream()
                    .filter(member -> member.getJoinDate().compareTo(joinDateFrom) >= 0 &&
                                      member.getJoinDate().compareTo(joinDateTo) <= 0)
                    .collect(Collectors.toList());
        } else if (joinDateFrom != null && !joinDateFrom.isEmpty()) {
            members = members.stream()
                    .filter(member -> member.getJoinDate().compareTo(joinDateFrom) >= 0)
                    .collect(Collectors.toList());
        } else if (joinDateTo != null && !joinDateTo.isEmpty()) {
            members = members.stream()
                    .filter(member -> member.getJoinDate().compareTo(joinDateTo) <= 0)
                    .collect(Collectors.toList());
        }

        getAllCollection(model, members);

        return "members-list"; // Your view for displaying members
    }


    void getAllCollection( Model model ,List<Member> members) {
    	// Calculate total paid amount and pending amount, and count members
        double totalPaidAmount = 0.0;
        double totalPendingAmount = 0.0;
        int paidMembersCount = 0;
        int pendingMembersCount = 0;

        for (Member member : members) {
            for (Payment payment : member.getPayments()) {
                if ("paid".equalsIgnoreCase(payment.getStatus())) {
                    totalPaidAmount += payment.getPrice();
                    paidMembersCount++;
                } else if ("pending".equalsIgnoreCase(payment.getStatus())) {
                    totalPendingAmount += payment.getPendingprice();
                    pendingMembersCount++;
                }
            }
        }

        // Add calculated values to the model
        model.addAttribute("members", members);
        model.addAttribute("totalPaidAmount", totalPaidAmount);
        model.addAttribute("totalPendingAmount", totalPendingAmount);
        model.addAttribute("paidMembersCount", paidMembersCount);
        model.addAttribute("pendingMembersCount", pendingMembersCount);
        model.addAttribute("gymImagePath", gymImagePath);
    }
    
   

    
}

