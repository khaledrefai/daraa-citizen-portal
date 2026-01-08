import React, { useLayoutEffect } from 'react';

import { useAppDispatch } from 'app/config/store';
import { logout } from 'app/shared/reducers/authentication';

export const Logout = () => {
  const dispatch = useAppDispatch();

  useLayoutEffect(() => {
    dispatch(logout());
    window.location.href = '/';
  }, [dispatch]);

  return (
    <div className="p-5">
      <h4>Logged out successfully!</h4>
    </div>
  );
};

export default Logout;
