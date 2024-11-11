Cypress.Commands.add('mockGeolocation', (latitude = 39.48508, longitude = -80.14258) => {
    cy.window().then(($window) =>  {
        cy.stub($window.navigator.geolocation, 'getCurrentPosition').callsFake((callback) => {
            return callback({ coords: { latitude, longitude } });
        })
    });
});