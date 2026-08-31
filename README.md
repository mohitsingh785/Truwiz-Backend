# TruWiz Backend

Production REST API for a cosmetic ingredient safety intelligence platform built with Java 21 and Spring Boot.

TruWiz processes ingredient formulations against individual user health profiles—skin type, hair type, and declared allergens—to compute deterministic safety ratings. Unlike applications using opaque static scores, TruWiz decouples mathematical scoring from natural language explanations: a rule-based engine calculates the risk ratings, while an LLM layer generates plain-English summaries from structured JSON outputs.

---

##  Core Engine Highlights

* **Multi-Parametric Scoring:** Evaluates ingredients across 8 scientific metrics (Safety, Irritation, Comedogenic, Hydration, Sebum Control, Hair Benefit, Allergy Risk, Data Confidence).


* **Positional Concentration Weighting:** Factors in ingredient label position (`ingredient_order`) to weight formula impact. Ingredients at the top of the label carry higher risk weightings than trace additives.


* **Allergen & Profile Matching Engine:** Walks relational mappings (`product` $\rightarrow$ `product_ingredient` $\rightarrow$ `inci_ingredient` $\rightarrow$ `inci_allergy_mapping`) to intersect formulas directly with user profiles.


* **Isolated LLM Execution Layer:** The LLM receives structured JSON outputs from the scoring pipeline to generate summaries. It cannot modify scores, override ratings, or invent risks.


* **Guided Packaging OCR Pipeline:** Features a fallback flow for uncatalogued products: captures packaging images, extracts ingredients via OCR, computes a preliminary rating, and queues the product for verification.


* **Public Developer API:** Supports API client keys to expose structured product, INCI ingredient, and allergen mapping datasets to third-party developers.



---

## System Architecture

```
                                  [ Mobile Client / Public API Client ]
                                                    │
                                                    ▼
                                  [ Spring Security (JWT / API Key) ]
                                                    │
                                                    ▼
                                     [ REST Controllers / OpenAPI ]
                                                    │
                                                    ▼
                                             [ Service Layer ]
                                ┌───────────────────┼───────────────────┐
                                │                   │                   │
                        [ Auth & OTP ]      [ Catalog Services ]    [ Scoring Engine ]
                                                                        │
                                                                        ▼
                                                             [ LLM Explanation Layer ]
                                                                        │
                                                                        ▼
                                                          [ Spring Data JPA / Hibernate ]
                                                                        │
                                                                        ▼
                                                             [ MySQL Relational DB ]

```

### Analysis Execution Pipeline

```
┌─────────────────┐     ┌──────────────────┐     ┌──────────────────┐     ┌─────────────────┐
│ Product Lookup  │ ──► │ Extract Label    │ ──► │ INCI DB &        │ ──► │ Calculate       │
│ (Barcode/Search)│     │ Ingredients      │     │ Synonym Mapping  │     │ Ingredient Risk │
└─────────────────┘     └──────────────────┘     └──────────────────┘     └────────┬────────┘
                                                                                   │
┌─────────────────┐     ┌──────────────────┐     ┌──────────────────┐              │
│ Personalized    │ ◄── │ LLM Summarizer   │ ◄── │ Weighted Product │ ◄─────────────┘
│ Safety Report   │     │ (Plain Text)     │     │ & Profile Score  │
└─────────────────┘     └──────────────────┘     └──────────────────┘

```

---

## Database Schema

The following ER diagram represents the relational database used by TruWiz.

<p align="center">
  <img src="docs/images/er-diagram.svg" alt="TruWiz Database ER Diagram" width="1000">
</p>

```


---

##  Tech Stack

* **Language:** Java 21 LTS


* **Framework:** Spring Boot 3.x


* **Security:** Spring Security + Stateless JWT


* **ORM:** Spring Data JPA / Hibernate


* **Database:** MySQL 8.0+


* **Documentation & Tooling:** OpenAPI 3.0 / Swagger UI, Maven, Lombok


##  Getting Started

### Prerequisites

* JDK 21
* Maven 3.8+
* MySQL 8.0+

### Installation & Run

1. **Clone repository:**
```bash
git clone https://github.com/your-org/truwiz-backend.git
cd truwiz-backend

```


2. **Configure Database Connection:**
Set environment variables or edit `src/main/resources/application.yml`:
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


3. **Build the Application:**
```bash
mvn clean package -DskipTests

```


4. **Launch Application:**
```bash
mvn spring-boot:run

```


5. **API Documentation:**
Access the Swagger UI at `http://localhost:8080/swagger-ui.html`.
