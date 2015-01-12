var gulp = require('gulp');

gulp.task('default', ['clean', 'vendor', 'browserify', 'stylus', 'images']);
