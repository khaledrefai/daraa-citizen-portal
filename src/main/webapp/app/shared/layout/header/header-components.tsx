import React from 'react';
import { Translate } from 'react-jhipster';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/logo.ai.svg" alt="شعار بوابة درعا" />
  </div>
);

export const Brand = () => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    <BrandIcon />
    <span className="brand-title">
      <Translate contentKey="global.title">بوابة خدمات المواطنين</Translate>
    </span>
    <span className="navbar-version">{VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`}</span>
  </NavbarBrand>
);

export const Home = () => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span>
        <Translate contentKey="global.menu.home">الصفحة الرئيسية</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const CitizenPortalLink = () => (
  <NavItem>
    <NavLink tag={Link} to="/citizen" className="d-flex align-items-center">
      <FontAwesomeIcon icon="tasks" />
      <span>
        <Translate contentKey="global.menu.citizenPortal">دليل المواطنين</Translate>
      </span>
    </NavLink>
  </NavItem>
);
