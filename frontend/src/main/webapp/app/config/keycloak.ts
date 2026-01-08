import Keycloak from 'keycloak-js';
import { Storage } from 'react-jhipster';

const AUTH_TOKEN_KEY = 'jhi-authenticationToken';

const keycloak = new Keycloak({
  url: 'http://173.212.199.139:8090',
  realm: 'daraaGov-internet',
  clientId: 'web-client',
});

const clearStoredToken = () => {
  if (Storage.local.get(AUTH_TOKEN_KEY)) {
    Storage.local.remove(AUTH_TOKEN_KEY);
  }
  if (Storage.session.get(AUTH_TOKEN_KEY)) {
    Storage.session.remove(AUTH_TOKEN_KEY);
  }
};

const persistToken = (token?: string) => {
  clearStoredToken();
  if (token) {
    Storage.session.set(AUTH_TOKEN_KEY, token);
  }
};

export const initKeycloak = async () => {
  try {
    const authenticated = await keycloak.init({
      onLoad: 'check-sso',
      pkceMethod: 'S256',
      checkLoginIframe: false,
      silentCheckSsoRedirectUri: `${window.location.origin}/silent-check-sso.html`,
    });
    persistToken(keycloak.token);

    keycloak.onTokenExpired = async () => {
      try {
        const refreshed = await keycloak.updateToken(30);
        if (refreshed && keycloak.token) {
          persistToken(keycloak.token);
        }
      } catch (error) {
        clearStoredToken();
      }
    };

    return authenticated;
  } catch (error) {
    clearStoredToken();
    return false;
  }
};

export const loginWithKeycloak = (redirectUri?: string) =>
  keycloak.login({ redirectUri: redirectUri ?? window.location.href, prompt: 'login' });
export const logoutFromKeycloak = (redirectUri?: string) => keycloak.logout({ redirectUri: redirectUri ?? window.location.origin });
export const registerWithKeycloak = (redirectUri?: string) => keycloak.register({ redirectUri: redirectUri ?? window.location.origin });

export const getKeycloakInstance = () => keycloak;
