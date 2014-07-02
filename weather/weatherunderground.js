/**
 * Created by ray on 6/29/14.
 *
 * Weather Underground API Client
 */

var request = require('request');
var urljoin = require('url-join');
var RateLimiter = require('limiter').RateLimiter;
//var limiter = new RateLimiter(10, 'minute', true);  // true: fire callback immediately
var limiter = new RateLimiter(10, 'minute', true);

// My Weather Underground API Key
var apiKey = 'e6c0977fda8829a9';

// Weather Underground API Example
// http://api.wunderground.com/api/Your_Key/conditions/q/<state>/<city>.json

var weatherunderground = function (debug) {
    ///var host = 'http://api.wunderground.com/api/' + apikey;
    var host = 'http://api.wunderground.com/api';
    var format = '.json';

    this.get = function (state, city, callback) {
        var url = urljoin(host, apiKey, 'conditions', 'q', state, city + format);
        if (debug) {
            console.log('Send HTTP request: ' + url);
        }

        limiter.removeTokens(1, function(err, remainingRequests) {
          if (debug) {
            console.log('remainingRequests=', remainingRequests);
          }
          if (remainingRequests < 0) {
            console.log('Your wunderground API key exceeded its allotted usage today (' +
                '500 calls per day with 10 calls per minute limit).');
            //callback(false, body);
          } else {
            // Send http request if the token is available
            request(url, function (error, response, body) {
              if (!error && response.statusCode == 200) {
                if (debug) {
                  //console.log('response body: ' + body);
                }
                callback(error, body);
              } else {
                console.log('Request failed!');
              }
            });
          }
        });

    };
};

module.exports = weatherunderground;