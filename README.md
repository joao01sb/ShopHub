# 🛒 ShopHub

A modern e-commerce Android application built with Jetpack Compose, following Clean Architecture, SOLID principles, and MVVM pattern.

## 📱 Features

- **User Authentication**: Login and registration via Firebase Authentication
- **Product Catalog**: Browse and view products
- **Shopping Cart**: Add/remove products and complete purchases
- **Order History**: View order history
- **Product Search**: Search system with search history
- **Bottom Navigation**: Intuitive navigation between main sections

## 🏗️ Architecture

The project follows **Clean Architecture** principles with:

- **Presentation Layer**: ViewModels, UI Events, and Compose Screens
- **Domain Layer**: Use Cases and Domain Models
- **Data Layer**: Repositories, Data Sources (Remote/Local)

### 📂 Feature Structure

```
features/
├── auth/           # Authentication (Login/Register)
├── cart/           # Shopping cart
├── home/           # Home screen and products
└── orders/         # Order history
```

## 🛠️ Technologies and Libraries

### Core
- **Kotlin** - Main programming language
- **Jetpack Compose** - Modern declarative UI
- **Android Jetpack** - Android components

### Architecture and Dependency Injection
- **Hilt** - Dependency injection
- **Navigation Compose** - Screen navigation
- **ViewModel** - State management

### Data Persistence
- **Firebase Firestore** - Cloud database
- **Firebase Authentication** - User authentication
- **Room Database** - Local database (search history)

### Network and Serialization
- **Retrofit** - HTTP client
- **OkHttp** - Network interceptors and logging
- **Kotlinx Serialization** - JSON serialization

### UI and Images
- **Material 3** - Design system
- **Coil** - Image loading

### Pagination
- **Paging 3** - Efficient list loading

### Code Quality
- **Detekt** - Static code analysis
- **Kover** - Test coverage

### Testing
- **JUnit** - Unit tests
- **Mockk** - Mocking for tests
- **Turbine** - Flow testing
- **Truth** - Assertions

## 🚀 How to Run

### Prerequisites
- Android Studio Arctic Fox or higher
- JDK 11 or higher
- Android SDK 26+

### Setup

1. **Clone the repository**:
```bash
git clone https://github.com/joao01sb/ShopHub.git
cd ShopHub
```

2. **Configure Firebase**:
   - Create a project in [Firebase Console](https://console.firebase.google.com/)
   - Add the `google-services.json` file to the `app/` folder
   - Set up Authentication and Firestore

3. **Run the project**:
```bash
./gradlew assembleDebug
```

## 🔧 Development Scripts

### Code Analysis
```bash
# Run Detekt
./gradlew detekt

# Run tests
./gradlew test

# Generate coverage report
./gradlew koverHtmlReport
```

## 📋 CI/CD

The project includes GitHub Actions for:
- ✅ Code analysis with Detekt
- ✅ Unit test execution
- ✅ Build verification
- 🔄 Auto-merge from develop to master (when all checks pass)

## 📐 Code Standards

- **Clean Architecture** with clear separation of concerns
- **MVVM** for UI state management
- **Repository Pattern** for data abstraction
- **Use Cases** for business logic
- **Dependency Injection** with Hilt

## 🔒 Security

- Secure authentication via Firebase
- Firestore security rules
- Data validation on client and server

## 📊 Code Analysis

The project uses **Detekt** with custom configurations to:
- Detect code smells
- Enforce naming patterns
- Check method complexity
- Ensure consistent formatting

## 🧪 Testing

- **Unit Tests**: Business logic validation
- **Integration Tests**: Complete flow verification
- **Mocking**: External dependency isolation
