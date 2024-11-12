The commit [#2](https://github.com/nsalleron/coveo-challenge/commit/376b253183f5fc1570bd17e7be490337b1a89276) introduces a major refactor of both the backend and frontend, enhancing the project's structure and functionality, especially for managing city suggestions within an application. I'm sorry for only having a single commit to show you for such a big refactoring, which prevents you from seeing how the refactoring was carried out step by step. I wasn't aware that I needed to submit code, so I panicked a bit and focused on delivering something as quickly as possible. Here is a list of the major changes made to the project.

## 1. Backend Changes

The main changes to the backend include:

- **Refactoring of Model and Service Classes**:
  - **Removal of old classes `City`, `CsvParser`, and `SuggestionsResource`** that previously handled cities and suggestions.
  - **New Classes**:
    - **`CityRecord`**: a model representing a city with simplified fields (id, name, alternative names, latitude, and longitude).
    - **`CityRepository`**: responsible for reading and parsing the CSV file of cities, with more robust resource handling and warnings for duplicates.
    - **`CityService`**: handles the business logic of retrieving and filtering cities based on search parameters, with support for pagination and geographic filtering (latitude and longitude).
    - **`SuggestionsController`**: REST controller, simplified to delegate business logic to `CityService`.
  - **Pagination and Filtering**: The new structure improves pagination in search results, controls the current page, and provides filtering based on geolocation (latitude and longitude).

- **Logs**: Methods use `Logger` for traceability and handle exceptions more precisely, especially for data loading and request errors.

- **Additional Dependencies**:
  - Added OpenAPI with `springdoc-openapi-starter-webmvc-ui` to document and explore the API, facilitating development and testing.
- **Structure**
  - Adopted a Controller-Service-Repository pattern for clearer separation of concerns.
- **Tests**
  - Added unit tests for new classes

## 2. Frontend Changes

The frontend improvements focus on enhancing the user experience and modernizing the project configuration.

- **Cypress Configuration**:
  - New setup to use browser permissions (`cypress-browser-permissions`) for geolocation, enabling simulated location-based searches.
  - End-to-end (`e2e`) and component tests to verify search and pagination functionalities, including validation of search results and navigation between pages.
  - Custom Commands: Added `mockGeolocation` command to simulate geolocation during tests.
- **Dependency Updates**:
  - **React**: 16 -> 18
  - **Jest**: New Jest configuration for unit tests. 
  - **Lodash / React Router / React Query / Testing Library **
- **Removal of CSS Files**
  - Replaced CSS files with Tailwind CSS, offering a more flexible and maintainable styling approach.
- **Structure**
  - Adopted feature-based grouping for components, even though there is currently only one main feature.
- **Hooks**
  - Developed new hooks, such as debounce and objectState, to optimize network call management and local component state handling.
- **Testing Improvements**
  - Added tests for all components to ensure functionality across the frontend.
    