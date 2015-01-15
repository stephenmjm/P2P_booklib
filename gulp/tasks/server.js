var gulp = require('gulp');
var server = require('gulp-connect').server;
var config = require('../config').server;

gulp.task('server',  ['clean', 'vendor', 'browserify', 'images'], function() {
  server(config.development);
});