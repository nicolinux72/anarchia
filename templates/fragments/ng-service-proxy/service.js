'use strict';

angular.module('${appName}')
    .factory('${proxyName}', ['\$http', function (\$http) {
        return {

<% endpoints.each{%>
                $it.name: function () {

                var params = {
                }
               
                return \$http.${it.httpMethod}('${it.url}',{params: params}).then(function (response) {
                    return response.data;
                });
            },
<%}%> 

        };

}]);
