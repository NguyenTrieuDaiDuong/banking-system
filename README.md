# Banking System

The **Banking System** project is a web application that simulates a banking system, supporting customer management, accounts, transactions and reports. The project is developed using **Java Spring Boot**, with a **MySQL** database.

---

## Main Features

- Customer management (add, edit, delete, view details)
- Bank account management
- Transaction execution: money transfer (internal), deposit/withdrawal
- View transaction history and report summary
- System authorization basis (user/admin)
- Unlock/lock account status by owner

---

## Technology Used

- **Backend:** Java, Spring Boot, Spring Data JPA
- **Database:** MySQL
- **Tools:** Maven, Git, GitHub

---

## System Requirements

- **Java:** JDK 11 or higher
- **Maven:** 3.6+
- **MySQL:** 8.0+
- **IDE:** IntelliJ IDEA, Eclipse, or VS Code (recommended)

---

## Installation Instructions

### 1. Clone the project to your computer

```bash
git clone https://github.com/username/banking_system.git
cd banking_system
```

### 2. Configure Database

Create a MySQL database:

```sql
CREATE DATABASE banking_system;
```

Update database configuration in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3308/banking_system
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3. Install Dependencies

Using Maven:

```bash
mvn clean install
```

Or if using Maven wrapper:

```bash
./mvnw clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

Or:

```bash
./mvnw spring-boot:run
```

The application will run at: `http://localhost:8088`

---

## Project Structure

```
banking_system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/demo/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │             ├── admin/
│   │   │             ├── api/
│   │   │       ├── dtos/
│   │   │             ├── response/
│   │   │             ├── request/
|   |   |       ├── entities/
|   |   |       ├── exception/
|   |   |       ├── filter/
|   |   |       ├── repositories/
|   |   |       ├── service/
│   │   │             ├── admin/
│   │   │             ├── impl/
│   │   │       └── BankingSystemApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   └── test/
├── pom.xml
└── README.md
```

---

## API Endpoints

### Customer Management
- `GET /api/customers` - Get all customers
- `GET /api/customers/{id}` - Get customer by ID
- `POST /api/customers` - Create new customer
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer

### Account Management
- `GET /api/accounts/my-accounts"` - Get all accounts
- `GET /api/accounts/{accountNumber}` - Get account by accountNumber
- `POST /api/accounts/create-saving` - Create new account
- `PUT /api/accounts/{accountNumber}/lock` - Lock account
- `PUT /api/accounts/{accountNumber}` - Unlock account

### Transactions
- `POST /api/deposit` - Deposit money
- `POST /api/withdraw/internal` - Withdraw money
- `POST /api/transfer/internal` - Transfer money
- `GET /api/transactions/history/{accountNumber}` - Get transaction history
- `GET /api/transactions/{transactionCode}` - Get transaction history by transactionCode
---
### Login / Register
- `POST /api/auth/login` - login account
- `POST /api/auth/register` - register account
### Profile
- `GET /api/users/profile` - view user profile
- `PUT /api/users/profile`` - update user profile
- `PATCH /api/users/change-password`` - change user password
## Usage Example

### Create a new customer

```bash
curl -X POST http://localhost:8088/api/accounts/create-saving \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nguyen Van A",
    "email": "nguyenvana@example.com",
    "phone": "0123456789"
  }'
```

### Transfer money

```bash
curl -X POST http://localhost:8088/api/transfer/internal \
  -H "Content-Type: application/json" \
  -d '{
    "fromAccountId": 1,
    "toAccountId": 2,
    "amount": 1000000
  }'
```

---

## Testing

Run unit tests:

```bash
mvn test
```

---

## Troubleshooting

**Problem:** Cannot connect to database
- **Solution:** Check MySQL is running and credentials in `application.properties` are correct

**Problem:** Port 8080 already in use
- **Solution:** Change port in `application.properties`:
  ```properties
  server.port=8081
  ```

**Problem:** Maven dependencies not downloading
- **Solution:** Try cleaning Maven cache:
  ```bash
  mvn dependency:purge-local-repository
  ```

---

## Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## Author

**Your Name**
- GitHub: https://github.com/NguyenTrieuDaiDuong
- Email: duongnguyen22394@gmail.com

---

## Acknowledgments

- Spring Boot documentation
- MySQL community
- All contributors to this project

---

## Future Enhancements

- [ ] Add email notifications for transactions
- [ ] Implement interest calculation for savings accounts
- [ ] Add multi-language support
- [ ] Create mobile application
- [ ] Add loan management feature
