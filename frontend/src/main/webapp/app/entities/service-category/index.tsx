import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ServiceCategory from './service-category';
import ServiceCategoryDetail from './service-category-detail';
import ServiceCategoryUpdate from './service-category-update';
import ServiceCategoryDeleteDialog from './service-category-delete-dialog';

const ServiceCategoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ServiceCategory />} />
    <Route path="new" element={<ServiceCategoryUpdate />} />
    <Route path=":id">
      <Route index element={<ServiceCategoryDetail />} />
      <Route path="edit" element={<ServiceCategoryUpdate />} />
      <Route path="delete" element={<ServiceCategoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ServiceCategoryRoutes;
