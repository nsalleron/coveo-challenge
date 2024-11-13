/// <reference types="Cypress" />

const enterCityName = () => {
  cy.get('input').type('Mont');
};

const displayResultAndNextPage = () => {
  cy.get('[data-testid=search-button]').click();
  cy.get('[data-testid=arrow-right]').click();
};

context('Searchbox', () => {
  beforeEach(() => {
    cy.visit('http://localhost:3000/');
  });

  it('should display a searchbox', () => {
    cy.get('input').should('be.visible').should('have.attr', 'placeholder', 'Search cities');
  });

  it('should display a suggestion', () => {
    enterCityName();

    cy.get('[data-testid=suggestions]').children('li').should('be.visible');
  });

  it('should trigger a search', () => {
    enterCityName();
    cy.get('[data-testid=search-button]').click();
    cy.get('[data-testid=result-list]').children('ol').should('be.visible');
  });

  it('should trigger a location search', () => {
    cy.mockGeolocation();

    enterCityName();

    cy.get('[data-testid=location-button]').click();
    cy.get('[data-testid=search-button]').click();
    cy.get('[data-testid=result-list]').children('ol').should('be.visible');
  });

  it('should allow next paginated page', () => {
    enterCityName();

    displayResultAndNextPage();

    cy.get('[data-testid=count-pages]').contains('2 / 19');
  });

  it('should allow previous paginated page', () => {
    cy.get('input').type('Mont');

    displayResultAndNextPage();
    cy.get('[data-testid=arrow-left]').click();
    cy.get('[data-testid=count-pages]').contains('1 / 19');
  });
});
