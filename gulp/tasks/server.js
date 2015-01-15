var gulp = require('gulp');
var server = require('gulp-connect').server;

gulp.task('server',  ['clean', 'vendor', 'browserify', 'images'], function() {
  server({
    root: 'src/main/webapp',
    livereload: true
  })
});