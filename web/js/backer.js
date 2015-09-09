'use strict';
var backerApp = angular.module('backer', ['backer.controllers', 'ui.router']);
backerApp.config(function ($stateProvider, $urlRouterProvider) {
  $urlRouterProvider.otherwise("main");
  $stateProvider
      .state('main', {
        url: "/main",
        templateUrl: "templates/main.html",
        controller: "MainController"
      })
      .state('offer', {
        url: "/offer?id",
        templateUrl: "templates/offer.html",
        controller: "OfferController"
      });
});