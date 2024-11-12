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
  - **Pagination and Filtering**: The new structure improves pagination in search results, controls the current page, and provides filtering based on geolocation (latitude and longitude) + radius + page size.
- **Logs**: Methods use `Logger` for traceability and handle exceptions more precisely, especially for data loading and request errors.
- **RequestMapping to PostMapping**: To be more precise in the request type we make and the return Content-type
- **Remove default value for lat and lng**: To have more results corresponding to the input of the user.
- **Checks**:
  - page < 0
  - pageSize <= 0
- **Stream**:
  - Filter / map
- **Repository**: Parsing at the beginning of the app. Only necessary fields are parsed. Warn if there is some duplicated keys
- Use of Data class
- **Additional Dependencies**:
  - Added OpenAPI with `springdoc-openapi-starter-webmvc-ui` to document and explore the API, facilitating development and testing.
- **Structure**
  - Adopted a Controller-Service-Repository pattern for clearer separation of concerns.
- **Tests**
  - Added unit tests all the classes

## 2. Frontend Changes

The frontend improvements focus on enhancing the user experience and modernizing the project configuration.

- **Cypress Configuration**:
  - New setup to use browser permissions (`cypress-browser-permissions`) for geolocation, enabling simulated location-based searches.
  - End-to-end (`e2e`) and component tests to verify search and pagination functionalities, including validation of search results and navigation between pages.
  - Custom Commands: Added `mockGeolocation` command to simulate geolocation during tests.
- **Dependency Updates**:
  - **React**: 16 -> 18
  - **Jest**: New Jest configuration for unit tests. 
  - **Lodash / React Router / React Query / Testing Library**
- **Removal of CSS Files**
  - Replaced CSS files with Tailwind CSS, offering a more flexible and maintainable styling approach.
- **Structure**
  - Adopted feature-based grouping for components, even though there is currently only one main feature.
- **Hooks**
  - Developed new hooks, such as debounce and objectState, to optimize network call management and local component state handling.
- **Testing Improvements**
  - Added tests for all components to ensure functionality across the frontend.


## 3. Proposed Release Plan

**Release 1.0.1:**
- **Bugfix:** Refactor repository - Retrieve and parse code at bean instantiation, parsing only the relevant fields. Added repository-level tests and warnings for duplicate keys.
- **Bugfix:** Controller-level validation of page values: if the page is below 0, set it to 0.
- **Tests:** Black-box input/output tests at the Controller level.
- **Bugfix:** Fix Cypress tests for automated release.

- **Feature:** Search by alternative names.
- **Feature:** Geolocation button - it's a best practice not to request user geolocation unnecessarily due to privacy concerns.
- **Feature:** Error message display in case of server connection loss.
- **Feature:** Display of alternative names.

**Release 1.0.2:**
- **Bugfix / Refactor:** Change from `RequestMapping` to `PostMapping`.
- **Tech / Refactor:** Add logs via `loggerFactory`.
- **Refactor:** Create a service with separation of concerns, establishing smaller methods for each action + service-level testing.

- **Feature:** Remove default values for latitude/longitude for more results.
- **Feature:** Add filters in the search bar to allow sharing of search links.
- **Feature:** Integrate React Router + add an error page for invalid URLs.
- **Feature:** Country filter and corresponding tests.
- **Feature:** Display of admins.

**Release 1.0.3:**
- **Refactor:** Use streams for filtering, mapping, etc.
- **Refactor:** Java data class records for reduced boilerplate code.
- **Tech:** Add configurations for Prettier, Jest, and Cypress (TS version).

- **Feature:** Threshold radius options of 5 / 10 / 20 / 40 / 100 km.
- **Feature (TODO):** Filter admins and test.
- **Feature (TODO):** Display population and test.
- **Feature (TODO):** Display modification date and test.

**Release 1.0.4:**
- **Tech:** Update dependencies (React 16 => 18).
- **Tech:** Migrate CSS files to TailwindCSS.
- **Tech:** Add hooks `useDebounce`, `useObjectState`, `useApiSearch` for refactoring and optimisation.

- **Feature (TODO):** Custom definition of page SIZE.
- **Feature (TODO):** Display elevation.
- **Feature (TODO):** Multi-criteria filters.
