import directorate from 'app/entities/directorate/directorate.reducer';
import serviceCategory from 'app/entities/service-category/service-category.reducer';
import citizenService from 'app/entities/citizen-service/citizen-service.reducer';
import requiredDocument from 'app/entities/required-document/required-document.reducer';
import serviceFormTemplate from 'app/entities/service-form-template/service-form-template.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  directorate,
  serviceCategory,
  citizenService,
  requiredDocument,
  serviceFormTemplate,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
