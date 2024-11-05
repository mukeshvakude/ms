var dietHtml;

$(document).ready(function() {
	// Initialize DataTable
	var membersTable = $('#membersTable').DataTable();

	// Function to format date
	function formatDate(dateString) {
		const options = { year: 'numeric', month: 'short', day: 'numeric' };
		const date = new Date(dateString);
		return date.toLocaleDateString('en-US', options).replace(/,/g, ''); // Format as '12 Sep 2024'
	}

	// Format join dates
	$('.join-date').each(function() {
		const originalDate = $(this).text(); // Get the original join date string
		const formattedDate = formatDate(originalDate); // Format the date
		$(this).text(formattedDate); // Update the cell with the formatted date
	});
	$('.last-date').each(function() {
		const originalDate = $(this).text(); // Get the original join date string
		const formattedDate = formatDate(originalDate); // Format the date
		$(this).text(formattedDate); // Update the cell with the formatted date
	});


	// Handle click event on View Details button
	$('#membersTable').on('click', '.btn-info', function() {
		const row = $(this).closest('tr');
		const memberId = row.find('td:nth-child(1)').text();
		const memberImage = row.find('td:nth-child(15)').text(); // Image URL from the hidden column
		const memberName = row.find('td:nth-child(2)').text() + ' ' + row.find('td:nth-child(3)').text(); // First Name + Last Name
		const memberMobile = row.find('td:nth-child(4)').text(); // Mobile Number
		const memberJoinDate = row.find('td:nth-child(5)').text(); // Join Date
		const memberLastDate = row.find('td:nth-child(6)').text(); // Last Date
		const memberPaymentStatus = row.find('td:nth-child(7)').text(); // Payment Status
		const memberDob = row.find('td:nth-child(8)').text(); // Date of Birth
		const memberAge = row.find('td:nth-child(9)').text(); // Age
		const memberGender = row.find('td:nth-child(10)').text(); // Gender
		const memberMembershipType = row.find('td:nth-child(11)').text(); // Membership Type
		const memberHeight = row.find('td:nth-child(12)').text(); // Height
		const memberWeight = row.find('td:nth-child(13)').text(); // Weight
		const memberBatchTiming = row.find('td:nth-child(14)').text(); // Batch Timing
		const totalAmount = parseFloat(row.find('td:nth-child(17)').text()); // Total Amount
		const pendingAmount = parseFloat(row.find('td:nth-child(18)').text()); // Total Amount Paid

		// Set member details in the modal
		$('#memberImage').attr('src', memberImage);
		$('#memberName').text(memberName);
		$('#memberMobile').text(memberMobile);
		$('#memberJoinDate').text(memberJoinDate);
		$('#memberLastDate').text(memberLastDate); // Set Last Date
		$('#memberGender').text(memberGender);
		$('#memberMembershipType').text(memberMembershipType);
		$('#memberHeight').text(memberHeight);
		$('#memberWeight').text(memberWeight);
		$('#memberBatchTiming').text(memberBatchTiming);
		$('#memberDob').text(memberDob); // Set Date of Birth
		$('#memberAge').text(memberAge); // Set Age
		$('#memberPaymentStatus').text(memberPaymentStatus); // Set Payment Status
		// Example of how to call this function when the modal closes


		const totalAmountHeaderId = `totalAmountPaidHeader_${memberId}`;
		$('#totalAmountPaidHeader').attr('id', totalAmountHeaderId); // Set the new ID
		const pendingAmountDisplayId = `pendingAmountDisplay_${memberId}`;
		$('#pendingAmountDisplay').attr('id', pendingAmountDisplayId); // Set the new ID
		const payButtonId = `payButton_${memberId}`;
		$('#payButton').attr('id', payButtonId); // Set the new ID
		const paymentModalId = `paymentHistoryModal_${memberId}`;
		$('#paymentHistoryModal').attr('id', paymentModalId); // Set the new ID

		const pendingAmountModalId = `pendingAmount_${memberId}`;
		$('#pendingAmount').attr('id', pendingAmountModalId); // Set the new ID

		const paymentHistoryButtonModalId = `paymentHistoryButton_${memberId}`;
		$('#paymentHistoryButton').attr('id', paymentHistoryButtonModalId); // Set the new ID
		
		const dietPlanButtonModalId = `dietPlan_${memberId}`;
		$('#dietPlan').attr('id', dietPlanButtonModalId); // Set the new ID
		
		const dietContentModalId = `dietContent_${memberId}`;
		$('#dietContent').attr('id', dietContentModalId); // Set the new ID
		
		
		
		


		$(`#${paymentHistoryButtonModalId}`).on('click', function() {
			// Update the total amount header
			$(`#${totalAmountHeaderId}`).text(totalAmount);

			// Display the pending amount below the total amount
			$(`#${pendingAmountDisplayId}`).text(pendingAmount);

			if (pendingAmount == 0) {
				$(`#${payButtonId}`).text('Paid'); // Change button text to "Paid"
				$(`#${payButtonId}`).prop('disabled', true); // Disable the button
				$(`#${payButtonId}`).removeClass('btn-danger').addClass('btn-success'); // Change button color to green
				$(`#${pendingAmountModalId}`).attr('disabled', true);
				$(`#${pendingAmountModalId}`).val(pendingAmount);
			} else {
				$(`#${payButtonId}`).text('Pay'); // Change button text to "Pay"
				$(`#${payButtonId}`).prop('disabled', false); // Enable the button
				$(`#${payButtonId}`).removeClass('btn-success').addClass('btn-danger'); // Change button color to red
				$(`#${pendingAmountModalId}`).attr('disabled', false);
				$(`#${pendingAmountModalId}`).val(pendingAmount);
			}
			$(`#${paymentModalId}`).modal('show');
		});
		// Function to show the modal and hide it after 5 seconds
		function showModal() {
			$("#paymentAlertModal").modal('show');

			// Automatically close the modal after 5 seconds
			setTimeout(function() {
				$("#paymentAlertModal").modal('hide');
			}, 5000);
		}


		$(`#${payButtonId}`).on('click', function() {
			const id = memberId;
			const pendingprice = $(`#${pendingAmountModalId}`).val();


			$.ajax({
				url: '/updatePayment', // The endpoint that will handle the payment
				type: 'POST',
				contentType: 'application/json',
				data: JSON.stringify({
					id: memberId,
					pendingprice: pendingprice
				}),
				success: function(response) {
					$("#paymentAlertMessage").text(response.message);
					$("#modalHeader").css("background-color", "#d4edda");
					$("#modalHeader").css("color", "#155724");
					showModal();

					// Hide the modal
					$('#paymentHistoryModal').modal('hide');
					//location.reload();


				}, error: function(xhr, status, error) {
					$("#alertMessage").text($("#errorMessage").text());
					$("#modalHeader").css("background-color", "#f8d7da");
					$("#modalHeader").css("color", "#721c24");
					showModal();
				}
			});

		});
		
		$(`#${dietPlanButtonModalId}`).on('click', function() {
		    const id = memberId;

		    $.ajax({
		        url: `/diet/view?memberId=${id}`, // Pass memberId as a query parameter
		        type: 'GET',
		        contentType: 'application/json',
		        success: function(data) {
					console.log(data);
		           
		            $(`#${dietContentModalId}`).innerHTML = ''; // Clear previous content
					if(data.id != null){
						var meals = parseDietPlan(data.dietPlan);
						$("#dietModalLabel").html(data.recommendedDiet);
						// Construct the diet plan HTML
						    dietHtml = `
						    
						       <table class="table">
						           <thead>
						               <tr>
						                   <th>Meal Time</th>
						                   <th>Food Items</th>
						                   
						               </tr>
						           </thead>
						           <tbody>
						               <tr>
						                   <td>Breakfast</td>
						                   <td>${meals.breakfast}</td>
						               </tr>
						               <tr>
						                   <td>Lunch</td>
						                   <td>${meals.lunch}</td>
						                  
						               </tr>
						               <tr>
						                   <td>Dinner</td>
						                   <td>${meals.dinner}</td>
						                  
						               </tr>
						               <tr>
						                   <td>Snacks</td>
						                   <td>${meals.snacks}</td>
						                   
						               </tr>
						           </tbody>
						       </table>
						   `;

						// Append the constructed HTML to the dietContent div
						$(`#${dietContentModalId}`).html(dietHtml);

						
						
					}else{
						$(`#${dietContentModalId}`).html("No Diet Found");
						
					}
		           
		        },
		        error: function(xhr, status, error) {
		            console.error('Error fetching diets:', error);
		        }
		    });
			
		});

		

		// Function to parse the diet plan string
		function parseDietPlan(dietPlan) {
		    const meals = {};
		    const mealParts = dietPlan.split('. ');

		    mealParts.forEach(part => {
		        const [mealType, ...mealItems] = part.split(': ');
		        if (mealItems.length > 0) {
		            meals[mealType.trim().toLowerCase()] = mealItems.join(': ').trim();
		        }
		    });
			console.log(meals);

		    return meals;
		}
		function resetModalIDs(memberId) {
			const originalIDs = {
				totalAmountHeader: "totalAmountPaidHeader",
				pendingAmountDisplay: "pendingAmountDisplay",
				payButton: "payButton",
				paymentHistoryModal: "paymentHistoryModal",
				pendingAmount: "pendingAmount",
				paymentHistoryButton: "paymentHistoryButton",
				dietPlanButton : "dietPlan",
				dietContent : "dietContent"
			};

			// Reset each ID based on the original values
			$(`#totalAmountPaidHeader_${memberId}`).attr('id', originalIDs.totalAmountHeader);
			$(`#pendingAmountDisplay_${memberId}`).attr('id', originalIDs.pendingAmountDisplay);
			$(`#payButton_${memberId}`).attr('id', originalIDs.payButton);
			$(`#paymentHistoryModal_${memberId}`).attr('id', originalIDs.paymentHistoryModal);
			$(`#pendingAmount_${memberId}`).attr('id', originalIDs.pendingAmount);
			$(`#paymentHistoryButton_${memberId}`).attr('id', originalIDs.paymentHistoryButton);
			$(`#dietPlan_${memberId}`).attr('id', originalIDs.dietPlanButton);
			$(`#dietContent_${memberId}`).attr('id', originalIDs.dietContent);
		}

		$('#memberDetailsModal').on('hidden.bs.modal', function() {
			resetModalIDs(memberId);
		});
		$('#paymentAlertModal').on('hidden.bs.modal', function() {
			location.reload();
		});

	});
	$('#membersTable tr').each(function() {
		const row = $(this).closest('tr');
		const memberLastDate = row.find('td:nth-child(6)').text(); // Last Date
		const memberPaymentStatus = row.find('td:nth-child(7)').text(); // Payment Status
		paymentStausAlertMessages(row, memberLastDate, memberPaymentStatus);


	});

	function paymentStausAlertMessages(row, lastDateText, paymentStatusText) {
		console.log("executing");
		const lastDate = new Date(lastDateText);
		const today = new Date();

		// Calculate the difference in days
		const timeDiff = lastDate - today;
		const remainingDays = Math.ceil(timeDiff / (1000 * 3600 * 24)); // Convert milliseconds to days

		// Check payment status and remaining days
		if (paymentStatusText === "pending") {
			if (remainingDays < 10 && remainingDays >= 0) {
				row.find('td:nth-child(7)').html("<span class='highlight'>Membership  expiring  soon!</span>");
				row.addClass('text-danger'); // Add a warning style
			} else if (remainingDays < 0) {
				row.find('td:nth-child(7)').html(" <span class='highlight'>Membership expired! </span>");
				row.find('td:nth-child(7)').addClass('text-danger'); // Add a warning style
			}
		} else if (paymentStatusText === "paid") {
			if (remainingDays < 10 && remainingDays >= 0) {
				row.find('td:nth-child(7)').html("<span class='highlight'> Membership expiring soon!</span>");
				row.addClass('text-success'); // Add a warning style
			}
			else if (remainingDays < 0) {
				row.find('td:nth-child(7)').html("<span class='highlight'> Membership expired!</span>");
				row.find('td:nth-child(7)').addClass('text-success'); // Add a warning style
			}
		}
	}






	$('#applyFilters').click(function() {
		// Get the selected values from the filters
		const memberType = $('#memberTypeFilter').val();
		const paymentsStatus = $('#paymentStatusFilter').val();
		const joinDateFrom = $('#joinDateFrom').val();
		const joinDateTo = $('#joinDateTo').val();

		// Show all rows initially
		$('#membersTable tbody tr').show();

		// Filter by Member Type
		if (memberType !== 'all') {
			$('#membersTable tbody tr').filter(function() {
				return $(this).find('td:nth-child(11)').text().toLowerCase() !== memberType;
			}).hide();
		}

		// Filter by Membership Status
		if (paymentsStatus !== 'all') {
			$('#membersTable tbody tr').filter(function() {
				const paymentCurrentStatus = $(this).find('td:nth-child(7)').text().toLowerCase();
				return (paymentsStatus === 'paid' && paymentCurrentStatus !== 'paid') ||
					(paymentsStatus === 'pending' && paymentCurrentStatus !== 'pending');
			}).hide();
		}

		// Filter by Join Date Range
		if (joinDateFrom || joinDateTo) {
			$('#membersTable tbody tr').filter(function() {
				const joinDate = new Date($(this).find('.join-date').text());
				const fromDate = new Date(joinDateFrom);
				const toDate = new Date(joinDateTo);
				return (joinDateFrom && joinDate < fromDate) || (joinDateTo && joinDate > toDate);
			}).hide();
		}
	});







	// Function to get URL parameters
	function getUrlParameter(name) {
		const urlParams = new URLSearchParams(window.location.search);
		return urlParams.get(name) || '';
	}

		// Get URL parameters and set defaults
		const memberType = getUrlParameter('memberType') || 'personal';  // Default to 'personal'
		const paymentStatus = getUrlParameter('paymentStatus') || 'all'; // Default to 'all'
		const joinDateFrom = getUrlParameter('joinDateFrom') || '';       // Default to empty
		const joinDateTo = getUrlParameter('joinDateTo') || '';           // Default to empty

		// Set the form fields based on the URL parameters
		document.getElementById('memberTypeFilter').value = memberType;
		document.getElementById('paymentStatusFilter').value = paymentStatus;
		document.getElementById('joinDateFrom').value = joinDateFrom;
		document.getElementById('joinDateTo').value = joinDateTo;
		console.log("Executing...load");
	
		



});



