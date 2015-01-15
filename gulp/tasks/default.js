var gulp = require('gulp');

gulp.task('build', ['clean', 'vendor', 'browserify', 'images', 'stylus']);

gulp.task('default', ['build']);