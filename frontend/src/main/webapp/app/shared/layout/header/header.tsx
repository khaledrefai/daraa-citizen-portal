import './header.scss';
import React, { useState } from 'react';
import { Storage } from 'react-jhipster';
import { Collapse, Nav, Navbar, NavbarToggler, Container } from 'reactstrap';
import LoadingBar from 'react-redux-loading-bar';

import { useAppDispatch } from 'app/config/store';
import { setLocale } from 'app/shared/reducers/locale';
import { AccountMenu, AdminMenu, EntitiesMenu, LocaleMenu } from '../menus';
import { Brand, CitizenPortalLink, Home } from './header-components';

export interface IHeaderProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isOpenAPIEnabled: boolean;
  currentLocale: string;
}

const Header = (props: IHeaderProps) => {
  const [menuOpen, setMenuOpen] = useState(false);

  const dispatch = useAppDispatch();

  const handleLocaleChange = event => {
    const langKey = event.target.value;
    Storage.session.set('locale', langKey);
    dispatch(setLocale(langKey));
  };

  const toggleMenu = () => setMenuOpen(!menuOpen);

  return (
    <div id="app-header">
      <LoadingBar className="loading-bar" />
      {/* تم التغيير إلى expand="lg" لضمان ظهور زر القائمة عند 768px */}
      <Navbar data-cy="navbar" dark expand="lg" fixed="top" className="jh-navbar">
        <Container fluid className="d-flex justify-content-between align-items-center">
          <Brand />

          <NavbarToggler aria-label="Menu" onClick={toggleMenu} />

          <Collapse isOpen={menuOpen} navbar id="responsive-navbar-nav">
            {/* إضافة كلاس custom-nav للتحكم الدقيق في المسافات */}
            <Nav id="header-tabs" navbar className="ms-auto custom-nav">
              <Home />
              <CitizenPortalLink />
              {props.isAuthenticated && <EntitiesMenu />}
              {props.isAuthenticated && props.isAdmin && <AdminMenu showOpenAPI={props.isOpenAPIEnabled} />}
              <AccountMenu isAuthenticated={props.isAuthenticated} />
              <LocaleMenu currentLocale={props.currentLocale} onClick={handleLocaleChange} />
            </Nav>
          </Collapse>
        </Container>
      </Navbar>
    </div>
  );
};

export default Header;
