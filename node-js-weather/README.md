# node-js-weather

A node.js app using [Express 4](http://expressjs.com/) obtains weather data from the [Weather Underground](http://www.wunderground.com/)
for four cities (Campell, CA), (Omaha, NE), (Austin, TX) and (Timonium, MD). Then it displays the weather results in a
table.

I already deployed the app on heroku. 
- Welcome page: [http://ancient-atoll-8898.herokuapp.com](http://ancient-atoll-8898.herokuapp.com)
- Weather data page: [http://ancient-atoll-8898.herokuapp.com/weather](http://ancient-atoll-8898.herokuapp.com/weather)

Heroku puts my app to sleep if it doesn't receive any traffic in 1 hour. When you accesses the app, Heroku will automatically wake up the app. This causes a short delay for this first request, but subsequent requests will perform normally.

## Brief Description

To fetch weather data for the four cities above, the app makes four HTTP requests to the Weather Underground API in full parallel. By default, the weather string is "N/A". Once receiving a response, the app parse the response which is a JSON object and obtains the weather string for each city.  The app holds for 0.1 second after sending out all HTTP requests, and then renders the weather data page, no matter whether it receives all HTTP responses or not. I am using Jade template engine to render the web pages.

Because the Weather Underground only allows me to make 10 calls within a minute, I use a rate limiter to control the number of HTTP requests. Each HTTP request needs obtain a token from the rate limiter. If a token is not available, I bypass the HTTP request and log the information "Your wunderground API key exceeded its allotted usage today (10 calls per minute limit)".

## Node.js Modules

I am using the following node.js modules which are already declared in package.json
- express
- [jade](https://github.com/visionmedia/jade)
- request
- [limiter](https://github.com/jhurliman/node-rate-limiter)
- [url-join](https://github.com/jfromaniello/url-join)
- logger

## Running Locally

Make sure you have [Node.js](http://nodejs.org/) installed. 
```sh
cd node-js-weather
npm install
npm start
```

Your app should now be running on [localhost:5000](http://localhost:5000/).

### Welcome page

When you type in a URL [localhost:5000](http://localhost:5000) in browser, it displays a welcome page:
```
Welcome to Ray's Weather Report!
```

### Weather Data Page

When you type in a URL [localhost:5000/weather](http://localhost:5000/weather) in browser, it displays weather results in a table such as:
```
CA	Campbell    Partly Cloudy
NE	Omaha       Mostly Cloudy
TX	Austin      Clear
MD	Timonium    Clear
```

## Deploying to Heroku

Make sure you have [Heroku Toolbelt](https://toolbelt.heroku.com/) installed.
```sh
heroku create
git remote add heroku git@heroku.com:ancient-atoll-8898.git
git push heroku master
```

The app is now hosted on my Heroku dyno ancient-atoll-8898.
