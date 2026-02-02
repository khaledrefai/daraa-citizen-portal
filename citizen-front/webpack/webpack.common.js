const path = require('path');
const webpack = require('webpack');
const { merge } = require('webpack-merge');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ForkTsCheckerWebpackPlugin = require('fork-ts-checker-webpack-plugin');
const { hashElement } = require('folder-hash');
const MergeJsonWebpackPlugin = require('merge-jsons-webpack-plugin');
const utils = require('./utils.js');
const environment = require('./environment');

const getTsLoaderRule = () => {
  return [
    {
      loader: 'thread-loader',
      options: {
        workers: require('os').cpus().length - 1,
      },
    },
    {
      loader: 'ts-loader',
      options: {
        transpileOnly: true,
        happyPackMode: true,
      },
    },
  ];
};

module.exports = async options => {
  const development = options.env === 'development';
  const languagesHash = await hashElement(path.resolve(__dirname, '../src/app/i18n'), {
    algo: 'md5',
    encoding: 'hex',
    files: { include: ['*.json'] },
  });

  return merge({
    cache: {
      type: 'filesystem',
      cacheDirectory: path.resolve(__dirname, '../target/webpack'),
      buildDependencies: {
        config: [
          __filename,
          path.resolve(__dirname, `webpack.${development ? 'dev' : 'prod'}.js`),
          path.resolve(__dirname, 'environment.js'),
          path.resolve(__dirname, 'utils.js'),
          path.resolve(__dirname, '../postcss.config.js'),
          path.resolve(__dirname, '../tsconfig.json'),
        ],
      },
    },
    resolve: {
      extensions: ['.js', '.jsx', '.ts', '.tsx', '.json'],
      modules: ['node_modules'],
      alias: utils.mapTypescriptAliasToWebpackAlias(),
      fallback: {
        path: require.resolve('path-browserify'),
      },
    },
    module: {
      rules: [
        {
          test: /\.tsx?$/,
          use: getTsLoaderRule(options.env),
          include: [utils.root('./src/app')],
          exclude: [utils.root('node_modules')],
        },
      ],
    },
    stats: {
      children: false,
    },
    plugins: [
      new webpack.EnvironmentPlugin({
        LOG_LEVEL: development ? 'info' : 'error',
      }),
      new webpack.DefinePlugin({
        I18N_HASH: JSON.stringify(languagesHash.hash),
        DEVELOPMENT: JSON.stringify(development),
        VERSION: JSON.stringify(environment.VERSION),
        SERVER_API_URL: JSON.stringify(environment.SERVER_API_URL),
      }),
      new ForkTsCheckerWebpackPlugin(),
      new CopyWebpackPlugin({
        patterns: [
          { from: './assets/', to: 'assets/' },
          { from: './content/', to: 'content/' },
        ],
      }),
      new HtmlWebpackPlugin({
        template: './index.html',
        chunksSortMode: 'auto',
        inject: 'body',
        base: '/',
      }),
      new MergeJsonWebpackPlugin({
        output: {
          groupBy: [
            { pattern: './src/app/i18n/ar-ly/*.json', fileName: './i18n/ar-ly.json' },
          ],
        },
      }),
    ],
  });
};
