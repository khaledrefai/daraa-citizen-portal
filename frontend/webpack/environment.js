module.exports = {
  // APP_VERSION is passed as an environment variable from the Gradle / Maven build tasks.
  VERSION: process.env.APP_VERSION || 'DEV',
  // The root URL for API calls, ending with a '/' - for example: `"https://www.jhipster.tech:8081/myservice/"`.
  // If this URL is left empty (""), then it will be relative to the current context.
  // If you use an API server, in `prod` mode, you will need to enable CORS
  // (see the `jhipster.cors` common JHipster property in the `application-*.yml` configurations)
  // Use relative URLs when using webpack proxy (development), or absolute URL for production
  // The webpack proxy in webpack.dev.js forwards /api/* to http://173.212.199.139:8084
  SERVER_API_URL: process.env.SERVER_API_URL || '',
};
