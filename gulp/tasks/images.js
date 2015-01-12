var gulp         = require('gulp');
var sourcemaps   = require('gulp-sourcemaps');
var handleErrors = require('../util/handle-errors');
var config       = require('../config').images;

gulp.task('images', function() {
  return gulp.src(config.src)
    .on("error", handleErrors)
    .pipe(gulp.dest(config.dest))
});