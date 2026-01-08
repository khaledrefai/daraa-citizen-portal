import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Directorate from './directorate';
import DirectorateDetail from './directorate-detail';
import DirectorateUpdate from './directorate-update';
import DirectorateDeleteDialog from './directorate-delete-dialog';

const DirectorateRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Directorate />} />
    <Route path="new" element={<DirectorateUpdate />} />
    <Route path=":id">
      <Route index element={<DirectorateDetail />} />
      <Route path="edit" element={<DirectorateUpdate />} />
      <Route path="delete" element={<DirectorateDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DirectorateRoutes;
