import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './citizen-service.reducer';

export const CitizenServiceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const citizenServiceEntity = useAppSelector(state => state.citizenService.entity);
  return (
    <Row className="justify-content-center">
      <Col md="8" lg="8" className="entity-detail-card">
        <h2 data-cy="citizenServiceDetailsHeading">
          <Translate contentKey="citizenServicesApp.citizenService.detail.title">CitizenService</Translate>
        </h2>
        <dl className="jh-entity-details">
          {/*
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{citizenServiceEntity.id}</dd>
          */}
          <dt>
            <span id="name">
              <Translate contentKey="citizenServicesApp.citizenService.name">Name</Translate>
            </span>
          </dt>
          <dd className="fw-bold text-primary">{citizenServiceEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="citizenServicesApp.citizenService.description">Description</Translate>
            </span>
          </dt>
          <dd>{citizenServiceEntity.description}</dd>
          <dt>
            <Translate contentKey="citizenServicesApp.citizenService.estimatedDuration">Duration</Translate>
          </dt>
          <dd>
            {citizenServiceEntity.estimatedDuration} {citizenServiceEntity.estimatedDurationUnit}
          </dd>
          <dt>
            <span id="requiresPhysicalPresence">
              <Translate contentKey="citizenServicesApp.citizenService.requiresPhysicalPresence">Requires Physical Presence</Translate>
            </span>
          </dt>
          <dd>{citizenServiceEntity.requiresPhysicalPresence ? 'true' : 'false'}</dd>
          {/*
          <dt>
            <span id="isElectronic">
              <Translate contentKey="citizenServicesApp.citizenService.isElectronic">Is Electronic</Translate>
            </span>
          </dt>
          <dd>{citizenServiceEntity.isElectronic ? 'true' : 'false'}</dd>
          */}
          <dt>
            <span id="hasSmartCard">
              <Translate contentKey="citizenServicesApp.citizenService.hasSmartCard">Has Smart Card</Translate>
            </span>
          </dt>
          <dd>{citizenServiceEntity.hasSmartCard ? 'true' : 'false'}</dd>
          <dt>
            <span id="feesDescription">
              <Translate contentKey="citizenServicesApp.citizenService.feesDescription">Fees Description</Translate>
            </span>
          </dt>
          <dd>{citizenServiceEntity.feesDescription}</dd>
          {/*
          <dt>
            <span id="serviceLink">
              <Translate contentKey="citizenServicesApp.citizenService.serviceLink">Service Link</Translate>
            </span>
          </dt>
          <dd>
            {citizenServiceEntity.serviceLink ? (
              <a href={citizenServiceEntity.serviceLink} target="_blank" rel="noopener noreferrer">
                {citizenServiceEntity.serviceLink}
              </a>
            ) : (
              ''
            )}
          </dd>
          */}
          <dt>
            <span id="active">
              <Translate contentKey="citizenServicesApp.citizenService.active">Active</Translate>
            </span>
          </dt>
          <dd>
            <span className={`badge ${citizenServiceEntity.active ? 'bg-success' : 'bg-secondary'}`}>
              {citizenServiceEntity.active ? 'Active' : 'Inactive'}
            </span>
          </dd>
          <dt>
            <Translate contentKey="citizenServicesApp.citizenService.category">Category</Translate>
          </dt>
          <dd>{citizenServiceEntity.category ? citizenServiceEntity.category.id : ''}</dd>
        </dl>
        <div className="btn-group flex-btn-group-container mt-4">
          <Button tag={Link} to="/citizen-service" replace color="info" data-cy="entityDetailsBackButton">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/citizen-service/${citizenServiceEntity.id}/edit`} replace color="primary">
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

export default CitizenServiceDetail;
