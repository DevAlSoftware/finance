/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        spartan: {
          50: '#faf8f5',
          100: '#f5f0e8',
          200: '#e8ddd0',
          300: '#d4c2a8',
          400: '#b89a7a',
          500: '#8b5a3c',
          600: '#6d4328',
          700: '#553320',
          800: '#3d2417',
          900: '#2d1a10',
        },
        bronze: {
          50: '#fdf8f0',
          100: '#faefd8',
          200: '#f5ddb0',
          300: '#edc87e',
          400: '#e3ab4a',
          500: '#d9971a',
          600: '#c08014',
          700: '#9e6613',
          800: '#805317',
          900: '#6b4615',
        },
        crimson: {
          50: '#fef2f2',
          100: '#fee2e2',
          200: '#fecaca',
          300: '#fca5a5',
          400: '#f87171',
          500: '#dc2626',
          600: '#b91c1c',
          700: '#991b1b',
          800: '#7f1d1d',
          900: '#6b1d1d',
        },
        primary: {
          50: '#faf8f5',
          100: '#f5f0e8',
          200: '#e8ddd0',
          300: '#d4c2a8',
          400: '#b89a7a',
          500: '#8b5a3c',
          600: '#6d4328',
          700: '#553320',
          800: '#3d2417',
          900: '#2d1a10',
        },
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
      },
      spacing: {
        '18': '4.5rem',
        '88': '22rem',
      },
      boxShadow: {
        'spartan': '0 4px 6px -1px rgba(109, 67, 40, 0.3), 0 2px 4px -1px rgba(109, 67, 40, 0.2)',
        'spartan-lg': '0 10px 15px -3px rgba(109, 67, 40, 0.3), 0 4px 6px -2px rgba(109, 67, 40, 0.2)',
      },
    },
  },
  plugins: [],
}

