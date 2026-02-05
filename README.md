# Hotel Lobby Unmanned Vending System

酒店大堂无人售货超市系统 - A full-stack hotel lobby unmanned vending machine system

> 🔑 **管理员登录**: [点击查看登录信息](LOGIN_CREDENTIALS.md) | **Admin Login**: [Click for credentials](LOGIN_CREDENTIALS.md)
> 
> 📖 **中文用户请查看**: [常见问题FAQ (中文)](FAQ_CN.md) - 包含管理员账号、系统功能等详细说明

## Features

### Core E-commerce Functionality
- ✅ User registration and login
- ✅ Product catalog browsing
- ✅ Shopping cart management
- ✅ Order creation and management
- ✅ Order history tracking

### Feature 1: Employee Internal Pricing 🏷️
- Hotel employees get special discounted prices on products
- Support for both percentage-based discounts and fixed employee prices
- Automatic price calculation based on user role
- Visual indicators for discounted items

### Feature 2: Electronic Coupon System 🎫
- Coupon distribution and management
- Multiple coupon types:
  - Full reduction (e.g., ¥10 off ¥50)
  - Percentage discount (e.g., 20% off)
  - Fixed amount discount
- User coupon collection and usage
- Automatic validation of coupon conditions

### Feature 3: Dual Payment Support 💳
- WeChat Pay integration
- Alipay integration
- User can choose payment method at checkout

## Technology Stack

### Backend
- **Spring Boot 3** - Application framework
- **MyBatis-Plus** - ORM framework
- **SaToken** - Authentication and authorization
- **Knife4j** - API documentation (Swagger)
- **MySQL** - Database
- **Redis** - Caching and session storage
- **Maven** - Dependency management

### Frontend
- **Vue 3** - Progressive JavaScript framework
- **Vite** - Build tool
- **TypeScript** - Type-safe JavaScript
- **Pinia** - State management
- **Vue Router** - Client-side routing
- **Axios** - HTTP client
- **Element Plus** - UI component library

## Project Structure

```
hotel/
├── backend/                 # Spring Boot backend
│   ├── src/main/java/
│   │   └── com/hotel/vending/
│   │       ├── controller/  # REST controllers
│   │       ├── service/     # Business logic
│   │       ├── mapper/      # MyBatis mappers
│   │       ├── entity/      # Entity models
│   │       ├── dto/         # Data transfer objects
│   │       ├── vo/          # View objects
│   │       ├── config/      # Configuration classes
│   │       └── common/      # Common utilities
│   ├── src/main/resources/
│   │   ├── application.yml  # Application configuration
│   │   └── schema.sql       # Database schema
│   └── pom.xml              # Maven dependencies
│
└── frontend/                # Vue 3 frontend
    ├── src/
    │   ├── api/             # API service layer
    │   ├── stores/          # Pinia stores
    │   ├── router/          # Vue Router configuration
    │   ├── views/           # Page components
    │   ├── components/      # Reusable components
    │   ├── types/           # TypeScript type definitions
    │   ├── utils/           # Utility functions
    │   ├── App.vue          # Root component
    │   └── main.ts          # Application entry point
    ├── package.json         # NPM dependencies
    ├── vite.config.ts       # Vite configuration
    └── tsconfig.json        # TypeScript configuration
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- MySQL 8.0 or higher
- Redis 6.0 or higher
- Maven 3.6 or higher

### Database Setup

1. Create MySQL database:
```sql
CREATE DATABASE hotel_vending DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Run the schema script:
```bash
mysql -u root -p hotel_vending < backend/src/main/resources/schema.sql
```

### Backend Setup

1. Navigate to backend directory:
```bash
cd backend
```

2. Update database configuration in `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hotel_vending?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

3. Build and run:
```bash
mvn clean install
mvn spring-boot:run
```

Backend will start on http://localhost:8080

API documentation available at: http://localhost:8080/api/doc.html

### Frontend Setup

1. Navigate to frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Run development server:
```bash
npm run dev
```

Frontend will start on http://localhost:3000

4. Build for production:
```bash
npm run build
```

## Demo Accounts / 演示账号

After running the database schema, you can use these demo accounts:

运行数据库脚本后，可以使用以下演示账号：

| Username / 用户名 | Password / 密码 | Role / 角色 | Description / 描述                    |
|-------------------|-----------------|-------------|---------------------------------------|
| admin             | 123456          | admin       | Administrator account / 管理员账号    |
| employee1         | 123456          | employee    | Employee with 20% discount / 员工账号（20%折扣） |
| customer1         | 123456          | customer    | Regular customer account / 普通客户账号 |

**Note / 注意**: Passwords are hashed with BCrypt. The plain text password for all demo accounts is `123456`.

**注意**：密码使用 BCrypt 加密存储。所有演示账号的明文密码都是 `123456`。

### 管理员登录信息 / Administrator Login Information

- **用户名 / Username**: `admin`
- **密码 / Password**: `123456`
- **角色 / Role**: Administrator / 管理员

## API Endpoints

### User Management
- `POST /api/user/register` - User registration
- `POST /api/user/login` - User login
- `POST /api/user/logout` - User logout
- `GET /api/user/info` - Get current user info

### Product Management
- `GET /api/product/list` - Get all products
- `GET /api/product/{id}` - Get product by ID
- `GET /api/product/category/{categoryId}` - Get products by category
- `GET /api/product/search?keyword={keyword}` - Search products

### Coupon Management
- `GET /api/coupon/available` - Get available coupons
- `POST /api/coupon/receive/{couponId}` - Receive a coupon
- `GET /api/coupon/my` - Get user's coupons
- `GET /api/coupon/available-for-order?orderAmount={amount}` - Get applicable coupons

### Order Management
- `POST /api/order/create` - Create order
- `POST /api/order/pay/{orderId}` - Pay order
- `GET /api/order/my` - Get user's orders
- `GET /api/order/{orderId}` - Get order detail
- `POST /api/order/cancel/{orderId}` - Cancel order

## License

This project is licensed under the MIT License.

## Contact

For questions or support, please contact the development team.
