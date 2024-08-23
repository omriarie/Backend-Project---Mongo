# Backend Development for Media Management Website

![Media Management Logo](mongo.png)

This project involved the development of the backend for a Media Management Website. The frontend and basic structure of the website were pre-existing, and my role focused on implementing the backend functionality using MongoDB. The work included establishing the database connection, creating controllers, and defining the necessary models to manage media items, user data, and interaction history.

## Key Features

- **MongoDB Integration and Data Management:**
  - **Database Design:** Structured MongoDB collections to efficiently store and manage various types of data, including media items, user profiles, and interaction history.
  - **Data Storage and Retrieval:** Utilized MongoDB's document-based model to store complex data, ensuring flexible and efficient access.
  - **Efficient Querying:** Implemented optimized queries for quick data retrieval, ensuring system responsiveness even as the dataset grew.

- **API Development:**
  - **CRUD Operations:** Developed RESTful APIs using the Spring framework for managing user registrations, media items, and historical data.
  - **Data Ingestion from External Sources:** Implemented functionality to fetch data from an external server via a URL, downloading a CSV file, parsing it, and storing the data in MongoDB.

- **Controllers and Services:**
  - **Controller Implementation:** Created modular and reusable controllers (e.g., `ParentController`, `RegistrationController`, `ItemsController`, `HistoryController`) to manage application logic and database interactions.
  - **Service Layer:** Developed a service layer to encapsulate business logic, ensuring clean separation between request handling and database operations.

- **Data Management Features:**
  - **User Registration and Authentication:** The `RegistrationController` handles user sign-ups, securely storing user credentials and profile information in MongoDB.
  - **Media Item Management:** The `ItemsController` manages CRUD operations for media items, maintaining data consistency.
  - **Interaction History Tracking:** The `HistoryController` tracks user interactions with media items, storing this data for insights into user behavior.

## Technologies Used

- **MongoDB:** Used as the primary database for storing media items, user data, and interaction history.
- **Java & Spring Framework:** Implemented the backend using Java and the Spring framework for robust API development.
- **RESTful API:** Created APIs to manage data operations between the frontend and MongoDB.
- **CSV Data Handling:** Developed a system to download, parse, and store data from external CSV files.

## Project Structure

- **`MediaItems.java`:** Represents the media items stored in the database, including metadata and other relevant details.
- **`User.java`:** Defines the structure of user profiles, managing authentication and user-specific data.
- **`ParentController.java`:** A base controller that other controllers extend, managing common functionality.
- **`RegistrationController.java`:** Manages user registration and authentication processes.
- **`ItemsController.java`:** Handles CRUD operations for media items.
- **`HistoryController.java`:** Tracks and stores user interactions with media items.

## Setup and Installation

Ensure you have Java and MongoDB installed on your system.

1. **Clone the repository**
   ```bash
   git clone https://github.com/omriarie/Backend-Project---Mongo.git

   ```
2. **Navigate to the project directory**
   ```bash
   cd media-management-backend

   ```
3. **Configure MongoDB Connection**
   ```bash
   git clone https://github.com/omriarie/Backend-Project---Mongo.git

   ```
4. **Run the Application**
   ```bash
   java -jar target/Backend-Project---Mongo.jar

   ```

## Contact
For any inquiries or issues, please open an issue on the GitHub repository or contact the maintainers directly:

Omri Arie – omriarie@gmail.com  
Project Link: https://github.com/omriarie/Backend-Project---Mongo
