import React, { useEffect, useState } from 'react';
import { Translate, ValidatedField, translate } from 'react-jhipster';
import { Alert, Button, Col, Row } from 'reactstrap';
import { Link } from 'react-router-dom';
import { type FieldError, useForm } from 'react-hook-form';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { handleRegister, reset } from './register.reducer';

export const RegisterPage = () => {
  const dispatch = useAppDispatch();
  const currentLocale = useAppSelector(state => state.locale.currentLocale);
  const registrationSuccess = useAppSelector(state => state.register.registrationSuccess);
  const registrationFailure = useAppSelector(state => state.register.registrationFailure);
  const errorMessage = useAppSelector(state => state.register.errorMessage);

  const [doNotMatch, setDoNotMatch] = useState(false);

  const {
    handleSubmit,
    register,
    reset: resetForm,
    formState: { errors, touchedFields },
  } = useForm({ mode: 'onTouched' });

  useEffect(() => {
    dispatch(reset());
    return () => {
      dispatch(reset());
      resetForm();
    };
  }, [dispatch, resetForm]);

  const handleValidSubmit = ({ username, email, firstPassword, secondPassword }) => {
    if (firstPassword !== secondPassword) {
      setDoNotMatch(true);
    } else {
      setDoNotMatch(false);
      dispatch(handleRegister({ login: username, email, password: firstPassword, langKey: currentLocale || 'en' }));
    }
  };

  const handleRegisterSubmit = e => {
    handleSubmit(handleValidSubmit)(e);
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1 id="register-title" data-cy="registerTitle">
            <Translate contentKey="register.title">Registration</Translate>
          </h1>
          <Alert color="success" isOpen={registrationSuccess} data-cy="registerSuccess">
            <Translate contentKey="register.messages.success">Registration saved!</Translate>
          </Alert>
          <Alert color="danger" isOpen={registrationFailure} data-cy="registerError">
            <Translate contentKey="register.messages.error.fail">Registration failed!</Translate>{' '}
            {errorMessage && <span className="fw-semibold">{errorMessage}</span>}
          </Alert>
          <Alert color="danger" isOpen={doNotMatch} data-cy="doNotMatch">
            <Translate contentKey="global.messages.error.dontmatch">The password and its confirmation do not match!</Translate>
          </Alert>

          <form onSubmit={handleRegisterSubmit}>
            <ValidatedField
              name="username"
              label={translate('global.form.username.label')}
              placeholder={translate('global.form.username.placeholder')}
              required
              data-cy="username"
              validate={{
                required: 'Username is required.',
                pattern: {
                  value: /^[_.@A-Za-z0-9-]*$/,
                  message: translate('register.messages.validate.login.pattern'),
                },
                minLength: {
                  value: 1,
                  message: translate('register.messages.validate.login.minlength'),
                },
                maxLength: {
                  value: 50,
                  message: translate('register.messages.validate.login.maxlength'),
                },
              }}
              register={register}
              error={errors.username as FieldError}
              isTouched={touchedFields.username}
            />
            <ValidatedField
              name="email"
              label={translate('global.form.email.label')}
              placeholder={translate('global.form.email.placeholder')}
              required
              data-cy="email"
              type="email"
              validate={{
                required: 'Email is required.',
                minLength: { value: 5, message: translate('global.messages.validate.email.minlength') },
                maxLength: { value: 254, message: translate('global.messages.validate.email.maxlength') },
              }}
              register={register}
              error={errors.email as FieldError}
              isTouched={touchedFields.email}
            />
            <ValidatedField
              name="firstPassword"
              type="password"
              label={translate('global.form.newpassword.label')}
              placeholder={translate('global.form.newpassword.placeholder')}
              required
              data-cy="firstPassword"
              validate={{
                required: 'Password is required.',
                minLength: { value: 4, message: translate('global.messages.validate.newpassword.minlength') },
                maxLength: { value: 50, message: translate('global.messages.validate.newpassword.maxlength') },
              }}
              register={register}
              error={errors.firstPassword as FieldError}
              isTouched={touchedFields.firstPassword}
            />
            <ValidatedField
              name="secondPassword"
              type="password"
              label={translate('global.form.confirmpassword.label')}
              placeholder={translate('global.form.confirmpassword.placeholder')}
              required
              data-cy="secondPassword"
              validate={{
                required: 'Confirmation is required.',
                minLength: { value: 4, message: translate('global.messages.validate.confirmpassword.minlength') },
                maxLength: { value: 50, message: translate('global.messages.validate.confirmpassword.maxlength') },
              }}
              register={register}
              error={errors.secondPassword as FieldError}
              isTouched={touchedFields.secondPassword}
            />
            <Button color="primary" type="submit" data-cy="submit">
              <Translate contentKey="register.form.button">Register</Translate>
            </Button>
          </form>
          <p className="mt-3">
            <Translate contentKey="register.haveAccount">Already have an account?</Translate>{' '}
            <Link to="/login">
              <Translate contentKey="register.link.signin">Sign in</Translate>
            </Link>
          </p>
        </Col>
      </Row>
    </div>
  );
};

export default RegisterPage;
