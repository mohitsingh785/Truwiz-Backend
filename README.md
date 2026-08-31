<div align="center">

<img src="docs/truwizlogo.svg" alt="TruWiz Logo" width="120" onerror="this.style.display='none'"/>

# TruWiz Backend

### Know Before You Use

**AI-powered Product Intelligence Platform — built on deterministic science, explained in plain English.**

<br/>

<img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21"/>
<img src="https://img.shields.io/badge/Spring%20Boot-3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot 3"/>
<img src="https://img.shields.io/badge/Spring%20Security-JWT-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white" alt="Spring Security"/>
<img src="https://img.shields.io/badge/MySQL-8-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL"/>
<img src="https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker"/>
<img src="https://img.shields.io/badge/OpenAPI-Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black" alt="Swagger"/>

<br/><br/>

[Getting Started](#getting-started) · [API Docs](#swagger-documentation) · [Architecture](#system-architecture) · [Roadmap](#roadmap)

</div>

<br/>

## Overview

TruWiz is an AI-powered **Product Intelligence Platform** built to help people understand what's actually in the products they use — starting with ingredients, and starting with personal care.

The platform analyzes a product's ingredient composition against an individual's profile — skin type, hair type, and known allergies — to determine whether that product is genuinely suitable for them. Not a generic star rating, but a personalized, evidence-based verdict.

**Cosmetics are the starting point, not the destination.** TruWiz is being built domain by domain: personal care, cosmetics, skincare, and haircare today; food, packaged goods, household products, baby care, and pet care as the platform matures. The scoring engine, allergy-mapping model, and AI explanation layer are all built to generalize across product categories — see [Vision & Roadmap](#vision--roadmap) below.

What sets TruWiz apart architecturally is that **scoring and explanation are strictly separated.** A deterministic scientific engine computes every score first. Only after the numbers are final does a Large Language Model step in — and its sole job is to translate those numbers into a clear, human-readable explanation. The AI never rates a product on its own, and it never touches a score once one exists. This principle is designed to hold regardless of which product category it's applied to.

<br/>

## Application Preview

<div align="center">

<a href="docs/videos/app-demo.mp4">
  <img src="docs/Codex%20Image%20Sep%201%2C%202026%2C%2001_30_06%20AM.png" alt="TruWiz application preview — click to watch the demo" width="850"/>
</a>

<sub>Click the preview above to watch the full demo (MP4). GitHub does not support inline MP4 playback — swap this asset for a GIF if you want the preview to autoplay directly in the README.</sub>

</div>

<br/>

## Key Features

<table>
<tr>
<td width="50%" valign="top">

**Authentication & Identity**
- JWT-based stateless authentication
- Email OTP verification
- Role-based access control

**User Profiling**
- Skin type classification
- Hair type classification
- Allergy and sensitivity mapping

**Catalog Management**
- Brand management
- Category management
- Full product catalog

</td>
<td width="50%" valign="top">

**Ingredient Intelligence**
- INCI ingredient database
- Ingredient synonym resolution
- Ingredient tagging system
- Ingredient–allergy mapping

**Analysis Engine**
- Deterministic scientific scoring
- Position-based ingredient weighting
- Personalized product analysis
- LLM-powered explanation layer

**Platform**
- Audit logging
- Public developer API
- OCR ingredient extraction *(planned)*

</td>
</tr>
</table>

<br/>

## Why TruWiz Is Different

| | Traditional Cosmetic Apps | TruWiz |
|---|---|---|
| **Scoring** | Opaque, editorial, or crowd-sourced | Deterministic, formula-driven scientific engine |
| **Personalization** | Generic ratings for all users | Matched against skin type, hair type, and allergies |
| **Role of AI** | Often generates the rating itself | Explains a score it cannot change or override |
| **Transparency** | "Black box" verdicts | Every score is traceable to a scientific calculation |
| **Trust model** | Rating and reasoning are entangled | Rating and reasoning are architecturally separated |

<br/>

## Vision & Roadmap

TruWiz's long-term goal is to become a **Product Intelligence Platform** that spans the full range of consumer products people put on and in their bodies and homes — not a cosmetics app.

Personal care is the starting domain because it has the clearest scientific literature and the highest personalization value. The scoring engine, allergy-mapping model, and AI explanation layer are all built to be domain-agnostic, so expanding into a new category is a data and taxonomy problem, not an architecture rewrite.

<table>
<tr>
<td width="50%" valign="top">

**Current Focus**
- Personal Care
- Cosmetics
- Skincare
- Haircare

</td>
<td width="50%" valign="top">

**Planned Expansion**
- Food & Beverages
- Packaged Foods
- Nutritional Products
- Household & Cleaning Products
- Baby Care Products
- Pet Care Products
- Other consumer products with ingredient transparency

</td>
</tr>
</table>

<br/>

## Core Engine

Every ingredient is evaluated against a multi-metric scientific model:

| Metric | Description |
|---|---|
| Safety | Overall safety profile of the ingredient |
| Irritation | Likelihood of causing skin/scalp irritation |
| Comedogenic | Pore-clogging potential |
| Hydration | Moisturizing contribution |
| Sebum Control | Effect on oil regulation |
| Hair Benefit | Contribution to hair health outcomes |
| Allergy Risk | Risk relative to the user's declared allergies |
| Confidence | Reliability of the underlying data for this ingredient |

**Position-based weighting.** Ingredient order in a formulation reflects concentration. TruWiz weights ingredients accordingly — those listed earlier in the INCI list contribute more heavily to the final score, mirroring how formulations are actually read by chemists.

**The AI explanation boundary.** Once scoring is complete, a structured JSON payload — not raw text — is passed to the LLM. The LLM's role is strictly bounded:

```
✔ Convert structured scores into natural-language explanations
✔ Highlight the ingredients driving a result
✔ Communicate findings in plain, accessible English

✘ Cannot modify scores
✘ Cannot invent risks
✘ Cannot override calculations
✘ Cannot manipulate ingredient ratings
```

This boundary is what makes TruWiz's output auditable: every explanation the user reads traces back to a deterministic calculation, not a language model's judgment call.

<br/>

## System Architecture

```
┌──────────────┐      ┌──────────────────┐      ┌─────────────────────┐
│   Client /    │─────▶│   Spring Boot      │─────▶│   Spring Security    │
│   Public API  │      │   REST Layer       │      │   (JWT + OTP)        │
└──────────────┘      └──────────────────┘      └─────────────────────┘
                               │
                               ▼
                    ┌────────────────────┐
                    │  Service Layer       │
                    │  (Business Logic)    │
                    └────────────────────┘
                               │
              ┌────────────────┼────────────────┐
              ▼                                   ▼
   ┌─────────────────────┐            ┌─────────────────────┐
   │  Scientific Scoring   │            │  LLM Explanation      │
   │  Engine (Deterministic)│──scores──▶│  Layer (Gemini/OpenAI) │
   └─────────────────────┘            └─────────────────────┘
              │
              ▼
   ┌─────────────────────┐
   │  Spring Data JPA /     │
   │  Hibernate / MySQL     │
   └─────────────────────┘
```

<br/>

## Product Analysis Flow

```
Product Search
      │
      ▼
Ingredient Extraction
      │
      ▼
Synonym Resolution
      │
      ▼
INCI Database Lookup
      │
      ▼
Scientific Ingredient Analysis
      │
      ▼
Position Weighting
      │
      ▼
User Profile Matching
      │
      ▼
Final Product Score
      │
      ▼
LLM Explanation
      │
      ▼
Personalized Safety Report
```

<br/>

## Database Schema

<div align="center">
<img src="docs/database-er.svg" alt="TruWiz Database ER Diagram" width="850"/>
</div>

The schema centers on a normalized ingredient model: products reference INCI entries through a synonym-resolution layer, while allergy mappings and user profiles join at query time to produce a personalized score — keeping the scientific data reusable across every user rather than duplicated per profile. This normalized, category-agnostic design is also what makes the schema extensible to future product domains without a redesign.

<br/>

## The TruWiz Open Product Database

One of the core long-term ideas behind TruWiz is to open up the underlying data — not just the app experience.

The vision is an **open, community-accessible product intelligence database**: a structured, API-first dataset that developers, researchers, startups, and consumers can query directly, rather than a closed dataset locked behind a single app.

As it matures, the database is intended to expose:

| Data Type | Description |
|---|---|
| Products | Catalog entries across all supported product domains |
| Brands | Structured brand metadata |
| Categories | Product category taxonomy |
| Ingredients | INCI and category-specific ingredient records |
| Scientific Ingredient Profiles | Safety, irritation, comedogenic, and other computed metrics |
| Allergen Mappings | Ingredient-to-allergen relationships |
| Product Ratings | Aggregated and personalized scoring data |
| Ingredient Relationships | Synonyms, interactions, and cross-references |
| Safety Metadata | Supporting evidence behind each score |
| Regulatory Information | *(future)* Region-specific compliance data |

The closest analogy is **OpenFoodFacts** — but where OpenFoodFacts is food-first and largely descriptive, TruWiz aims to layer in deterministic scientific scoring and AI-generated explanations, and to extend that model across cosmetics, food, household, baby care, and pet care products.

The **Public Developer API** listed under [Key Features](#key-features) is the first surface of this vision — it isn't just an integration endpoint bolted onto the app. It's designed to be the access layer for the open database itself, so third-party developers can build their own tools, comparisons, and applications on top of TruWiz's data rather than TruWiz's UI alone.

<br/>

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3 |
| Security | Spring Security, JWT |
| Persistence | Spring Data JPA, Hibernate |
| Database | MySQL |
| API Documentation | Swagger / OpenAPI |
| Containerization | Docker |
| Build Tool | Maven |
| AI / LLM | Gemini / OpenAI |
| Ingredient Extraction | OCR *(planned)* |

<br/>

## Project Structure

```
truwiz-backend/
├── src/
│   ├── main/
│   │   ├── java/com/truwiz/
│   │   │   ├── config/           # Security, Swagger, app configuration
│   │   │   ├── controller/       # REST controllers
│   │   │   ├── service/          # Business logic & scoring engine
│   │   │   ├── repository/       # Spring Data JPA repositories
│   │   │   ├── entity/           # JPA entities
│   │   │   ├── dto/              # Request/response models
│   │   │   ├── security/         # JWT & authentication logic
│   │   │   ├── llm/              # LLM explanation layer integration
│   │   │   └── exception/        # Global exception handling
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/migration/     # Schema migrations
│   └── test/
├── docs/                         # Diagrams, media, documentation assets
├── Dockerfile
├── pom.xml
└── README.md
```

<br/>

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.9+
- MySQL 8+
- Docker (optional, for containerized setup)

### Local Setup

```bash
# Clone the repository
git clone https://github.com/<your-org>/truwiz-backend.git
cd truwiz-backend

# Configure environment variables
cp .env.example .env

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Run with Docker

```bash
docker build -t truwiz-backend .
docker run -p 8080:8080 --env-file .env truwiz-backend
```

The API will be available at `http://localhost:8080`.

<br/>

## Configuration

Key environment variables required to run TruWiz:

| Variable | Description |
|---|---|
| `DB_URL` | MySQL connection string |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `JWT_SECRET` | Secret key used to sign JWTs |
| `JWT_EXPIRATION` | Token expiration time (ms) |
| `LLM_API_KEY` | API key for Gemini/OpenAI explanation layer |
| `MAIL_HOST` / `MAIL_USERNAME` / `MAIL_PASSWORD` | SMTP config for OTP email delivery |

<br/>

## Swagger Documentation

Once the application is running, interactive API documentation is available at:

```
http://localhost:8080/swagger-ui.html
```

The OpenAPI spec is exposed at:

```
http://localhost:8080/v3/api-docs
```

<br/>

## Roadmap

- [ ] OCR-based ingredient extraction from product images
- [ ] Public developer API key management portal
- [ ] Expanded allergy and sensitivity taxonomy
- [ ] Ingredient interaction warnings (multi-ingredient conflicts)
- [ ] Mobile SDK for third-party integrations
- [ ] Open Product Database public release (API-first access)
- [ ] Expansion into Food & Beverage products
- [ ] Expansion into Household & Cleaning products
- [ ] Expansion into Baby Care and Pet Care products
- [ ] Regulatory and compliance metadata layer

<br/>

## Contributing

Contributions are welcome. Please open an issue to discuss significant changes before submitting a pull request.

```bash
# Fork the repo, then:
git checkout -b feature/your-feature-name
git commit -m "Add: your feature"
git push origin feature/your-feature-name
```

Then open a pull request against `main`.

<br/>

Authors
<div align="center"> <table> <tr> <td align="center" width="320"> <br/> <img src="docs/Mohit.jpg" width="110" height="110" style="border-radius:50%; object-fit:cover;" alt="Mohit Singh"/> <br/><br/>

Mohit Singh <br/> <sub>Backend Developer</sub> <br/><br/> <sub>Architected the scoring engine and API layer end to end.</sub> <br/><br/>

<a href="https://www.linkedin.com/in/mohit-singh-37966720b/"> <img src="https://img.shields.io/badge/LinkedIn-Connect-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white" alt="Mohit Singh LinkedIn"/> </a>

<br/><br/>

</td> <td align="center" width="320"> <br/> <img src="docs/Uma.png" width="110" height="110" style="border-radius:50%; object-fit:cover;" alt="Uma Shankar"/> <br/><br/>

Uma Shankar <br/> <sub>UI / UX Design</sub> <br/><br/> <sub>Designed the product experience and visual identity.</sub> <br/><br/>

<a href="https://www.linkedin.com/in/uma-shankar-k/"> <img src="https://img.shields.io/badge/LinkedIn-Connect-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white" alt="Uma Shankar LinkedIn"/> </a> <a href="https://umashankardesign.netlify.app"> <img src="https://img.shields.io/badge/Portfolio-Visit-FF5A5F?style=for-the-badge&logo=aboutdotme&logoColor=white" alt="Uma Shankar Portfolio"/> </a>

<br/><br/>

</td> </tr> </table> </div> <br/>

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

<br/>

<div align="center">
<sub>Built with Java 21 and Spring Boot — scored by science, explained by AI.</sub>
</div>
