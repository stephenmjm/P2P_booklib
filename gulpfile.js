var gulp = require('gulp');

var coffee = require('gulp-coffee');
var clean = require('gulp-clean');
var stylus = require('gulp-stylus');

gulp.task('copy-bower-components', function () {
  gulp.src('bower_components/**')
    .pipe(gulp.dest('src/main/webapp/bower_components'));
});

gulp.task('compile-coffee-script', function () {
  gulp.src('src/main/coffee/**/*.coffee')
    .pipe(coffee())
    .pipe(gulp.dest('src/main/webapp/scripts/'));
});

gulp.task('copy-html-files', function () {
  gulp.src('src/main/coffee/**/*.html')
    .pipe(gulp.dest('src/main/webapp/scripts/'));
});

gulp.task('default', ['copy-bower-components', 'compile-coffee-script', 'copy-html-files']);

