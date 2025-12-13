import React, { useEffect, useMemo, useRef, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormGroup, Input, Label, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getServiceCategories } from 'app/entities/service-category/service-category.reducer';
import { IRequiredDocument } from 'app/shared/model/required-document.model';
import { IServiceFormTemplate } from 'app/shared/model/service-form-template.model';
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
  const [requiredDocuments, setRequiredDocuments] = useState<IRequiredDocument[]>([]);
  const [formTemplates, setFormTemplates] = useState<IServiceFormTemplate[]>([]);
  const newEntityDefaults = useRef({
    estimatedDurationUnit: 'MINUTE',
    requiresPhysicalPresence: false,
    isElectronic: false,
    hasSmartCard: false,
    active: false,
  });

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
    if (isNew) {
      setRequiredDocuments([]);
      setFormTemplates([]);
    } else {
      setRequiredDocuments(citizenServiceEntity.requiredDocuments ?? []);
      setFormTemplates(citizenServiceEntity.formTemplates ?? []);
    }
  }, [citizenServiceEntity, isNew]);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const normalizeNumberValue = (value: any) => (value === '' || value === null || value === undefined ? undefined : Number(value));
  const normalizeValue = (value: any) => (value === '' ? undefined : value);

  const addRequiredDocument = () => setRequiredDocuments([...requiredDocuments, { mandatory: false }]);

  const updateRequiredDocument = (index: number, field: keyof IRequiredDocument, value: any) => {
    const updated = [...requiredDocuments];
    const parsedValue = field === 'orderIndex' ? normalizeNumberValue(value) : normalizeValue(value);
    updated[index] = { ...updated[index], [field]: parsedValue };
    setRequiredDocuments(updated);
  };

  const removeRequiredDocument = (index: number) => {
    const updated = [...requiredDocuments];
    updated.splice(index, 1);
    setRequiredDocuments(updated);
  };

  const addFormTemplate = () => setFormTemplates([...formTemplates, { active: true }]);

  const updateFormTemplate = (index: number, field: keyof IServiceFormTemplate, value: any) => {
    const updated = [...formTemplates];
    const parsedValue = field === 'orderIndex' ? normalizeNumberValue(value) : normalizeValue(value);
    updated[index] = { ...updated[index], [field]: parsedValue };
    setFormTemplates(updated);
  };

  const removeFormTemplate = (index: number) => {
    const updated = [...formTemplates];
    updated.splice(index, 1);
    setFormTemplates(updated);
  };

  const onTemplateFileChange = (index: number, event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) {
      updateFormTemplate(index, 'file', undefined);
      updateFormTemplate(index, 'fileContentType', undefined);
      return;
    }

    const reader = new FileReader();
    reader.onload = e => {
      const base64Result = (e.target?.result as string) ?? '';
      const base64 = base64Result.split(',').pop();
      updateFormTemplate(index, 'file', base64);
      updateFormTemplate(index, 'fileContentType', file.type);
    };
    reader.readAsDataURL(file);
  };

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.estimatedDuration !== undefined && typeof values.estimatedDuration !== 'number') {
      values.estimatedDuration = Number(values.estimatedDuration);
    }

    const sanitizedRequiredDocuments = requiredDocuments.filter(doc => doc.name);
    const sanitizedFormTemplates = formTemplates.filter(template => template.name && template.file);

    const entity = {
      ...citizenServiceEntity,
      ...values,
      requiredDocuments: sanitizedRequiredDocuments,
      formTemplates: sanitizedFormTemplates,
      category: serviceCategories.find(it => it.id.toString() === values.category?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = useMemo(() => {
    if (isNew) {
      return newEntityDefaults.current;
    }
    return {
      estimatedDurationUnit: 'MINUTE',
      ...citizenServiceEntity,
      category: citizenServiceEntity?.category?.id,
    };
  }, [citizenServiceEntity?.id, citizenServiceEntity?.category?.id, isNew]);

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
            <ValidatedForm defaultValues={defaultValues} onSubmit={saveEntity}>
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
              <hr className="my-4" />
              <div className="d-flex justify-content-between align-items-center mb-2">
                <h4 className="mb-0">
                  <Translate contentKey="citizenServicesApp.requiredDocument.home.title">Required Documents</Translate>
                </h4>
                <Button color="secondary" size="sm" onClick={addRequiredDocument}>
                  <FontAwesomeIcon icon="plus" /> <Translate contentKey="entity.action.add">Add</Translate>
                </Button>
              </div>
              {requiredDocuments.length === 0 ? (
                <p className="text-muted">No required documents added yet.</p>
              ) : (
                requiredDocuments.map((doc, index) => (
                  <Row className="align-items-end mb-3" key={doc.id ?? `required-document-${index}`}>
                    <Col md="4">
                      <FormGroup>
                        <Label for={`required-doc-name-${index}`}>
                          <Translate contentKey="citizenServicesApp.requiredDocument.name">Name</Translate>
                        </Label>
                        <Input
                          id={`required-doc-name-${index}`}
                          type="text"
                          value={doc.name ?? ''}
                          required
                          onChange={event => updateRequiredDocument(index, 'name', event.target.value)}
                        />
                      </FormGroup>
                    </Col>
                    <Col md="4">
                      <FormGroup>
                        <Label for={`required-doc-description-${index}`}>
                          <Translate contentKey="citizenServicesApp.requiredDocument.description">Description</Translate>
                        </Label>
                        <Input
                          id={`required-doc-description-${index}`}
                          type="text"
                          value={doc.description ?? ''}
                          onChange={event => updateRequiredDocument(index, 'description', event.target.value)}
                        />
                      </FormGroup>
                    </Col>
                    <Col md="2">
                      <FormGroup check className="mt-4">
                        <Input
                          id={`required-doc-mandatory-${index}`}
                          type="checkbox"
                          className="form-check-input"
                          checked={doc.mandatory ?? false}
                          onChange={event => updateRequiredDocument(index, 'mandatory', event.target.checked)}
                        />
                        <Label for={`required-doc-mandatory-${index}`} check>
                          <Translate contentKey="citizenServicesApp.requiredDocument.mandatory">Mandatory</Translate>
                        </Label>
                      </FormGroup>
                    </Col>
                    <Col md="2">
                      <FormGroup>
                        <Label for={`required-doc-order-${index}`}>
                          <Translate contentKey="citizenServicesApp.requiredDocument.orderIndex">Order Index</Translate>
                        </Label>
                        <Input
                          id={`required-doc-order-${index}`}
                          type="number"
                          min="0"
                          value={doc.orderIndex ?? ''}
                          onChange={event => updateRequiredDocument(index, 'orderIndex', event.target.value)}
                        />
                      </FormGroup>
                    </Col>
                    <Col md="12" className="d-flex justify-content-end">
                      <Button color="danger" size="sm" onClick={() => removeRequiredDocument(index)}>
                        <FontAwesomeIcon icon="trash" /> <Translate contentKey="entity.action.remove">Remove</Translate>
                      </Button>
                    </Col>
                  </Row>
                ))
              )}
              <hr className="my-4" />
              <div className="d-flex justify-content-between align-items-center mb-2">
                <h4 className="mb-0">
                  <Translate contentKey="citizenServicesApp.serviceFormTemplate.home.title">Service Form Templates</Translate>
                </h4>
                <Button color="secondary" size="sm" onClick={addFormTemplate}>
                  <FontAwesomeIcon icon="plus" /> <Translate contentKey="entity.action.add">Add</Translate>
                </Button>
              </div>
              {formTemplates.length === 0 ? (
                <p className="text-muted">No form templates added yet.</p>
              ) : (
                formTemplates.map((template, index) => (
                  <Row className="align-items-end mb-3" key={template.id ?? `service-form-template-${index}`}>
                    <Col md="4">
                      <FormGroup>
                        <Label for={`form-template-name-${index}`}>
                          <Translate contentKey="citizenServicesApp.serviceFormTemplate.name">Name</Translate>
                        </Label>
                        <Input
                          id={`form-template-name-${index}`}
                          type="text"
                          required
                          value={template.name ?? ''}
                          onChange={event => updateFormTemplate(index, 'name', event.target.value)}
                        />
                      </FormGroup>
                    </Col>
                    <Col md="4">
                      <FormGroup>
                        <Label for={`form-template-description-${index}`}>
                          <Translate contentKey="citizenServicesApp.serviceFormTemplate.description">Description</Translate>
                        </Label>
                        <Input
                          id={`form-template-description-${index}`}
                          type="text"
                          value={template.description ?? ''}
                          onChange={event => updateFormTemplate(index, 'description', event.target.value)}
                        />
                      </FormGroup>
                    </Col>
                    <Col md="4">
                      <FormGroup>
                        <Label for={`form-template-file-${index}`}>
                          <Translate contentKey="citizenServicesApp.serviceFormTemplate.file">File</Translate>
                        </Label>
                        <Input
                          id={`form-template-file-${index}`}
                          type="file"
                          required={!template.file}
                          onChange={event => onTemplateFileChange(index, event)}
                        />
                        {template.fileContentType ? <small className="text-muted">{template.fileContentType}</small> : null}
                      </FormGroup>
                    </Col>
                    <Col md="3">
                      <FormGroup check className="mt-4">
                        <Input
                          id={`form-template-active-${index}`}
                          type="checkbox"
                          className="form-check-input"
                          checked={template.active ?? false}
                          onChange={event => updateFormTemplate(index, 'active', event.target.checked)}
                        />
                        <Label for={`form-template-active-${index}`} check>
                          <Translate contentKey="citizenServicesApp.serviceFormTemplate.active">Active</Translate>
                        </Label>
                      </FormGroup>
                    </Col>
                    <Col md="3">
                      <FormGroup>
                        <Label for={`form-template-order-${index}`}>
                          <Translate contentKey="citizenServicesApp.serviceFormTemplate.orderIndex">Order Index</Translate>
                        </Label>
                        <Input
                          id={`form-template-order-${index}`}
                          type="number"
                          min="0"
                          value={template.orderIndex ?? ''}
                          onChange={event => updateFormTemplate(index, 'orderIndex', event.target.value)}
                        />
                      </FormGroup>
                    </Col>
                    <Col md="12" className="d-flex justify-content-end">
                      <Button color="danger" size="sm" onClick={() => removeFormTemplate(index)}>
                        <FontAwesomeIcon icon="trash" /> <Translate contentKey="entity.action.remove">Remove</Translate>
                      </Button>
                    </Col>
                  </Row>
                ))
              )}
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
