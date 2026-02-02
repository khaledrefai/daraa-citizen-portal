import './app.scss';

import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';

import CitizenPortalHome from 'app/citizen-portal/citizen-portal-home';
import CitizenServiceDetails from 'app/citizen-portal/citizen-service-details';
import Footer from 'app/shared/layout/footer/footer';

const App = () => {
  return (
    <BrowserRouter>
      <div className="app-container" dir="rtl">
        <div className="view-container">
          <Routes>
            <Route path="/" element={<CitizenPortalHome />} />
            <Route path="/services/:id" element={<CitizenServiceDetails />} />
          </Routes>
        </div>
        <Footer />
      </div>
    </BrowserRouter>
  );
};

export default App;
