var gulp    = require('gulp');
var rimraf  = require('rimraf');
var _       = require('ramda');
var config  = require('../config').clean;

gulp.task('clean', function () {
    _.forEach(function(config) {
      rimraf(config, function(){});
    })(config);
});