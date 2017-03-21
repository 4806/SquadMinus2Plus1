module.exports = function(config) {
    'use strict';

    config.set({

        // base path that will be used to resolve all patterns (eg. files, exclude)
        basePath: '',

        // frameworks to use
        frameworks: ['jasmine'],

        // list of files / patterns to load in the browser
        files: [
            'src/main/resources/public/lib/webix.js',
            'src/main/resources/public/lib/showdown.js',
            'src/main/resources/public/*.js',
            'src/test/javascript/**/*.js'
        ],

        // list of files to exclude
        exclude: [],
        webpackMiddleware: {
            stats: 'errors-only'
        },
        // test results reporter to use
        reporters: ['progress', 'coverage'],

        preprocessors: {
            // source files, that you wanna generate coverage for
            // do not include tests or libraries
            // (these files will be instrumented by Istanbul)
            'src/main/resources/public/*.js': ['coverage']
        },

        coverageReporter: {
            dir: 'reports/coverage',
            reporters: [
                { type: 'html', subdir: 'html' },
                { type: 'lcov', subdir: 'lcov' },
                { type: 'text' }
            ],
            instrumenterOptions: {
                istanbul: { noCompact: true }
            }
        },

        // web server port
        port: 9876,

        // enable / disable colors in the output (reporters and logs)
        colors: true,

        client : {
            captureConsole : true
        },

        browserConsoleLogOptions : {
            level   : 'debug',
            terminal : true
        },

        // level of logging
        logLevel: config.LOG_INFO,

        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: false,

        plugins : [
            'karma-jasmine',
            'karma-coverage',
            'karma-phantomjs-launcher'
        ],

        // start these browsers
        browsers: [
            'PhantomJS'
        ],

        // Continuous Integration mode
        // if true, Karma captures browsers, runs the tests and exits
        singleRun: true

    });
};
