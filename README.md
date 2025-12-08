📦 E-Commerce Web Application (Spring Boot + Thymeleaf)

A complete full-stack E-Commerce application built using Spring Boot, Thymeleaf, Spring Data JPA, and MySQL.
The project includes role-based login, with separate panels for Admin and User.

🚀 Features
👤 User Features

User Registration & Login

Role-based dashboard (User Panel)

Browse products

Add to cart

Place orders

View order history

Update profile

🛠️ Admin Features

Admin dashboard

Manage Categories

Manage Products

Manage Users

Manage Orders (view & update status)

Upload product images

View product list, orders list, users list

🔐 Test Credentials
Admin Login
Username: admin
Password: admin

User Login
Username: user
Password: user

🛠️ Tech Stack
Layer	Technology
Backend	Spring Boot
Template Engine	Thymeleaf
Database	MySQL
ORM	Spring Data JPA
Security	Spring Security
Frontend	HTML, CSS
Build Tool	Maven
📁 Project Structure
src/
├── main/
│    ├── java/
│    │     └── com.shop/
│    │          ├── config/
│    │          ├── controller/
│    │          ├── entity/
│    │          ├── repository/
│    │          ├── service/
│    │          └── ShopApplication.java
│    │
│    └── resources/
│          ├── static/
│          │     ├── css/
│          │     └── images/        # static images (not uploaded)
│          │
│          ├── templates/
│          │     ├── admin/
│          │     ├── user/
│          │     └── authentication/
│          │
│          └── application.properties
│
└── uploads/                         # dynamic product uploads (ignored in Git)


⚙️ How to Run the Project
1️⃣ Clone the Repository
git clone https://github.com/primeio/e-commerce.git

2️⃣ Configure MySQL

Edit src/main/resources/application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/shop
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

📂 Uploads Folder

This project uses an /uploads folder for storing dynamic product images uploaded by admin.

This folder is not included in GitHub.

If you clone the project, create this folder manually:

mkdir uploads


Then upload product images using the Admin Pane

3️⃣ Run the Application
mvn spring-boot:run

4️⃣ Access the Application

After login, the system automatically redirects users based on their role:

Role	Redirect URL	Description
USER	http://localhost:8080/	User dashboard for browsing products, adding to cart, and ordering
ADMIN	http://localhost:8080/admin	Admin dashboard for managing products, categories, orders, and users

You do not need to manually enter URLs.
The application will detect the logged-in user's role and:

Redirect User → User Panel

Redirect Admin → Admin Panel

🧑‍💻 Author

Sushant Shinde
GitHub: https://github.com/primeio