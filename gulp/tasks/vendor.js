var gulp         = require('gulp');
var sourcemaps   = require('gulp-sourcemaps');
var concat       = require('gulp-concat');
var _            = require('ramda');

var concatThis = _.compose(_.forEach(function(config) {
  gulp.src(config.src)
      .pipe(concat(config.outputName))
      .pipe(gulp.dest(config.dest));
}), _.values);

gulp.task('vendor', function() {
  concatThis(require('../config').vendor);
});
