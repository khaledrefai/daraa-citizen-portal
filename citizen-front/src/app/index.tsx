import React from 'react';
import { createRoot } from 'react-dom/client';
import { TranslatorContext } from 'react-jhipster';

import { loadIcons } from 'app/config/icon-loader';
import App from 'app/app';

// Initialize icons
loadIcons();

// Initialize i18n
TranslatorContext.setDefaultLocale('ar-ly');
TranslatorContext.registerTranslations('ar-ly', {});

// Load translations dynamically
const loadTranslations = async () => {
  try {
    const response = await fetch('/i18n/ar-ly.json');
    const translations = await response.json();
    TranslatorContext.registerTranslations('ar-ly', translations);
  } catch (e) {
    console.error('Failed to load translations', e);
  }
};

const rootEl = document.getElementById('root');
const root = createRoot(rootEl!);

loadTranslations().then(() => {
  root.render(<App />);
});
