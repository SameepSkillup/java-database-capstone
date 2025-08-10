# User Story Template

**Title:**
_As a [user role], I want [feature/goal], so that [reason]._

**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]

**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Additional information or edge cases]
# User Stories – Admin Role

## User Story 1 – Admin Login
**Title:**  
_As an admin, I want to log into the portal with my username and password, so that I can manage the platform securely._

**Acceptance Criteria:**  
1. The system validates the admin’s username and password.  
2. Successful login redirects the admin to the dashboard.  
3. Failed login attempts display an error message without revealing sensitive details.  

**Priority:** High  
**Story Points:** 3  

**Notes:**  
- Use secure password storage (e.g., hashed passwords).  
- Implement protection against brute-force login attempts.  

---

## User Story 2 – Admin Logout
**Title:**  
_As an admin, I want to log out of the portal, so that I can protect system access when I’m done using it._

**Acceptance Criteria:**  
1. Clicking “Logout” ends the admin session immediately.  
2. The system redirects the admin to the login page.  
3. Session cookies or tokens are invalidated.  

**Priority:** High  
**Story Points:** 2  

**Notes:**  
- Ensure logout works across all open browser tabs/windows.  

---

## User Story 3 – Add Doctors
**Title:**  
_As an admin, I want to add doctors to the portal, so that they can be part of the platform and provide services._

**Acceptance Criteria:**  
1. The system allows the admin to fill in doctor details (name, specialization, contact, etc.).  
2. Doctor profiles are saved in the database.  
3. The system confirms when the doctor has been successfully added.  

**Priority:** High  
**Story Points:** 5  

**Notes:**  
- Include form validation for required fields.  

---

## User Story 4 – Delete Doctor Profile
**Title:**  
_As an admin, I want to delete a doctor’s profile from the portal, so that I can remove outdated or incorrect records._

**Acceptance Criteria:**  
1. The system prompts the admin for confirmation before deleting.  
2. Deleted doctor profiles are removed from the database.  
3. The system shows a success message after deletion.  

**Priority:** Medium  
**Story Points:** 3  

**Notes:**  
- Consider soft delete (marking as inactive) instead of permanent removal.  

---

## User Story 5 – Run Stored Procedure for Appointments
**Title:**  
_As an admin, I want to run a stored procedure in MySQL CLI to get the number of appointments per month, so that I can track usage statistics._

**Acceptance Criteria:**  
1. The stored procedure returns the total number of appointments grouped by month.  
2. Results are accurate and based on the appointments database table.  
3. The admin can access and execute this procedure from the CLI.  

**Priority:** Medium  
**Story Points:** 4  

**Notes:**  
- Ensure the stored procedure is secure and optimized for performance.  
