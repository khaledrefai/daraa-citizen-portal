import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const Home = () => (
  <div className="home-container">
    <div className="home-hero">
      <h1>
        <Translate contentKey="home.title" />
      </h1>
      <p className="lead">
        <Translate contentKey="home.subtitle" />
      </p>
      <div className="home-brand">
        <img src="assets/images/logo.ai.svg" alt="شعار بوابة درعا" />
        <div>
          <p className="mb-1 fw-bold">بوابة خدمات محافظة درعا</p>
        </div>
      </div>
      <div className="home-actions">
        <Link to="/citizen" className="btn btn-primary btn-lg">
          <FontAwesomeIcon icon="search" />
          <span className="ms-2">
            <Translate contentKey="home.actions.citizen" />
          </span>
        </Link>
      </div>
    </div>
  </div>
);

export default Home;
