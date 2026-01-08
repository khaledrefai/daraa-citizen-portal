import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './directorate.reducer';

export const DirectorateDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const directorateEntity = useAppSelector(state => state.directorate.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="directorateDetailsHeading">
          <Translate contentKey="citizenServicesApp.directorate.detail.title">Directorate</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{directorateEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="citizenServicesApp.directorate.code">Code</Translate>
            </span>
          </dt>
          <dd>{directorateEntity.code}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="citizenServicesApp.directorate.name">Name</Translate>
            </span>
          </dt>
          <dd>{directorateEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="citizenServicesApp.directorate.description">Description</Translate>
            </span>
          </dt>
          <dd>{directorateEntity.description}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="citizenServicesApp.directorate.active">Active</Translate>
            </span>
          </dt>
          <dd>{directorateEntity.active ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/directorate" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/directorate/${directorateEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DirectorateDetail;
