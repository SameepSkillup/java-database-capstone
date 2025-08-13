/*
  Import the base API URL from the config file
  Define a constant DOCTOR_API to hold the full endpoint for doctor-related actions


  Function: getDoctors
  Purpose: Fetch the list of all doctors from the API

   Use fetch() to send a GET request to the DOCTOR_API endpoint
   Convert the response to JSON
   Return the 'doctors' array from the response
   If there's an error (e.g., network issue), log it and return an empty array


  Function: deleteDoctor
  Purpose: Delete a specific doctor using their ID and an authentication token

   Use fetch() with the DELETE method
    - The URL includes the doctor ID and token as path parameters
   Convert the response to JSON
   Return an object with:
    - success: true if deletion was successful
    - message: message from the server
   If an error occurs, log it and return a default failure response


  Function: saveDoctor
  Purpose: Save (create) a new doctor using a POST request

   Use fetch() with the POST method
    - URL includes the token in the path
    - Set headers to specify JSON content type
    - Convert the doctor object to JSON in the request body

   Parse the JSON response and return:
    - success: whether the request succeeded
    - message: from the server

   Catch and log errors
    - Return a failure response if an error occurs


  Function: filterDoctors
  Purpose: Fetch doctors based on filtering criteria (name, time, and specialty)

   Use fetch() with the GET method
    - Include the name, time, and specialty as URL path parameters
   Check if the response is OK
    - If yes, parse and return the doctor data
    - If no, log the error and return an object with an empty 'doctors' array

   Catch any other errors, alert the user, and return a default empty result
*/
// Import the base API URL from the config file
import { API_BASE_URL } from "../config/config.js";

// Define a constant DOCTOR_API to hold the full endpoint for doctor-related actions
const DOCTOR_API = `${API_BASE_URL}/doctor`;

/**
 * Function: getDoctors
 * Purpose: Fetch the list of all doctors from the API
 */
export async function getDoctors() {
  try {
    const response = await fetch(DOCTOR_API, { method: "GET" });
    const data = await response.json();
    return data.doctors || []; // return the doctors array
  } catch (error) {
    console.error("Error fetching doctors:", error);
    return []; // return empty array on failure
  }
}

/**
 * Function: deleteDoctor
 * Purpose: Delete a specific doctor using their ID and an authentication token
 * @param {string} id - Doctor ID
 * @param {string} token - Authentication token
 */
export async function deleteDoctor(id, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/${id}/${token}`, {
      method: "DELETE"
    });

    const data = await response.json();

    return {
      success: response.ok,
      message: data.message || "Unknown error during deletion"
    };
  } catch (error) {
    console.error("Error deleting doctor:", error);
    return { success: false, message: "Failed to delete doctor" };
  }
}

/**
 * Function: saveDoctor
 * Purpose: Save (create) a new doctor using a POST request
 * @param {Object} doctor - Doctor details
 * @param {string} token - Authentication token
 */
export async function saveDoctor(doctor, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/${token}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(doctor)
    });

    const data = await response.json();

    return {
      success: response.ok,
      message: data.message || "Unknown error while saving doctor"
    };
  } catch (error) {
    console.error("Error saving doctor:", error);
    return { success: false, message: "Failed to save doctor" };
  }
}

/**
 * Function: filterDoctors
 * Purpose: Fetch doctors based on filtering criteria (name, time, and specialty)
 * @param {string} name
 * @param {string} time
 * @param {string} specialty
 */
export async function filterDoctors(name = "", time = "", specialty = "") {
  try {
    const url = `${DOCTOR_API}/filter/${encodeURIComponent(name)}/${encodeURIComponent(time)}/${encodeURIComponent(specialty)}`;
    const response = await fetch(url, { method: "GET" });

    if (!response.ok) {
      console.error("Failed to filter doctors");
      return { doctors: [] };
    }

    return await response.json();
  } catch (error) {
    console.error("Error filtering doctors:", error);
    alert("An error occurred while filtering doctors");
    return { doctors: [] };
  }
}
