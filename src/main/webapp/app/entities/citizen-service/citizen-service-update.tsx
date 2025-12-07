import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getServiceCategories } from 'app/entities/service-category/service-category.reducer';
import { EstimatedTimeUnit } from 'app/shared/model/enumerations/estimated-time-unit.model';
import { createEntity, getEntity, reset, updateEntity } from './citizen-service.reducer';

export const CitizenServiceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const serviceCategories = useAppSelector(state => state.serviceCategory.entities);
  const citizenServiceEntity = useAppSelector(state => state.citizenService.entity);
  const loading = useAppSelector(state => state.citizenService.loading);
  const updating = useAppSelector(state => state.citizenService.updating);
  const updateSuccess = useAppSelector(state => state.citizenService.updateSuccess);
  const estimatedTimeUnitValues = Object.keys(EstimatedTimeUnit);

  const handleClose = () => {
    navigate(`/citizen-service${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getServiceCategories({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.estimatedDuration !== undefined && typeof values.estimatedDuration !== 'number') {
      values.estimatedDuration = Number(values.estimatedDuration);
    }

    const entity = {
      ...citizenServiceEntity,
      ...values,
      category: serviceCategories.find(it => it.id.toString() === values.category?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          estimatedDurationUnit: 'MINUTE',
          ...citizenServiceEntity,
          category: citizenServiceEntity?.category?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="citizenServicesApp.citizenService.home.createOrEditLabel" data-cy="CitizenServiceCreateUpdateHeading">
            <Translate contentKey="citizenServicesApp.citizenService.home.createOrEditLabel">Create or edit a CitizenService</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="citizen-service-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('citizenServicesApp.citizenService.code')}
                id="citizen-service-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('citizenServicesApp.citizenService.name')}
                id="citizen-service-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <ValidatedField
                label={translate('citizenServicesApp.citizenService.description')}
                id="citizen-service-description"
                name="description"
                data-cy="description"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('citizenServicesApp.citizenService.estimatedDuration')}
                id="citizen-service-estimatedDuration"
                name="estimatedDuration"
                data-cy="estimatedDuration"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('citizenServicesApp.citizenService.estimatedDurationUnit')}
                id="citizen-service-estimatedDurationUnit"
                name="estimatedDurationUnit"
                data-cy="estimatedDurationUnit"
                type="select"
              >
                {estimatedTimeUnitValues.map(estimatedTimeUnit => (
                  <option value={estimatedTimeUnit} key={estimatedTimeUnit}>
                    {translate(`citizenServicesApp.EstimatedTimeUnit.${estimatedTimeUnit}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('citizenServicesApp.citizenService.requiresPhysicalPresence')}
                id="citizen-service-requiresPhysicalPresence"
                name="requiresPhysicalPresence"
                data-cy="requiresPhysicalPresence"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('citizenServicesApp.citizenService.isElectronic')}
                id="citizen-service-isElectronic"
                name="isElectronic"
                data-cy="isElectronic"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('citizenServicesApp.citizenService.hasSmartCard')}
                id="citizen-service-hasSmartCard"
                name="hasSmartCard"
                data-cy="hasSmartCard"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('citizenServicesApp.citizenService.feesDescription')}
                id="citizen-service-feesDescription"
                name="feesDescription"
                data-cy="feesDescription"
                type="text"
                validate={{
                  maxLength: { value: 1000, message: translate('entity.validation.maxlength', { max: 1000 }) },
                }}
              />
              <ValidatedField
                label={translate('citizenServicesApp.citizenService.active')}
                id="citizen-service-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <ValidatedField
                id="citizen-service-category"
                name="category"
                data-cy="category"
                label={translate('citizenServicesApp.citizenService.category')}
                type="select"
              >
                <option value="" key="0" />
                {serviceCategories
                  ? serviceCategories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/citizen-service" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CitizenServiceUpdate;
