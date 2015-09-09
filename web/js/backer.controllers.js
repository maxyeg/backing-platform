var bkControllers = angular.module('backer.controllers', ['backer.services']);

bkControllers.controller("MainController", function ($scope, $state, OfferService) {
  $scope.offers = OfferService.getOffers();
  $scope.addOffer = function () {
    $scope.offers.push({
      id: utils.uuid(),
      username: $scope.offer.username,
      description: $scope.offer.description
    });
    OfferService.setOffers($scope.offers);
  };
  $scope.selectOffer = function (id) {
    $state.go('offer', {id : id});
  };
});

bkControllers.controller("OfferController", function ($scope, $state, $location, OfferService) {
  $scope.goToList = function() {
    $state.go('main');
  };

  var params = $location.search();
  if (params) {
    $scope.offer = OfferService.getOffer(params.id);
  }
});