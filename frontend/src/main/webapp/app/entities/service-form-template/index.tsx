import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ServiceFormTemplate from './service-form-template';
import ServiceFormTemplateDetail from './service-form-template-detail';
import ServiceFormTemplateUpdate from './service-form-template-update';
import ServiceFormTemplateDeleteDialog from './service-form-template-delete-dialog';

const ServiceFormTemplateRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ServiceFormTemplate />} />
    <Route path="new" element={<ServiceFormTemplateUpdate />} />
    <Route path=":id">
      <Route index element={<ServiceFormTemplateDetail />} />
      <Route path="edit" element={<ServiceFormTemplateUpdate />} />
      <Route path="delete" element={<ServiceFormTemplateDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ServiceFormTemplateRoutes;
