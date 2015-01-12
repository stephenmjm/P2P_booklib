var dest = './src/main/webapp';
var src = './src/main/coffee';
var bowerSrc = './bower_components'

module.exports = {
  clean: [dest + '/css', dest + '/scripts'],
  vendor : {
    js: {
      src: [
          bowerSrc + '/angular/angular.js',
          bowerSrc + '/angular-aria/angular-aria.js',
          bowerSrc + '/angular-route/angular-route.js',
          bowerSrc + '/angular-animate/angular-animate.js',
          bowerSrc + '/hammerjs/hammer.js',
          bowerSrc + '/angular-material/angular-material.js',
          bowerSrc + '/angular-deckgrid/angular-deckgrid.js'
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
  browserSync: {
    server: {
      // Serve up our build folder
      baseDir: dest
    }
  },
  stylus: {
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
    src: src + '/images/**',
    dest: dest + '/images'
  },
  browserify: {
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
  },
  production: {
    cssSrc: dest + '/*.css',
    jsSrc: dest + '/*.js',
    dest: dest
  }
};
