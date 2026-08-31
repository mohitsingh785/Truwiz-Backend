````markdown
# TruWiz

<p align="center">
  <img src="docs/images/logo.png" width="180" alt="TruWiz Logo"/>
</p>

<h3 align="center">Know Before You Use</h3>

<p align="center">
AI-powered Cosmetic Ingredient Intelligence Platform
</p>

<p align="center">

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)
![JWT](https://img.shields.io/badge/JWT-Security-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Swagger](https://img.shields.io/badge/OpenAPI-Swagger-green)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED)

</p>

---

#  Overview

**TruWiz** is an intelligent cosmetic ingredient analysis platform that helps users determine whether a skincare or haircare product is suitable for their unique skin and hair profile.

Instead of relying on generic product ratings, TruWiz performs **scientific ingredient analysis**, **personalized risk scoring**, and **AI-generated explanations** based on the user's profile.

The platform combines a deterministic scoring engine with Large Language Models (LLMs) to deliver transparent and trustworthy cosmetic safety reports.

---

#  Application Preview

<p align="center">
<img src="docs/images/login.png" width="250"/>
<img src="docs/images/home.png" width="250"/>
<img src="docs/images/analysis.png" width="250"/>
</p>

---

#  Application Demo

<p align="center">
<img src="docs/videos/scan-demo.gif" width="280"/>
</p>

---

# ✨ Features

- 🔐 JWT Authentication
- 📧 Email OTP Verification
- 👤 User Profile Management
- 🧴 Personalized Cosmetic Analysis
- 🧪 Ingredient Intelligence Engine
- ⚠️ Allergen Detection
- 📷 OCR Ingredient Extraction (Planned)
- 🤖 AI-powered Product Explanation
- 📊 \\Scientific Safety Scoring
-  INCI Chemical Database
- Product Catalog
- Public Developer API
- Audit Logging
- Swagger Documentation

---

# 🧠 Core Engine Highlights

### Multi-Parametric Scoring

Each ingredient is evaluated using eight scientific metrics:

| Metric | Description |
|----------|-------------|
| Safety | Overall ingredient safety |
| Irritation | Skin irritation potential |
| Comedogenic | Acne / pore-clogging risk |
| Hydration | Moisturizing effectiveness |
| Sebum Control | Oil regulation capability |
| Hair Benefit | Hair health contribution |
| Allergy Risk | Personalized allergy detection |
| Confidence | Scientific evidence confidence |

---

### Position-Based Weighting

Ingredient concentration is estimated using its position within the INCI list.

Ingredients appearing earlier in the formulation contribute significantly more to the final product score than trace ingredients.

---

### Personalized Profile Matching

Every product is evaluated against the user's:

- Skin Type
- Hair Type
- Known Allergies

This ensures every analysis is personalized rather than generic.

---

### AI Explanation Layer

The scoring engine always computes the rating first.

The LLM only receives structured JSON outputs from the scoring engine to generate human-readable explanations.

**The AI cannot:**

- Change scores
- Modify ingredient risk
- Override calculations
- Invent safety information

This separation guarantees explainability while preserving deterministic scoring.

---

### Guided OCR Pipeline

For products unavailable in the catalog:

1. Capture packaging images
2. Extract ingredient list using OCR
3. Match ingredients against the INCI database
4. Calculate a preliminary safety score
5. Generate AI explanations
6. Queue product for verification

---

### Public Developer API

API Client Keys allow third-party developers to access:

- Product Catalog
- INCI Database
- Ingredient Information
- Allergen Mappings
- Product Analysis APIs

---

# 🏗 System Architecture

<p align="center">
<img src="docs/images/architecture.svg"/>
</p>

---

# 🔄 Product Analysis Pipeline

<p align="center">
<img src="docs/images/scoring-flow.svg"/>
</p>

The analysis process follows these stages:

1. Product Search / Barcode Scan
2. Ingredient Extraction
3. Synonym Resolution
4. INCI Database Lookup
5. Ingredient Risk Analysis
6. Position Weighting
7. Personalized Profile Matching
8. Product Rating Calculation
9. AI Explanation Generation
10. Personalized Safety Report

---

# 🗄 Database Schema

<p align="center">

<a href="docs/database-er.svg">

<img src="docs/images/database-er.svg" width="1000"/>

</a>

</p>

The database is organized into four major domains:

- Authentication & Security
- User Health Profiles
- Product Catalog
- INCI Chemical Intelligence

---

# 🛠 Tech Stack

| Layer | Technology |
|--------|------------|
| Language | Java 21 |
| Framework | Spring Boot 3.x |
| Security | Spring Security + JWT |
| ORM | Spring Data JPA / Hibernate |
| Database | MySQL 8 |
| Build Tool | Maven |
| Documentation | OpenAPI / Swagger |
| AI | Gemini / OpenAI |
| OCR | Google ML Kit *(Planned)* |
| Containerization | Docker |

---

# 📁 Project Structure

```text
src
├── config
├── controller
├── dto
├── entity
├── enums
├── exception
├── mapper
├── repository
├── security
├── service
├── util
└── validator
```

---

# 🚀 Getting Started

## Prerequisites

- Java 21
- Maven 3.8+
- MySQL 8+

---

## Clone Repository

```bash
git clone https://github.com/your-username/truwiz-backend.git

cd truwiz-backend
```

---

## Configure Database

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/truwiz_db?createDatabaseIfNotExist=true&useSSL=false
    username: ${DB_USER:root}
    password: ${DB_PASS:password}

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
```

---

## Build

```bash
mvn clean package -DskipTests
```

---

## Run

```bash
mvn spring-boot:run
```

---

# 📚 API Documentation

After running the application:

```
http://localhost:8080/swagger-ui.html
```

---

# 🛣 Roadmap

- ✅ Authentication
- ✅ OTP Verification
- ✅ User Profiles
- ✅ Ingredient Intelligence
- ✅ Personalized Product Analysis
- ✅ AI Explanations
- ✅ Public Developer API
- ✅ Audit Logging
- 🚧 OCR Ingredient Recognition
- 🚧 Barcode Scanner
- 🚧 Product Recommendations
- 🚧 Product Comparison
- 🚧 Admin Dashboard
- 🚧 Analytics

---

# 👨‍💻 Authors

## Backend Development

**Mohit Singh**

- LinkedIn: https://www.linkedin.com/in/mohit-singh-37966720b/

---

## UI / UX Design

**Uma Shankar**

- LinkedIn: https://www.linkedin.com/in/uma-shankar-k/
- Portfolio: https://umashankardesign.netlify.app

---

# ⭐ Support

If you found this project useful, consider giving it a ⭐ on GitHub.

It helps others discover the project and motivates future development.

---

<p align="center">
Built with ❤️ using Spring Boot, AI, and Cosmetic Science.
</p>
````
