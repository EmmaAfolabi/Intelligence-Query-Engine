# Queryable Intelligence Engine

This project upgrades the Profile Intelligence Service into a Queryable Intelligence Engine for advanced segmenting, identifying patterns, and querying large datasets. Built with Spring Boot 3 & Java 25.

## Table of Contents
1. [Overview](#overview)
2. [Data Seeding Mechanism](#data-seeding-mechanism)
3. [Features](#features)
4. [API Endpoints](#api-endpoints)
5. [Error Handling](#error-handling)

## Overview
A functional robust REST API utilizing the Spring Data JPA Criteria API to dynamically filter, sort, and paginate profile records efficiently. An interactive rule-based Natural Language Processing (NLP) filter evaluates natural English queries and extracts the respective filter metrics.

## Data Seeding Mechanism
On initialization, the `DataSeeder` configuration utilizes `ObjectMapper` to parse `profiles.data`. It intelligently provisions elements with missing requirements:
- Injecting an auto-generated strict `UUID v7` primary key per record (leveraging `uuid-creator`).
- Inserting `Instant.now()` for `created_at` matching Iso-8601 formatting.
- Safely avoiding duplicate insertion by enforcing conditional inserts with an internal `UNIQUE` name check matching the entity's database constraint.

## Features
- **Dynamic Combined Filtering**: Filter precisely by `min_age`, `max_age`, `country_id`, `gender` etc.
- **Rule-Based NLP Search**: Enter descriptive plain English strings (`?q=young males from nigeria`) directly mapping dynamically onto database criteria.
- **Auto-Sorting & Pagination**: Full declarative pagination structure and configurable sequential layout (`sort_by`, `order`, `page`, `limit`).
- **Global Error Protection**: Predictable structured `{ "status": "error", ... }` objects with corresponding status code allocations (400, 422, etc.).

## API Endpoints

### 1. Advanced Filtering
**GET** `/api/profiles`
- Query Params:
  - `gender`: Filter by gender (e.g. `male`)
  - `age_group`: Filter by precise group (`child`, `teenager`, `adult`, `senior`)
  - `country_id`: ISO 2-letter Code (`NG`, `UK`)
  - `min_age` / `max_age`: Numeric boundaries
  - `min_gender_probability` / `min_country_probability`: Float thresholds
  - `sort_by` / `order`: `age`|`created_at`|`gender_probability` and `asc`|`desc`

### 2. Natural Language Query
**GET** `/api/profiles/search?q={query}`
- Evaluates queries like:
  - *"young males"* -> Converts to `gender=male` + `min_age=16` + `max_age=24`
  - *"females above 30"* -> `gender=female` + `min_age=30`
  - *"people from angola"* -> Extrapolates mapping to `country_id=AO`

## Error Handling
Standard output format utilized via Spring `@ControllerAdvice`:
```json
{
  "status": "error",
  "message": "Unable to interpret query"
}
```
Available mappings: `400 Bad Request`, `422 Unprocessable Entity`, `404 Not Found`, `500 Server Error`.
