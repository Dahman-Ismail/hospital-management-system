# 🏥 GRH - Gestionnaire de Rendez-vous Hospitaliers

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)

**A comprehensive Hospital Appointment Management System built with Java and MongoDB**

[Features](#features) • [Installation](#installation) • [Usage](#usage) • [API Documentation](#api-documentation) • [Contributing](#contributing)

</div>

---

## 📋 Table of Contents

- [About](#about)
- [Features](#features)
- [Technologies](#technologies)
- [Database Schema](#database-schema)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Screenshots](#screenshots)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

---

## 🎯 About

**GRH (Gestionnaire de Rendez-vous Hospitaliers)** is a hospital appointment management system designed to streamline the booking process between patients and doctors. The system provides a robust platform for managing patient records, doctor schedules, and appointment bookings with real-time availability checking.

### Key Objectives

- 📅 Simplify appointment booking and management
- 👥 Efficient patient and doctor information management
- ⏰ Real-time schedule availability checking
- 📊 Generate comprehensive reports and analytics
- 🔒 Secure data handling and validation

---

## ✨ Features

### Patient Management
- ✅ Register new patients with complete profile information
- ✅ View, update, and delete patient records
- ✅ Search patients by name or ID
- ✅ Track patient appointment history

### Doctor Management
- ✅ Add, modify, and remove doctor profiles
- ✅ Manage doctor specialties and schedules
- ✅ Define working days and hours
- ✅ Search doctors by specialty or name

### Appointment Management
- ✅ Book appointments with availability validation
- ✅ Check available time slots before booking
- ✅ Update or cancel existing appointments
- ✅ View upcoming appointments by date or doctor
- ✅ Automatic status update for past appointments

### Reports & Analytics
- 📊 List all appointments for a specific date
- 📈 Count appointments by doctor or specialty
- 🔍 Identify patients with multiple recent visits
- 📅 Display available time slots for doctors

### Advanced Features
- 🚀 RESTful API endpoints
- 📧 Email/SMS notifications (mock implementation)
- 📄 Automated reports (PDF, Excel)
- 📊 Doctor dashboard with daily appointments and statistics
- 🔐 Input validation and duplicate booking prevention

---

## 🛠️ Technologies

| Technology | Description |
|------------|-------------|
| **Java** | Core programming language |
| **MongoDB** | NoSQL database for data storage |
| **MongoDB Java Driver** | Database connectivity |
| **Spring Boot** | REST API framework (optional) |
| **Maven/Gradle** | Dependency management |
| **JUnit** | Unit testing framework |

---

## 🗄️ Database Schema

### Collections Structure

#### `patients`
```json
{
  "_id": "ObjectId",
  "patientId": "string",
  "name": "string",
  "dateOfBirth": "date",
  "gender": "string",
  "phone": "string",
  "email": "string",
  "address": "string"
}
```

#### `doctors`
```json
{
  "_id": "ObjectId",
  "doctorId": "string",
  "name": "string",
  "specialty": "string",
  "phone": "string",
  "email": "string",
  "workingDays": ["string"],
  "workingHours": {
    "start": "string",
    "end": "string"
  }
}
```

#### `appointments`
```json
{
  "_id": "ObjectId",
  "appointmentId": "string",
  "patientId": "string",
  "doctorId": "string",
  "date": "date",
  "time": "string",
  "status": "string",
  "notes": "string"
}
```

---

## 🚀 Installation

### Prerequisites

- Java JDK 11 or higher
- MongoDB 4.4 or higher
- Maven 3.6+ or Gradle 7+
- Git

### Step 1: Clone the Repository

```bash
git clone https://github.com/yourusername/grh-hospital-manager.git
cd grh-hospital-manager
```

### Step 2: Install MongoDB

**Ubuntu/Debian:**
```bash
sudo apt-get install mongodb
sudo systemctl start mongodb
```

**macOS:**
```bash
brew tap mongodb/brew
brew install mongodb-community
brew services start mongodb-community
```

**Windows:**
Download and install from [MongoDB Official Website](https://www.mongodb.com/try/download/community)

### Step 3: Build the Project

**Using Maven:**
```bash
mvn clean install
```

**Using Gradle:**
```bash
./gradlew build
```

---

## ⚙️ Configuration

### Database Configuration

Edit `src/main/resources/application.properties`:

```properties
# MongoDB Configuration
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=grh_hospital

# Server Configuration
server.port=8080

# Logging
logging.level.root=INFO
```

### Import Sample Data

```bash
mongoimport --db grh_hospital --collection patients --file data/patients.json
mongoimport --db grh_hospital --collection doctors --file data/doctors.json
mongoimport --db grh_hospital --collection appointments --file data/appointments.json
```

---

## 💻 Usage

### Running the Application

**Console Application:**
```bash
java -jar target/grh-hospital-manager.jar
```

**Spring Boot Application:**
```bash
mvn spring-boot:run
```

### Quick Start Example

```java
// Create a new patient
Patient patient = new Patient("John Doe", "1990-05-15", "Male", 
                              "123-456-7890", "john@email.com", "123 Main St");
patientService.addPatient(patient);

// Book an appointment
Appointment appointment = new Appointment(patientId, doctorId, 
                                          "2024-12-15", "10:00", "Scheduled");
appointmentService.bookAppointment(appointment);
```

---

## 📡 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Endpoints

#### Patients

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/patients` | Get all patients |
| GET | `/patients/{id}` | Get patient by ID |
| POST | `/patients` | Create new patient |
| PUT | `/patients/{id}` | Update patient |
| DELETE | `/patients/{id}` | Delete patient |
| GET | `/patients/search?name={name}` | Search by name |

#### Doctors

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/doctors` | Get all doctors |
| GET | `/doctors/{id}` | Get doctor by ID |
| POST | `/doctors` | Create new doctor |
| PUT | `/doctors/{id}` | Update doctor |
| DELETE | `/doctors/{id}` | Delete doctor |
| GET | `/doctors/specialty/{specialty}` | Get by specialty |

#### Appointments

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/appointments` | Get all appointments |
| GET | `/appointments/{id}` | Get appointment by ID |
| POST | `/appointments` | Create appointment |
| PUT | `/appointments/{id}` | Update appointment |
| DELETE | `/appointments/{id}` | Cancel appointment |
| GET | `/appointments/doctor/{doctorId}` | Get by doctor |
| GET | `/appointments/date/{date}` | Get by date |
| GET | `/appointments/available-slots` | Get available slots |

### Example Requests

**Create Patient:**
```bash
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "dateOfBirth": "1990-05-15",
    "gender": "Male",
    "phone": "123-456-7890",
    "email": "john@email.com",
    "address": "123 Main St"
  }'
```

**Book Appointment:**
```bash
curl -X POST http://localhost:8080/api/appointments \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": "P001",
    "doctorId": "D001",
    "date": "2024-12-15",
    "time": "10:00",
    "status": "Scheduled"
  }'
```

---

## 📸 Screenshots

### Dashboard
![Dashboard](screenshots/dashboard.png)

### Appointment Booking
![Booking](screenshots/booking.png)

### Patient Management
![Patients](screenshots/patients.png)

---

## 📁 Project Structure

```
grh-hospital-manager/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── grh/
│   │   │           ├── models/
│   │   │           │   ├── Patient.java
│   │   │           │   ├── Doctor.java
│   │   │           │   └── Appointment.java
│   │   │           ├── services/
│   │   │           │   ├── PatientService.java
│   │   │           │   ├── DoctorService.java
│   │   │           │   └── AppointmentService.java
│   │   │           ├── controllers/
│   │   │           │   ├── PatientController.java
│   │   │           │   ├── DoctorController.java
│   │   │           │   └── AppointmentController.java
│   │   │           ├── repositories/
│   │   │           │   ├── PatientRepository.java
│   │   │           │   ├── DoctorRepository.java
│   │   │           │   └── AppointmentRepository.java
│   │   │           └── GRHApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   └── test/
│       └── java/
├── data/
│   ├── patients.json
│   ├── doctors.json
│   └── appointments.json
├── screenshots/
├── docs/
│   └── project-report.pdf
├── pom.xml
├── README.md
└── LICENSE
```

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Coding Standards

- Follow Java naming conventions
- Write meaningful commit messages
- Add unit tests for new features
- Update documentation as needed

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👥 Team

- **Your Name** - *Lead Developer* - [GitHub](https://github.com/yourusername)
- **Team Member 2** - *Backend Developer*
- **Team Member 3** - *Database Administrator*

---

## 📧 Contact

**Project Link:** [https://github.com/yourusername/grh-hospital-manager](https://github.com/yourusername/grh-hospital-manager)

**Email:** your.email@example.com

---

## 🙏 Acknowledgments

- MongoDB Documentation
- Spring Boot Framework
- Java Community
- All contributors and testers

---

<div align="center">

**Made with ❤️ for healthcare management**

⭐ Star this repository if you find it helpful!

</div>
