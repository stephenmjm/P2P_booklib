var browserify   = require('browserify');
var browserSync  = require('browser-sync');
var watchify     = require('watchify');
var source       = require('vinyl-source-stream');
var buffer       = require('vinyl-buffer');
var gulp         = require('gulp');
var sourcemaps   = require('gulp-sourcemaps');
var _            = require('ramda')
var bundleLogger = require('../util/bundle-logger');
var handleErrors = require('../util/handle-errors');
var config       = require('../config').browserify;

var browserifyTask = function(callback, devMode) {
  var bundleQueue = config.bundleConfigs.length;
  var browserifyThis = _.forEach(function(bundleConfig) {
    if(devMode) {
      // Add watchify args and debug (sourcemaps) option
      bundleConfig = _.mixin(bundleConfig, watchify.args, { debug: true })
      // A watchify require/external bug that prevents proper recompiling,
      // so (for now) we'll ignore these options during development
      bundleConfig = _.omit(['external', 'require'], bundleConfig)
    }

    var b = browserify(bundleConfig);
    var bundle = function() {
      // Log when bundling starts
      bundleLogger.start(bundleConfig.outputName);
      return b
        .bundle()
        // Report compile errors
        .on('error', handleErrors)
        // Use vinyl-source-stream to make the
        // stream gulp compatible. Specify the
        // desired output filename here.
        .pipe(source(bundleConfig.outputName))
        .pipe(buffer())
        // Specify the output destination
        .pipe(sourcemaps.init({loadMaps: true}))
        .pipe(sourcemaps.write('map'))
        .pipe(gulp.dest(bundleConfig.dest))
        .on('end', reportFinished)
        .pipe(browserSync.reload({stream:true}));
    };

    if(devMode) {
      // Wrap with watchify and rebundle on changes
      b = watchify(b);
      // Rebundle on update
      b.on('update', bundle);
      bundleLogger.watch(bundleConfig.outputName)
    } else {
      // Sort out shared dependencies.
      // b.require exposes modules externally
      if(bundleConfig.require) b.require(bundleConfig.require)
      // b.external excludes modules from the bundle, and expects
      // they'll be available externally
      if(bundleConfig.external) b.external(bundleConfig.external)
    }

    var reportFinished = function() {
      // Log when bundling completes
      bundleLogger.end(bundleConfig.outputName)
      if(bundleQueue) {
        bundleQueue--;
        if(bundleQueue === 0) {
          // If queue is empty, tell gulp the task is complete.
          // https://github.com/gulpjs/gulp/blob/master/docs/API.md#accept-a-callback
          callback();
        }
      }
    };

    return bundle();
  });

  // Start bundling with Browserify for each bundleConfig specified
  browserifyThis(config.bundleConfigs);
};

gulp.task('browserify', browserifyTask);

// Exporting the task so we can call it directly in our watch task, with the 'devMode' option
module.exports = browserifyTask