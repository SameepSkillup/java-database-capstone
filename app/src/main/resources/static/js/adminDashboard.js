// =======================
// Admin Dashboard - Managing Doctors
// =======================

// Imports
import { openModal } from "./components/modals.js";
import { createDoctorCard } from "./components/doctorCard.js";
import { getDoctors, filterDoctors, saveDoctor } from "./services/doctorServices.js";

// =======================
// Event Binding
// =======================

// Open Add Doctor Modal
document.getElementById("addDocBtn").addEventListener("click", () => {
  openModal("addDoctor");
});

// Load all doctors on page load
document.addEventListener("DOMContentLoaded", () => {
  loadDoctorCards();
});

// =======================
// Load and Render Doctors
// =======================
async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (error) {
    console.error("Error loading doctors:", error);
  }
}

function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById("content");
  contentDiv.innerHTML = "";

  if (!doctors || doctors.length === 0) {
    contentDiv.textContent = "No doctors found.";
    return;
  }

  doctors.forEach((doctor) => {
    const card = createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}

// =======================
// Search & Filter
// =======================
document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
document.getElementById("filterTime").addEventListener("change", filterDoctorsOnChange);
document.getElementById("filterSpecialty").addEventListener("change", filterDoctorsOnChange);

async function filterDoctorsOnChange() {
  try {
    const name = document.getElementById("searchBar").value || null;
    const time = document.getElementById("filterTime").value || null;
    const specialty = document.getElementById("filterSpecialty").value || null;

    const filteredData = await filterDoctors(name, time, specialty);

    if (filteredData && filteredData.doctors && filteredData.doctors.length > 0) {
      renderDoctorCards(filteredData.doctors);
    } else {
      document.getElementById("content").innerHTML = "No doctors found with the given filters.";
    }
  } catch (error) {
    console.error("Error filtering doctors:", error);
    alert("An error occurred while filtering doctors.");
  }
}

// =======================
// Add Doctor Functionality
// =======================
export async function adminAddDoctor() {
  const name = document.getElementById("doctorName").value;
  const email = document.getElementById("doctorEmail").value;
  const phone = document.getElementById("doctorPhone").value;
  const password = document.getElementById("doctorPassword").value;
  const specialty = document.getElementById("doctorSpecialty").value;

  // Get availability times from checked checkboxes
  const availabilityCheckboxes = document.querySelectorAll('input[name="availability"]:checked');
  const availableTimes = Array.from(availabilityCheckboxes).map((cb) => cb.value);

  const token = localStorage.getItem("token");
  if (!token) {
    alert("You must be logged in as an admin to add a doctor.");
    return;
  }

  const doctor = { name, email, phone, password, specialty, availableTimes };

  try {
    const result = await saveDoctor(doctor, token);
    if (result.success) {
      alert(result.message || "Doctor added successfully!");
      document.getElementById("addDoctorModal").close();
      loadDoctorCards();
    } else {
      alert(result.message || "Failed to add doctor.");
    }
  } catch (error) {
    console.error("Error adding doctor:", error);
    alert("An error occurred while adding the doctor.");
  }
}
