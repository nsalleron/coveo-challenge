/** @type {import('tailwindcss').Config} */
const { brown } = require('tailwindcss/colors');
module.exports = {
  content: ['./src/**/*.{js,jsx,ts,tsx}'],
  theme: {
    extend: {
      colors: {
        charcoal: '#282c34',
        brown: brown,
      },
    },
  },
  plugins: [],
};
