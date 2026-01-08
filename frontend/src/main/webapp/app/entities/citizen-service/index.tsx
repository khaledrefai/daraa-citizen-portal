import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CitizenService from './citizen-service';
import CitizenServiceDetail from './citizen-service-detail';
import CitizenServiceUpdate from './citizen-service-update';
import CitizenServiceDeleteDialog from './citizen-service-delete-dialog';

const CitizenServiceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CitizenService />} />
    <Route path="new" element={<CitizenServiceUpdate />} />
    <Route path=":id">
      <Route index element={<CitizenServiceDetail />} />
      <Route path="edit" element={<CitizenServiceUpdate />} />
      <Route path="delete" element={<CitizenServiceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CitizenServiceRoutes;
