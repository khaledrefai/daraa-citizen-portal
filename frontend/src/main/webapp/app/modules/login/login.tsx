import React from 'react';
import { Link, Navigate, useLocation } from 'react-router-dom';
import { Alert, Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, translate } from 'react-jhipster';
import { type FieldError, useForm } from 'react-hook-form';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { login } from 'app/shared/reducers/authentication';

export const Login = () => {
  const dispatch = useAppDispatch();
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const loginError = useAppSelector(state => state.authentication.loginError);

  const pageLocation = useLocation();
  const { from } = pageLocation.state || { from: { pathname: '/', search: pageLocation.search ?? '' } };

  const {
    handleSubmit,
    register,
    formState: { errors, touchedFields },
  } = useForm({ mode: 'onTouched' });

  const handleLogin = ({ username, password, rememberMe }) => {
    dispatch(login(username, password, rememberMe));
  };

  const handleLoginSubmit = e => {
    handleSubmit(handleLogin)(e);
  };

  if (isAuthenticated) {
    return <Navigate to={from} replace />;
  }

  return (
    <Row className="justify-content-center">
      <Col md="6">
        <h1 id="login-title" data-cy="loginTitle">
          <Translate contentKey="login.title">Sign in</Translate>
        </h1>
        <p className="lead">
          <Translate contentKey="login.subtitle">Sign in with your account</Translate>
        </p>
        {loginError ? (
          <Alert color="danger" data-cy="loginError">
            <Translate contentKey="login.messages.error.authentication">
              <strong>Failed to sign in!</strong> Please check your credentials and try again.
            </Translate>
          </Alert>
        ) : null}
        <form onSubmit={handleLoginSubmit}>
          <ValidatedField
            name="username"
            label={translate('global.form.username.label')}
            placeholder={translate('global.form.username.placeholder')}
            required
            autoFocus
            data-cy="username"
            validate={{ required: 'Username cannot be empty!' }}
            register={register}
            error={errors.username as FieldError}
            isTouched={touchedFields.username}
          />
          <ValidatedField
            name="password"
            type="password"
            label={translate('login.form.password')}
            placeholder={translate('login.form.password.placeholder')}
            required
            data-cy="password"
            validate={{ required: 'Password cannot be empty!' }}
            register={register}
            error={errors.password as FieldError}
            isTouched={touchedFields.password}
          />
          <ValidatedField
            name="rememberMe"
            type="checkbox"
            check
            label={translate('login.form.rememberme')}
            value={true}
            register={register}
          />

          <Button color="primary" type="submit" data-cy="submit" className="mt-3">
            <Translate contentKey="login.form.button">Sign in</Translate>
          </Button>
        </form>
        <div className="mt-4">
          <Alert color="warning">
            <Translate contentKey="login.password.forgot">Did you forget your password?</Translate>{' '}
            <Link className="alert-link" to="/account/reset/request">
              <Translate contentKey="login.password.link">Reset your password</Translate>
            </Link>
          </Alert>
          <Alert color="warning">
            <span>
              <Translate contentKey="global.messages.info.register.noaccount">You don&apos;t have an account yet?</Translate>
            </span>{' '}
            <Link className="alert-link" to="/account/register">
              <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
            </Link>
          </Alert>
        </div>
      </Col>
    </Row>
  );
};

export default Login;
