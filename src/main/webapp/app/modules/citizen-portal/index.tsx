import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CitizenPortalHome from './citizen-portal-home';
import CitizenServiceDetails from './citizen-service-details';

export default function CitizenPortalRoutes() {
  return (
    <ErrorBoundaryRoutes>
      <Route index element={<CitizenPortalHome />} />
      <Route path="services/:id" element={<CitizenServiceDetails />} />
    </ErrorBoundaryRoutes>
  );
}
