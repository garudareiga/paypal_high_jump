var express = require('express');
var util = require('util');
//var url = require('url');
//var querystring = require('querystring');
var weatherUnderground = require('./weatherunderground');

var debug = true;
var app = express();
var wuclient = new weatherUnderground(debug);

var items = [
  {state: 'CA', city: 'Campbell', weather: 'N/A'}
  , {state: 'NE', city: 'Omaha', weather: 'N/A'}
  , {state: 'TX', city: 'Austin', weather: 'N/A'}
  , {state: 'MD', city: 'Timonium', weather: 'N/A'}
];

app.set('port', (process.env.PORT || 5000));
app.use(express.static(__dirname + '/public'));
app.set('view engine', 'jade');
app.set('views', './views');

function fetchWeatherAsync(state, city, callback) {
  // asynchronous call with callback function
  wuclient.get(state, city, callback);
};

function fetchWeather() {
  // Fetch weather for each city in parallel
  items.forEach( function(item) {
    // reset before fetching latest weather data
    item.weather = 'N/A';

    var state = item.state;
    var city = item.city;

    if (debug) {
      console.log(util.format("Request-> %s, %s", city, state));
    }

    fetchWeatherAsync(state, city, function(error, body) {
      if (!error) {
        var data = JSON.parse(body);
        if (data.response.hasOwnProperty('error')) {
          if (debug) {
            // return "querynotfound" if we set wrong state or city name
            console.log(util.format("Response-> %s, %s weather: %s", city, state, data.response.error.type));
          }
        } else {
          item.weather = data.current_observation.weather;
          //item.temperature = date.current_observation.temperature_string;
          if (debug) {
            console.log(util.format("Response-> %s, %s weather: %s",
                city, state, item.weather));
          }
        }
      }
    });
  }); // Loop
};

app.get('/', function(req, res) {
  //res.send("Welcome to Ray's Weather Report!");
  // Rendering ./views/index.jade
  res.render('index');
});

app.get('/render_weather', function(req, res) {
    // Rendering ./views/weather.jade with empty weather data
    res.render('weather', {weather: items});
});

app.get('/weather', function(req, res, next) {
  // /weather?state=CA&city=San_Francisco
  if (debug) {
    console.log('URL: ' + req.url);
  }

  fetchWeather();

  // Timeout 1 second, and then render the weather data
  setTimeout( function() {
      res.render('weather', {weather: items})
    }
    , 0.5*1000
  );
  //console.log('END.');
});

app.listen(app.get('port'), function() {
  console.log("Node app is running at localhost:" + app.get('port'))
});