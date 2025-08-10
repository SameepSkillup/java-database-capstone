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
# Smart Clinic Management System – Schema Design

## MySQL Database Design

Structured, validated, and interrelated data works well in MySQL. Below are the core tables.

---

### Table: patients
- **id**: INT, Primary Key, AUTO_INCREMENT  
- **first_name**: VARCHAR(50), NOT NULL  
- **last_name**: VARCHAR(50), NOT NULL  
- **dob**: DATE, NOT NULL  
- **email**: VARCHAR(100), UNIQUE, NOT NULL  
- **phone**: VARCHAR(15), UNIQUE, NOT NULL  
- **created_at**: TIMESTAMP DEFAULT CURRENT_TIMESTAMP

---

### Table: doctors
- **id**: INT, Primary Key, AUTO_INCREMENT  
- **first_name**: VARCHAR(50), NOT NULL  
- **last_name**: VARCHAR(50), NOT NULL  
- **specialization**: VARCHAR(100), NOT NULL  
- **email**: VARCHAR(100), UNIQUE, NOT NULL  
- **phone**: VARCHAR(15), UNIQUE, NOT NULL  
- **availability_status**: BOOLEAN DEFAULT TRUE  
- **created_at**: TIMESTAMP DEFAULT CURRENT_TIMESTAMP

---

### Table: appointments
- **id**: INT, Primary Key, AUTO_INCREMENT  
- **doctor_id**: INT, Foreign Key → doctors(id)  
- **patient_id**: INT, Foreign Key → patients(id)  
- **appointment_time**: DATETIME, NOT NULL  
- **status**: ENUM('Scheduled','Completed','Cancelled') DEFAULT 'Scheduled'  
- **notes**: TEXT NULL  
- **created_at**: TIMESTAMP DEFAULT CURRENT_TIMESTAMP

---

### Table: admin
- **id**: INT, Primary Key, AUTO_INCREMENT  
- **username**: VARCHAR(50), UNIQUE, NOT NULL  
- **password_hash**: VARCHAR(255), NOT NULL  
- **role**: ENUM('SuperAdmin','Staff') DEFAULT 'Staff'  
- **created_at**: TIMESTAMP DEFAULT CURRENT_TIMESTAMP

---

**Design Notes:**
- Email and phone fields are marked `UNIQUE` to avoid duplicates.  
- `appointments` table uses ENUM for status for data consistency.  
- `doctor_id` and `patient_id` enforce relational integrity via foreign keys.  
- Passwords in `admin` table should always be stored hashed, never plain text.  
- Historical appointment records are retained for analytics.

---

## MongoDB Collection Design

Some data doesn’t fit well in rigid tables, like free-form notes, prescription details, or feedback. For this design, we’ll store **prescriptions** in MongoDB.

---

### Collection: prescriptions
```json
{
  "_id": { "$oid": "64abc123456789abcdef012" },
  "appointmentId": 51,
  "patient": {
    "id": 12,
    "name": "John Smith"
  },
  "doctor": {
    "id": 3,
    "name": "Dr. Emily Carter",
    "specialization": "Cardiology"
  },
  "medications": [
    {
      "name": "Paracetamol",
      "dosage": "500mg",
      "instructions": "Take 1 tablet every 6 hours",
      "duration_days": 5
    },
    {
      "name": "Aspirin",
      "dosage": "75mg",
      "instructions": "Take once daily",
      "duration_days": 30
    }
  ],
  "pharmacy": {
    "name": "Walgreens SF",
    "location": "Market Street, San Francisco"
  },
  "created_at": { "$date": "2025-08-10T09:00:00Z" },
  "tags": ["urgent", "follow-up"]
}
