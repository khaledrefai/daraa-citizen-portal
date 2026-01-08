import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/directorate">
        <Translate contentKey="global.menu.entities.directorate" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/service-category">
        <Translate contentKey="global.menu.entities.serviceCategory" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/citizen-service">
        <Translate contentKey="global.menu.entities.citizenService" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/required-document">
        <Translate contentKey="global.menu.entities.requiredDocument" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/service-form-template">
        <Translate contentKey="global.menu.entities.serviceFormTemplate" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
