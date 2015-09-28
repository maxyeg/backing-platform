var bkServices = angular.module('backer.services', []);
bkServices.factory('OfferService', function() {
  var service = {
    getOffer: function(id) {
      if (id && service.offers) {
        for (var i = 0; i < service.offers.length; i++) {
          if (id == service.offers[i].id) {
            return service.offers[i];
          }
        }
      } else {
        return null;
      }
    },
    getOffers: function() {
      return service.offers;
    },
    setOffers: function(offers) {
      service.offers = offers;
    }
  };
  service.offers = [];
  return service;
});
