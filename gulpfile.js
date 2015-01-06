var gulp = require('gulp');

var coffee = require('gulp-coffee');
var clean = require('gulp-clean');
var stylus = require('gulp-stylus');
var concat = require('gulp-concat');

gulp.task('clean-bower-components', function () {
  return gulp.src('src/main/webapp/bower_components/*', {read: false})
    .pipe(clean());
});

gulp.task('copy-bower-components', ['clean-bower-components'], function () {
  gulp.src('bower_components/**')
    .pipe(gulp.dest('src/main/webapp/bower_components'));
});

gulp.task('clean-scripts', function () {
  return gulp.src('src/main/webapp/scripts/*', {read: false})
    .pipe(clean());
});

gulp.task('compile-coffee-script', ['clean-scripts'], function () {
  gulp.src('src/main/coffee/**/*.coffee')
    .pipe(coffee())
    .pipe(concat('main.js'))
    .pipe(gulp.dest('src/main/webapp/scripts/'));
});

gulp.task('copy-html-files', function () {
  gulp.src('src/main/coffee/**/*.html')
    .pipe(gulp.dest('src/main/webapp/scripts/'));
});

gulp.watch('src/main/coffee/**/*.coffee', ['compile-coffee-script']);

gulp.task('default', ['copy-bower-components', 'compile-coffee-script', 'copy-html-files']);

