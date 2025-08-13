// Import the base API URL from the config file
import { API_BASE_URL } from "../config/config.js";

// Define the base endpoint for all patient-related actions
const PATIENT_API = `${API_BASE_URL}/patient`;

/**
 * Function: patientSignup
 * Purpose: Create a new patient in the database
 * @param {Object} data - Patient details (name, email, password, etc.)
 */
export async function patientSignup(data) {
  try {
    const response = await fetch(PATIENT_API, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    });

    const result = await response.json();

    if (!response.ok) {
      throw new Error(result.message);
    }

    return { success: true, message: result.message };
  } catch (error) {
    console.error("Error :: patientSignup ::", error);
    return { success: false, message: error.message || "Signup failed" };
  }
}

/**
 * Function: patientLogin
 * Purpose: Log in an existing patient
 * @param {Object} data - Login credentials (email, password)
 */
export async function patientLogin(data) {
  try {
    const response = await fetch(`${PATIENT_API}/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    });

    return response; // Let caller handle token extraction & status check
  } catch (error) {
    console.error("Error :: patientLogin ::", error);
    throw error; // Let UI handle login failures
  }
}

/**
 * Function: getPatientData
 * Purpose: Retrieve patient profile data using their token
 * @param {string} token - Authentication token
 */
export async function getPatientData(token) {
  try {
    const response = await fetch(`${PATIENT_API}/${token}`);
    const data = await response.json();

    if (response.ok) return data.patient;
    return null;
  } catch (error) {
    console.error("Error fetching patient details:", error);
    return null;
  }
}

/**
 * Function: getPatientAppointments
 * Purpose: Fetch patient appointments (used in both doctor & patient dashboards)
 * @param {string} id - Patient ID
 * @param {string} token - Authentication token
 * @param {string} user - Role of requester ("patient" or "doctor")
 */
export async function getPatientAppointments(id, token, user) {
  try {
    const response = await fetch(`${PATIENT_API}/${id}/${user}/${token}`);
    const data = await response.json();

    if (response.ok) {
      return data.appointments || [];
    }
    return null;
  } catch (error) {
    console.error("Error fetching patient appointments:", error);
    return null;
  }
}

/**
 * Function: filterAppointments
 * Purpose: Fetch filtered appointment records
 * @param {string} condition - Appointment condition ("pending", "consulted", etc.)
 * @param {string} name - Doctor or patient name
 * @param {string} token - Authentication token
 */
export async function filterAppointments(condition, name, token) {
  try {
    const response = await fetch(
      `${PATIENT_API}/filter/${encodeURIComponent(condition)}/${encodeURIComponent(name)}/${token}`,
      { method: "GET", headers: { "Content-Type": "application/json" } }
    );

    if (response.ok) {
      return await response.json();
    } else {
      console.error("Failed to filter appointments:", response.statusText);
      return { appointments: [] };
    }
  } catch (error) {
    console.error("Error filtering appointments:", error);
    alert("Something went wrong!");
    return { appointments: [] };
  }
}
