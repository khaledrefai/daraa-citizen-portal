import React from 'react';
import { Translate } from 'react-jhipster';
import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="assets/images/logo.ai.svg" alt="شعار بوابة درعا" />
  </div>
);

export const Brand = () => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    <BrandIcon />
    <span className="brand-text">
      <span className="brand-title">بوابة الخدمات الرقمية</span>
      <span className="brand-subtitle">محافظة درعا</span>
    </span>
  </NavbarBrand>
);

export const Home = () => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span>
        <Translate contentKey="global.menu.home" />
      </span>
    </NavLink>
  </NavItem>
);

export const CitizenPortalLink = () => (
  <NavItem>
    <NavLink tag={Link} to="/citizen" className="d-flex align-items-center">
      <FontAwesomeIcon icon="tasks" />
      <span>
        <Translate contentKey="global.menu.citizenPortal" />
      </span>
    </NavLink>
  </NavItem>
);
