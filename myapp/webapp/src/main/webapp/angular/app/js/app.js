'use strict';

// Declare app level module which depends on views, and components
angular.module('demo', ['ngResource'])
  .factory('Drivers', function ($resource) {
    return $resource(location.origin + '/restful/objects/rita.Car/0/collections/drivers');
  })

  .factory('AddRide', function ($resource) {
    return $resource(location.origin + '/restful/objects/rita.Car/0/actions/addRide/invoke');
  })

  .controller('AddRideCtrl', function ($scope, $resource, Drivers, AddRide) {
    $scope.drivers = Drivers.get();
    $scope.ride = {};

    $scope.addRide = function () {
      $('#addRideSuccess').hide();
      $('#addRideError').hide();

      var params = {
        description: {value: $scope.ride.description},
        date: {value: $scope.ride.date.toISOString().slice(0, 9)},
        newMileage: {value: $scope.ride.mileage},
        driver: {value: $scope.ride.driver}
      };

      AddRide.save(params,
        function () {
          $('#addRideSuccess').show();
        },
        function (error) {
          $scope.addRideError = error.data['x-ro-invalidReason'];
          $('#addRideError').show();
        });
    };
  });