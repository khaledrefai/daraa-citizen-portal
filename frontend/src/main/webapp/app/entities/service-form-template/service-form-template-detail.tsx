import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, byteSize, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './service-form-template.reducer';

export const ServiceFormTemplateDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const serviceFormTemplateEntity = useAppSelector(state => state.serviceFormTemplate.entity);
  return (
    <Row className="justify-content-center">
      <Col md="10" lg="8" className="entity-detail-card">
        <h2 data-cy="serviceFormTemplateDetailsHeading">
          <Translate contentKey="citizenServicesApp.serviceFormTemplate.detail.title">ServiceFormTemplate</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{serviceFormTemplateEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="citizenServicesApp.serviceFormTemplate.name">Name</Translate>
            </span>
          </dt>
          <dd>{serviceFormTemplateEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="citizenServicesApp.serviceFormTemplate.description">Description</Translate>
            </span>
          </dt>
          <dd>{serviceFormTemplateEntity.description}</dd>
          <dt>
            <span id="file">
              <Translate contentKey="citizenServicesApp.serviceFormTemplate.file">File</Translate>
            </span>
          </dt>
          <dd>
            {serviceFormTemplateEntity.file ? (
              <div className="d-flex flex-column">
                <div>
                  {serviceFormTemplateEntity.fileContentType ? (
                    <a
                      className="text-primary font-weight-bold"
                      style={{ cursor: 'pointer' }}
                      onClick={openFile(serviceFormTemplateEntity.fileContentType, serviceFormTemplateEntity.file)}
                    >
                      <FontAwesomeIcon icon="eye" /> <Translate contentKey="entity.action.open">Open</Translate>
                    </a>
                  ) : null}
                </div>
                <small className="text-muted">
                  {serviceFormTemplateEntity.fileContentType}, {byteSize(serviceFormTemplateEntity.file)}
                </small>
              </div>
            ) : (
              <span className="text-muted">No file attached</span>
            )}
          </dd>
          <dt>
            <span id="active">
              <Translate contentKey="citizenServicesApp.serviceFormTemplate.active">Active</Translate>
            </span>
          </dt>
          <dd>{serviceFormTemplateEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <span id="orderIndex">
              <Translate contentKey="citizenServicesApp.serviceFormTemplate.orderIndex">Order Index</Translate>
            </span>
          </dt>
          <dd>{serviceFormTemplateEntity.orderIndex}</dd>
          <dt>
            <Translate contentKey="citizenServicesApp.serviceFormTemplate.service">Service</Translate>
          </dt>
          <dd>{serviceFormTemplateEntity.service ? serviceFormTemplateEntity.service.id : ''}</dd>
        </dl>

        <div className="btn-group flex-btn-group-container">
          <Button tag={Link} to="/service-form-template" replace color="info" data-cy="entityDetailsBackButton">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          <Button tag={Link} to={`/service-form-template/${serviceFormTemplateEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </div>
      </Col>
    </Row>
  );
};

export default ServiceFormTemplateDetail;
