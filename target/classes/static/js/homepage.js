// script.js


$(document).ready(function() {
    // Function to show the modal and hide it after 5 seconds
    function showModal() {
        $("#alertModal").modal('show');

        // Automatically close the modal after 5 seconds
        setTimeout(function() {
            $("#alertModal").modal('hide');
        }, 5000);
    }

    // Check for error message and show modal
    if ($("#errorMessage").text().trim() !== "") {
        $("#alertMessage").text($("#errorMessage").text());
        $("#modalHeader").css("background-color", "#f8d7da");
        $("#modalHeader").css("color", "#721c24");
        showModal();
    }
    // Check for success message and show modal
    else if ($("#successMessage").text().trim() !== "") {
        $("#alertMessage").text($("#successMessage").text());
        $("#modalHeader").css("background-color", "#d4edda");
        $("#modalHeader").css("color", "#155724");
        showModal();
    }
	
	const membershipDuration = document.getElementById("membershipDuration");
	    const price = document.getElementById("price");

	    // Check if the selected value is 1 month
	    if (membershipDuration.value === "1") {
	        price.value = 700; // Set price to 600 for 1 month
	    } else {
	        price.value = ""; // Clear price if the condition is not met
	    }
		
		// Set the maximum date for the join date input to today
		    const today = new Date();
		    const dd = String(today.getDate()).padStart(2, '0');
		    const mm = String(today.getMonth() + 1).padStart(2, '0'); // January is 0!
		    const yyyy = today.getFullYear();
		    
		    const maxDate = `${yyyy}-${mm}-${dd}`;
		    document.getElementById("joinDate").setAttribute("max", maxDate);
});





document.getElementById('mobileNumber').addEventListener('input', function (e) {
            this.value = this.value.replace(/[^0-9]/g, '');
});
document.getElementById('mobileSignIn').addEventListener('input', function (e) {
							            this.value = this.value.replace(/[^0-9]/g, '');
});
		
		



	



		
// Function to calculate age based on DOB
    function calculateAge() {
        const dobInput = document.getElementById("dob");
        const ageInput = document.getElementById("age");
        const dob = new Date(dobInput.value);
        const today = new Date();

        if (dob) {
            let age = today.getFullYear() - dob.getFullYear();
            const monthDiff = today.getMonth() - dob.getMonth();
            if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < dob.getDate())) {
                age--;
            }
            ageInput.value = age;
        } else {
            ageInput.value = "";
        }
    }
	
	// Form validation for mobile number
	    function validateForm() {
	        const mobileInput = document.getElementById("mobileNumber");
	        const mobilePattern = /^[0-9]{10}$/; // Regex for 10-digit mobile number

	        if (!mobilePattern.test(mobileInput.value)) {
	            alert("Please enter a valid 10-digit mobile number.");
	            return false; // Prevent form submission
	        }
	        return true; // Allow form submission
	    }
		function updatePrice() {
		        const membershipType = document.getElementById('membershipType').value;
		        const membershipDuration = parseInt(document.getElementById('membershipDuration').value);
		        let price = 0;

		        if (membershipType === 'regular') {
		            if (membershipDuration === 1) price = 700;
		            else if (membershipDuration === 3) price = 1500;
		            else if (membershipDuration === 6) price = 3000;
		            else if (membershipDuration === 12) price = 5000;
		        } else if (membershipType === 'personal') {
		            if (membershipDuration === 1) price = 5000;
		            else if (membershipDuration === 3) price = 12000;
		            else if (membershipDuration === 6) price = 20000;
		            else if (membershipDuration === 12) price = 30000;
		        }

		        document.getElementById('price').value = price;
		    }

			function calculateLastDate() {
			    const joinDateInput = document.getElementById("joinDate");
			    const membershipDuration = document.getElementById("membershipDuration");
			    const lastDateInput = document.getElementById("lastDate");

			    const joinDate = new Date(joinDateInput.value);
			    let lastDate;

			    // Calculate last date based on membership duration
			    if (membershipDuration.value) {
			        const duration = parseInt(membershipDuration.value, 10);
			        switch (duration) {
			            case 1:
			                lastDate = new Date(joinDate);
			                lastDate.setMonth(joinDate.getMonth() + 1);
			                break;
			            case 3:
			                lastDate = new Date(joinDate);
			                lastDate.setMonth(joinDate.getMonth() + 3);
			                break;
			            case 6:
			                lastDate = new Date(joinDate);
			                lastDate.setMonth(joinDate.getMonth() + 6);
			                break;
			            case 12:
			                lastDate = new Date(joinDate);
			                lastDate.setFullYear(joinDate.getFullYear() + 1);
			                break;
			            default:
			                lastDate = null; // In case of invalid duration
			        }
			    }

			    // Format the last date as DD-MM-YYYY
			    if (lastDate) {
			        const day = String(lastDate.getDate()).padStart(2, '0');
			        const month = String(lastDate.getMonth() + 1).padStart(2, '0'); // Months are zero-based
			        const year = lastDate.getFullYear();
			        lastDateInput.value = `${day}-${month}-${year}`;
			    } else {
			        lastDateInput.value = ''; // Clear the last date if not set
			    }
			}
