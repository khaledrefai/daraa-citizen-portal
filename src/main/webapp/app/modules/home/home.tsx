import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Badge, Card, CardBody, Col, Row } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <div className="home-container">
      <div className="home-hero">
        <p className="home-eyebrow">
          <Translate contentKey="home.eyebrow" />
        </p>
        <h1>
          <Translate contentKey="home.title" />
        </h1>
        <p className="lead">
          <Translate contentKey="home.subtitle" />
        </p>
        <div className="home-actions">
          <Link to="/citizen" className="btn btn-primary btn-lg">
            <FontAwesomeIcon icon="search" />
            <span className="ms-2">
              <Translate contentKey="home.actions.citizen" />
            </span>
          </Link>
          <Link to="/login" className="btn btn-outline-light btn-lg">
            <FontAwesomeIcon icon="lock" />
            <span className="ms-2">
              <Translate contentKey="home.actions.admin" />
            </span>
          </Link>
        </div>
        <div className="home-badges">
          <Badge color="info">
            <Translate contentKey="home.badges.curated" />
          </Badge>
          <Badge color="success">
            <Translate contentKey="home.badges.realtime" />
          </Badge>
          <Badge color="warning">
            <Translate contentKey="home.badges.arabic" />
          </Badge>
        </div>
      </div>

      <Row className="g-4">
        <Col md="7">
          <Card className="shadow-sm">
            <CardBody>
              <h2 className="h4 mb-3">
                <Translate contentKey="home.citizenCard.title" />
              </h2>
              <p className="text-muted">
                <Translate contentKey="home.citizenCard.subtitle" />
              </p>
              <ul className="home-list">
                <li>
                  <FontAwesomeIcon icon="th-list" className="text-primary" />
                  <span className="ms-2">
                    <Translate contentKey="home.citizenCard.items.catalog" />
                  </span>
                </li>
                <li>
                  <FontAwesomeIcon icon="road" className="text-primary" />
                  <span className="ms-2">
                    <Translate contentKey="home.citizenCard.items.steps" />
                  </span>
                </li>
                <li>
                  <FontAwesomeIcon icon="cloud" className="text-primary" />
                  <span className="ms-2">
                    <Translate contentKey="home.citizenCard.items.downloads" />
                  </span>
                </li>
              </ul>
              <Link to="/citizen" className="btn btn-primary">
                <Translate contentKey="home.citizenCard.cta" />
              </Link>
            </CardBody>
          </Card>
        </Col>
        <Col md="5">
          <Card className="shadow-sm h-100">
            <CardBody>
              <h2 className="h4 mb-3">
                <Translate contentKey="home.adminCard.title" />
              </h2>
              <p className="text-muted">
                <Translate contentKey="home.adminCard.subtitle" />
              </p>
              {account?.login ? (
                <div className="alert alert-success">
                  <FontAwesomeIcon icon="user" />
                  <span className="ms-2">
                    <Translate contentKey="home.adminCard.logged" interpolate={{ username: account.login }} />
                  </span>
                </div>
              ) : (
                <div className="alert alert-info">
                  <FontAwesomeIcon icon="lock" />
                  <span className="ms-2">
                    <Translate contentKey="home.adminCard.loginHint" />
                  </span>
                </div>
              )}
              <Link to="/login" className="btn btn-outline-primary">
                <Translate contentKey="home.adminCard.cta" />
              </Link>
            </CardBody>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Home;
