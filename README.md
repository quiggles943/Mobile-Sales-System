# README #
This is an android point of sales(POS) system

### Features Completed ###

* Scan Qr Codes
* Checks Qr code against database and returns correct product
* Retrieves correct image information
* Allows for products to be added to a cart
* Tallies total and allows for checkout with cash option
* Saves transaction as an invoice.
* Invoice is saved and view-able in statistics
* Can download information from a web server
* Can download images from linked dropbox account
* Can edit web address that is accessed in preferences

### Known bugs ###

* When first Logging into dropbox account the login does not register. Another login is required

### Current Issues ###

* Checking of dropbox for valid images takes a long period of time (tested with 104 files, 7 were downloaded. check took 58s!)