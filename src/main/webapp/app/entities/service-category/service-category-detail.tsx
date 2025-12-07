import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './service-category.reducer';

export const ServiceCategoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const serviceCategoryEntity = useAppSelector(state => state.serviceCategory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="serviceCategoryDetailsHeading">
          <Translate contentKey="citizenServicesApp.serviceCategory.detail.title">ServiceCategory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{serviceCategoryEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="citizenServicesApp.serviceCategory.code">Code</Translate>
            </span>
          </dt>
          <dd>{serviceCategoryEntity.code}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="citizenServicesApp.serviceCategory.name">Name</Translate>
            </span>
          </dt>
          <dd>{serviceCategoryEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="citizenServicesApp.serviceCategory.description">Description</Translate>
            </span>
          </dt>
          <dd>{serviceCategoryEntity.description}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="citizenServicesApp.serviceCategory.active">Active</Translate>
            </span>
          </dt>
          <dd>{serviceCategoryEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="citizenServicesApp.serviceCategory.directorate">Directorate</Translate>
          </dt>
          <dd>{serviceCategoryEntity.directorate ? serviceCategoryEntity.directorate.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/service-category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/service-category/${serviceCategoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ServiceCategoryDetail;
