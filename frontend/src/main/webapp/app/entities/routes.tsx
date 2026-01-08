import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Directorate from './directorate';
import ServiceCategory from './service-category';
import CitizenService from './citizen-service';
import RequiredDocument from './required-document';
import ServiceFormTemplate from './service-form-template';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="directorate/*" element={<Directorate />} />
        <Route path="service-category/*" element={<ServiceCategory />} />
        <Route path="citizen-service/*" element={<CitizenService />} />
        <Route path="required-document/*" element={<RequiredDocument />} />
        <Route path="service-form-template/*" element={<ServiceFormTemplate />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
