# Smart Clinic Database Schema Design

This document outlines the hybrid data storage approach for the Smart Clinic System using:
- **MySQL**: For structured, relational data
- **MongoDB**: For flexible, document-based data

---

## **1. MySQL Database Design**

### **Tables**

#### 1. `patients`
| Column Name       | Data Type      | Constraints                      |
|-------------------|---------------|-----------------------------------|
| patient_id        | INT           | PRIMARY KEY, AUTO_INCREMENT      |
| first_name        | VARCHAR(50)   | NOT NULL                         |
| last_name         | VARCHAR(50)   | NOT NULL                         |
| email             | VARCHAR(100)  | UNIQUE, NOT NULL                 |
| phone_number      | VARCHAR(15)   | UNIQUE                           |
| date_of_birth     | DATE          | NOT NULL                         |
| gender            | ENUM('Male', 'Female', 'Other') | NOT NULL      |

---

#### 2. `doctors`
| Column Name       | Data Type      | Constraints                      |
|-------------------|---------------|-----------------------------------|
| doctor_id         | INT           | PRIMARY KEY, AUTO_INCREMENT      |
| first_name        | VARCHAR(50)   | NOT NULL                         |
| last_name         | VARCHAR(50)   | NOT NULL                         |
| specialization    | VARCHAR(100)  | NOT NULL                         |
| email             | VARCHAR(100)  | UNIQUE, NOT NULL                 |
| phone_number      | VARCHAR(15)   | UNIQUE                           |

---

#### 3. `appointments`
| Column Name       | Data Type      | Constraints                      |
|-------------------|---------------|-----------------------------------|
| appointment_id    | INT           | PRIMARY KEY, AUTO_INCREMENT      |
| patient_id        | INT           | FOREIGN KEY REFERENCES patients(patient_id) ON DELETE CASCADE |
| doctor_id         | INT           | FOREIGN KEY REFERENCES doctors(doctor_id) ON DELETE CASCADE   |
| appointment_date  | DATETIME      | NOT NULL                         |
| status            | ENUM('Scheduled', 'Completed', 'Cancelled') | DEFAULT 'Scheduled' |

---

#### 4. `admin`
| Column Name       | Data Type      | Constraints                      |
|-------------------|---------------|-----------------------------------|
| admin_id          | INT           | PRIMARY KEY, AUTO_INCREMENT      |
| username          | VARCHAR(50)   | UNIQUE, NOT NULL                 |
| password_hash     | VARCHAR(255)  | NOT NULL                         |
| email             | VARCHAR(100)  | UNIQUE, NOT NULL                 |

---

## **2. MongoDB Collection Design**

### **Collection: `prescriptions`**
- **Reason for MongoDB**: Prescriptions can have variable numbers of medications, dosage instructions, and additional notes. A flexible schema is ideal.

**Example Document:**
```json
{
  "prescription_id": "RX-2025-0001",
  "patient_id": 101,
  "doctor_id": 12,
  "issued_date": "2025-08-10",
  "medications": [
    {
      "name": "Amoxicillin",
      "dosage": "500mg",
      "frequency": "3 times a day",
      "duration_days": 7
    },
    {
      "name": "Ibuprofen",
      "dosage": "200mg",
      "frequency": "as needed",
      "duration_days": 5
    }
  ],
  "notes": "Take Amoxicillin after meals. Avoid alcohol during treatment."
}
