import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getCitizenServices } from 'app/entities/citizen-service/citizen-service.reducer';
import { createEntity, getEntity, reset, updateEntity } from './required-document.reducer';

export const RequiredDocumentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const citizenServices = useAppSelector(state => state.citizenService.entities);
  const requiredDocumentEntity = useAppSelector(state => state.requiredDocument.entity);
  const loading = useAppSelector(state => state.requiredDocument.loading);
  const updating = useAppSelector(state => state.requiredDocument.updating);
  const updateSuccess = useAppSelector(state => state.requiredDocument.updateSuccess);

  const handleClose = () => {
    navigate(`/required-document${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCitizenServices({}));
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
    if (values.orderIndex !== undefined && typeof values.orderIndex !== 'number') {
      values.orderIndex = Number(values.orderIndex);
    }

    const entity = {
      ...requiredDocumentEntity,
      ...values,
      service: citizenServices.find(it => it.id.toString() === values.service?.toString()),
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
          ...requiredDocumentEntity,
          service: requiredDocumentEntity?.service?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="citizenServicesApp.requiredDocument.home.createOrEditLabel" data-cy="RequiredDocumentCreateUpdateHeading">
            <Translate contentKey="citizenServicesApp.requiredDocument.home.createOrEditLabel">Create or edit a RequiredDocument</Translate>
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
                  id="required-document-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('citizenServicesApp.requiredDocument.name')}
                id="required-document-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <ValidatedField
                label={translate('citizenServicesApp.requiredDocument.description')}
                id="required-document-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 1000, message: translate('entity.validation.maxlength', { max: 1000 }) },
                }}
              />
              <ValidatedField
                label={translate('citizenServicesApp.requiredDocument.mandatory')}
                id="required-document-mandatory"
                name="mandatory"
                data-cy="mandatory"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('citizenServicesApp.requiredDocument.orderIndex')}
                id="required-document-orderIndex"
                name="orderIndex"
                data-cy="orderIndex"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="required-document-service"
                name="service"
                data-cy="service"
                label={translate('citizenServicesApp.requiredDocument.service')}
                type="select"
              >
                <option value="" key="0" />
                {citizenServices
                  ? citizenServices.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/required-document" replace color="info">
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

export default RequiredDocumentUpdate;
