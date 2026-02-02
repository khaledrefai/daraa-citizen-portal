const webpackMerge = require('webpack-merge').merge;
const SimpleProgressWebpackPlugin = require('simple-progress-webpack-plugin');
const sass = require('sass');
const postcssRTLCSS = require('postcss-rtlcss');

const utils = require('./utils.js');
const commonConfig = require('./webpack.common.js');

const ENV = 'development';

module.exports = async options =>
  webpackMerge(await commonConfig({ env: ENV }), {
    devtool: 'cheap-module-source-map',
    mode: ENV,
    entry: ['./src/app/index'],
    output: {
      path: utils.root('target/classes/static/'),
      filename: '[name].[contenthash:8].js',
      chunkFilename: '[name].[chunkhash:8].chunk.js',
    },
    optimization: {
      moduleIds: 'named',
    },
    module: {
      rules: [
        {
          test: /\.(sa|sc|c)ss$/,
          use: [
            'style-loader',
            {
              loader: 'css-loader',
              options: { url: false },
            },
            {
              loader: 'postcss-loader',
              options: {
                postcssOptions: {
                  plugins: [postcssRTLCSS()],
                },
              },
            },
            {
              loader: 'sass-loader',
              options: { implementation: sass },
            },
          ],
        },
      ],
    },
    devServer: {
      hot: true,
      host: '0.0.0.0',
      port: 9005,
      proxy: [
        {
          context: ['/api', '/services', '/management', '/v3/api-docs'],
          target: 'http://localhost:8084',
          secure: false,
        },
      ],
      historyApiFallback: true,
    },
    stats: process.env.JHI_DISABLE_WEBPACK_LOGS ? 'none' : options.stats,
    plugins: [
      process.env.JHI_DISABLE_WEBPACK_LOGS
        ? null
        : new SimpleProgressWebpackPlugin({
            format: options.stats === 'minimal' ? 'compact' : 'expanded',
          }),
    ].filter(Boolean),
  });
