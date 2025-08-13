// =======================
// Patient Dashboard - Viewing & Filtering Doctors
// =======================

// Imports
import { createDoctorCard } from "./components/doctorCard.js";
import { openModal } from "./components/modals.js";
import { getDoctors, filterDoctors } from "./services/doctorServices.js";
import { patientSignup, patientLogin } from "./services/patientServices.js";

// =======================
// Page Load
// =======================
document.addEventListener("DOMContentLoaded", () => {
  // Load all doctors initially
  loadDoctorCards();

  // Bind Signup Modal
  const signupBtn = document.getElementById("patientSignup");
  if (signupBtn) {
    signupBtn.addEventListener("click", () => openModal("patientSignup"));
  }

  // Bind Login Modal
  const loginBtn = document.getElementById("patientLogin");
  if (loginBtn) {
    loginBtn.addEventListener("click", () => openModal("patientLogin"));
  }

  // Bind filters
  const searchInput = document.getElementById("searchBar");
  const timeFilter = document.getElementById("filterTime");
  const specialtyFilter = document.getElementById("filterSpecialty");

  if (searchInput) searchInput.addEventListener("input", filterDoctorsOnChange);
  if (timeFilter) timeFilter.addEventListener("change", filterDoctorsOnChange);
  if (specialtyFilter) specialtyFilter.addEventListener("change", filterDoctorsOnChange);
});

// =======================
// Load All Doctors
// =======================
async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (error) {
    console.error("Failed to load doctors:", error);
    document.getElementById("content").innerHTML =
      "<p>❌ Failed to load doctors. Please try again later.</p>";
  }
}

// =======================
// Filter Doctors
// =======================
async function filterDoctorsOnChange() {
  const name = document.getElementById("searchBar")?.value.trim() || null;
  const time = document.getElementById("filterTime")?.value || null;
  const specialty = document.getElementById("filterSpecialty")?.value || null;

  try {
    const response = await filterDoctors(name, time, specialty);
    if (response.doctors && response.doctors.length > 0) {
      renderDoctorCards(response.doctors);
    } else {
      document.getElementById("content").innerHTML =
        "<p>No doctors found with the given filters.</p>";
    }
  } catch (error) {
    console.error("Failed to filter doctors:", error);
    document.getElementById("content").innerHTML =
      "<p>❌ Error filtering doctors. Please try again later.</p>";
  }
}

// =======================
// Render Utility
// =======================
function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById("content");
  contentDiv.innerHTML = "";
  doctors.forEach((doctor) => {
    const card = createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}

// =======================
// Patient Signup
// =======================
window.signupPatient = async function () {
  try {
    const data = {
      name: document.getElementById("name")?.value,
      email: document.getElementById("email")?.value,
      password: document.getElementById("password")?.value,
      phone: document.getElementById("phone")?.value,
      address: document.getElementById("address")?.value,
    };

    const { success, message } = await patientSignup(data);
    if (success) {
      alert(message);
      document.getElementById("modal").style.display = "none";
      window.location.reload();
    } else {
      alert(message);
    }
  } catch (error) {
    console.error("Signup failed:", error);
    alert("❌ An error occurred while signing up.");
  }
};

// =======================
// Patient Login
// =======================
window.loginPatient = async function () {
  try {
    const data = {
      email: document.getElementById("email")?.value,
      password: document.getElementById("password")?.value,
    };

    const response = await patientLogin(data);
    if (response.ok) {
      const result = await response.json();
      localStorage.setItem("token", result.token);
      selectRole("loggedPatient");
      window.location.href = "/pages/loggedPatientDashboard.html";
    } else {
      alert("❌ Invalid credentials!");
    }
  } catch (error) {
    console.error("Error logging in:", error);
    alert("❌ Failed to login. Please try again later.");
  }
};
