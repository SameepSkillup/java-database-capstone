

Section 1: Architecture Summary

This Spring Boot application follows a hybrid architecture combining **MVC (Model-View-Controller)** and **RESTful services**. The **Admin** and **Doctor dashboards** are rendered using **Thymeleaf templates**, providing a user-friendly web interface. Meanwhile, other modules such as Patient registration, Login, and Appointments are handled through **REST APIs**, making the application accessible to external clients like mobile apps or SPAs (Single Page Applications).

The system interacts with two databases:

* **MySQL** is used for structured relational data including Admin, Doctor, Patient, and Appointment records. It leverages **JPA (Java Persistence API)** for object-relational mapping.
* **MongoDB** is used for handling unstructured or semi-structured data such as **Prescription records**, which are stored as **documents** using Spring Data MongoDB.

The application routes all incoming requests through corresponding controllers (REST or MVC), which then pass the request to a **shared service layer**. This service layer encapsulates business logic and communicates with **JPA repositories** or **MongoDB repositories** depending on the data source.


Section 2: Numbered Flow of Data and Control

1. The user initiates an action—either by accessing a web page (like Admin Dashboard) or sending a REST request (e.g., for patient registration).
2. Based on the request type, it is routed to either a **Thymeleaf-based MVC controller** or a **REST controller**.
3. The controller processes the request and invokes the corresponding method in the **service layer**.
4. The service layer applies any business logic or validations required for the operation.
5. The service then interacts with the appropriate **repository layer**—JPA for MySQL or Spring Data MongoDB for document-based storage.
6. The result is returned from the repository to the service, then to the controller.
7. The controller finally returns the response—either by rendering a Thymeleaf view or sending back JSON/XML in case of REST endpoints.
