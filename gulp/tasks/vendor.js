var gulp         = require('gulp');
var sourcemaps   = require('gulp-sourcemaps');
var concat       = require('gulp-concat');
var _            = require('ramda');

var concatThis = function(config) {
  gulp.src(config.src)
      .pipe(concat(config.outputName))
      .pipe(gulp.dest(config.dest));
}

var concatAll = _.compose(_.forEach(concatThis), _.filter(_.is(Object)), _.values);

gulp.task('vendor', function() {
  concatAll(require('../config').vendor);
});
