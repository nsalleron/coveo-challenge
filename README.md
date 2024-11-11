The latest commit [#2](https://github.com/nsalleron/coveo-challenge/commit/376b253183f5fc1570bd17e7be490337b1a89276) introduces a major refactor of both the backend and frontend, enhancing the project's structure and functionality, especially for managing city suggestions within an application.

I'm sorry for only having a single commit to show you, which prevents you from seeing how the refactoring was carried out step by step. I wasn't aware that I needed to submit code, so I panicked a bit and focused on delivering something as quickly as possible. Here is a list of the major changes made to the project.

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

## 2. Frontend Changes

The frontend improvements focus on enhancing the user experience and modernizing the project configuration.

- **Cypress Configuration**:
  - New setup to use browser permissions (`cypress-browser-permissions`) for geolocation, enabling simulated location-based searches.
  - End-to-end (`e2e`) and component tests to verify search and pagination functionalities, including validation of search results and navigation between pages.
  - Custom Commands: Added `mockGeolocation` command to simulate geolocation during tests.

- **Dependency Updates**:
  - **Jest**: New Jest configuration for unit tests using modern environments.
  - **TypeScript and Babel**: Updated support for TypeScript and Babel with modern presets for improved compatibility with React and TypeScript.
 
- **Removal of CSS Files**
  - previous css files are removed and tailwindcss was added to the project.
    
- **Project reoganisation**
  - TODO

- 

## 3. Summary of Improvements

This commit brings significant enhancements in terms of:

- **Modularity**: Separation of responsibilities into dedicated classes for better maintainability.
- **Testing and Quality**: E2E, unit tests, and Cypress support to better validate functionality.
- **User Experience**: Optimized pagination and geolocation support in search.

Overall, this commit modernizes the codebase and improves the quality, structure, and reliability of the search features within the application&#8203;:contentReference[oaicite:0]{index=0}.
