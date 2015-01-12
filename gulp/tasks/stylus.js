var browserSync = require('browser-sync');
var gulp        = require('gulp');
var stylus      = require('gulp-stylus');
var concat      = require('gulp-concat');
var nib         = require('nib');
var sourcemaps  = require('gulp-sourcemaps');
var config      = require('../config').stylus;

gulp.task('stylus', function () {
  gulp.src(config.src)
      .pipe(stylus({use: nib()}))
      .pipe(concat(config.outputName))
      .pipe(gulp.dest(config.dest))
      .pipe(browserSync.reload({stream:true}));
});