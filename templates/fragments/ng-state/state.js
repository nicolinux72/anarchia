'use strict';

angular.module('${appName}')
    .config(function (\$stateProvider) {
        \$stateProvider
            .state('${state}', {
                parent: 'site',
                url: '/${state}',
                views: {
                    'content@': {
                        templateUrl: 'js/${state}/${state}.html',
                        controller:  '${State}Controller'
                    }
                },
                data: {
                pageTitle: '${state}.title'
                },
                resolve: {
                    mainTranslatePartialLoader: ['\$translate', '\$translatePartialLoader', function (\$translate,\$translatePartialLoader) {
                        \$translatePartialLoader.addPart('${state}');
                        return \$translate.refresh();
                    }]
                }
            });
    });
