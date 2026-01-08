import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import RequiredDocument from './required-document';
import RequiredDocumentDetail from './required-document-detail';
import RequiredDocumentUpdate from './required-document-update';
import RequiredDocumentDeleteDialog from './required-document-delete-dialog';

const RequiredDocumentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<RequiredDocument />} />
    <Route path="new" element={<RequiredDocumentUpdate />} />
    <Route path=":id">
      <Route index element={<RequiredDocumentDetail />} />
      <Route path="edit" element={<RequiredDocumentUpdate />} />
      <Route path="delete" element={<RequiredDocumentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RequiredDocumentRoutes;
