import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './required-document.reducer';

export const RequiredDocumentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const requiredDocumentEntity = useAppSelector(state => state.requiredDocument.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="requiredDocumentDetailsHeading">
          <Translate contentKey="citizenServicesApp.requiredDocument.detail.title">RequiredDocument</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{requiredDocumentEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="citizenServicesApp.requiredDocument.name">Name</Translate>
            </span>
          </dt>
          <dd>{requiredDocumentEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="citizenServicesApp.requiredDocument.description">Description</Translate>
            </span>
          </dt>
          <dd>{requiredDocumentEntity.description}</dd>
          <dt>
            <span id="mandatory">
              <Translate contentKey="citizenServicesApp.requiredDocument.mandatory">Mandatory</Translate>
            </span>
          </dt>
          <dd>{requiredDocumentEntity.mandatory ? 'true' : 'false'}</dd>
          <dt>
            <span id="orderIndex">
              <Translate contentKey="citizenServicesApp.requiredDocument.orderIndex">Order Index</Translate>
            </span>
          </dt>
          <dd>{requiredDocumentEntity.orderIndex}</dd>
          <dt>
            <Translate contentKey="citizenServicesApp.requiredDocument.service">Service</Translate>
          </dt>
          <dd>{requiredDocumentEntity.service ? requiredDocumentEntity.service.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/required-document" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/required-document/${requiredDocumentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RequiredDocumentDetail;
