var dest = './src/main/webapp';
var src = './src/main/coffee';
var bowerSrc = './bower_components'

module.exports = {
  clean: [dest + '/css', dest + '/scripts'],
  server: {
    development: {
      root: [dest],
      livereload: true,
      port: 8000
    }
  },
  vendor : {
    watch: bowerSrc + '/**',
    js: {
      src: [
          bowerSrc + '/hammerjs/hammer.js',
          bowerSrc + '/angular/angular.js',
          bowerSrc + '/angular-ui-router/release/angular-ui-router.js',
          bowerSrc + '/angular-animate/angular-animate.js',
          bowerSrc + '/angular-aria/angular-aria.js',
          bowerSrc + '/angular-material/angular-material.js',
          bowerSrc + '/angular-media-queries/match-media.js'
        ],
      dest: dest + '/scripts',
      outputName: 'vendor.js'
    },
    css: {
      src: bowerSrc + '/angular-material/angular-material.css',
      dest: dest + '/css',
      outputName: 'vendor.css'
    }
  },
  stylus: {
    watch: src + '/app/**/*.styl',
    src: src + '/app/**/*.styl',
    dest: dest + '/css',
    outputName: 'main.css',
    sourcemap: {
      inline: true,
      sourceRoot: '..',
      basePath: 'stylus'
    }
  },
  images: {
    watch: src + '/assets/images/**',
    src: src + '/assets/images/**/*.*',
    dest: dest + '/images'
  },
  browserify: {
    watch: [src + '/app/**/*.coffee', src + '/app/**/*.html'],
    // A separate bundle will be generated for each
    // bundle config in the list below
    bundleConfigs: [{
      entries: src + '/app/app.coffee',
      dest: dest + '/scripts',
      outputName: 'main.js',
      // Additional file extentions to make optional
      extensions: ['.coffee'],
      // list of modules to make require-able externally
      require: ['ramda']
    }]
  }
};
