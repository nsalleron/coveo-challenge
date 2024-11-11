import { defineConfig } from 'cypress'
const { cypressBrowserPermissionsPlugin } = require('cypress-browser-permissions')

export default defineConfig({
  env: {
    browserPermissions: {
      geolocation: "allow",
    },
  },
  e2e: {
    setupNodeEvents(on, config) {
      config = cypressBrowserPermissionsPlugin(on, config)
      return config
    },
  },
})

