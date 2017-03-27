![Alt text](https://bytebucket.org/mobileSalesSystem/android-sales-application/raw/506ce6df9acc1aa26baf7b4ab95a40d14e4ecfcd/MobileSalesSystem/app/src/main/res/drawable-hdpi/logo.png?token=df5e64831fef14906eaab557a9d8477c2f5aec21)
# README #

## Android Stock Management and POS System ##
*Current Version: 1.0.0 Bumble Bee*

### Application Overview ###

This app is being designed to allow for full stock management for a client who sells artwork at conventions. This app should be easy to use and allow for easy stock management at
home and be able to store what stock is being taken to a convention. While at a convention the app should be able to process transactions made by scanning the QR code of the items
being sold and calculate the total as well as then storing this information to update the central stock database after the convention

### App Description ###

The purpose of the full project is to create and maintain a stock count of items the end user has and to be able to use the android app to monitor the sale of items at conventions.
The stock is stored on an sqlite database in a java server and when going to conventions the user can scan the items that they are taking with them.
When the item is scanned, the application calls to the server with the id number of the item to request the data about the item and to add it to the applications own local database
where the items can be accessed without a data connection.

When at a convention, the user can begin a transaction with a customer in the app. When this occurs the user can scan the qr code label on the item. The app then looks up the item
in its internal database for the item and displays its information on the screen for the user to confirm it is the correct item. Here the user also selects what version of the
item is being sold. Once the user confirms this the item is then added to the transaction and displayed on the transaction screen. From this screen the user can either add another
item, edit/remove an item already in the transaction, or proceed to the next screen.

The next screen shows the subtotal for the transaction. From here the user can continue to select payment, apply a discount to the transaction, or return to the transaction window.

For the initial scope of the application there will be two options available: Cash, which marks on the transaction that it was paid for in cash; and Card, which would simply display
the cost to input to a card reader and mark that the transaction was paid for by card.
> If time constraints allow, the scope of the project can be expanded to incorperate paypal here support. The client already has and utilises a paypal here card reader and it would
> be possible to utilise the paypal here SDK (https://github.com/paypal/paypal-here-sdk-android-distribution) and the SDK Guide (https://developer.paypal.com/docs/integration/paypal-here/android-dev/overview/)
> to streamline the card payment process and to be able to send the transaction details to the card reader for a faster and more convenient system.


### Features Completed ###

| Development Target  | Completed? |
| -------------|---------|
| Scan Qr Codes| Yes |
| Checks Qr code against database and returns correct product| Yes |
| Retrieves correct image information| Yes |
| Allows for products to be added to a cart| Yes |
| Tallies total and allows for checkout with cash option| Yes |
| Saves transaction as an invoice in local database | Yes |
| Invoice is saved and view-able in statistics | Yes |
| Can download information from a web server| Yes |
| Ability to upload offline database to update stock database| Yes |
| | |
| Can download images from linked Dropbox account| Yes |
| Can edit web address that is accessed in preferences| Yes |
| | |
| Ease of use by the client| Yes |
| Fully working stock management| To be checked|

| Stretch Goals  | Completed? |
| ---------------------|---------|
| Paypal Here integration | No |
| Application optimisation | Partial |

### Implementation ###

##### QR Reader #####
QR code reader is implemented using the google play services [mobile vision API](https://developers.google.com/vision/)

##### Item Confirmation / Stock Selection #####
Item confirmation and stock selection is achieved by using an activity called Item check which shows in an image view a thumbnail of the image returned by the qr code along with its format options. From here the user can either cancel the item or select the format and add it to a transaction.

##### Transaction Screen #####
This screen uses a listview with a custom adapter to show each item which is added to the transaction. From this screen the user can view each product added including a thumbnail, remove items or move on to the checkout screen. A check on back button pressed confirms if the user wants to cancel the transaction to ensure this is not done accidentally. 

##### Sub-Total Screen #####
The sub-total screen shows the final list of the items in the transaction as well as the total price accumulated. There is also an option to change the final price to allow for discounting. The user can then select either cash or paypal payment. If cash payment is selected then the transaction is added to an invoice and saved. Currently paypal is not integrated but this may be added in the future


### Known bugs ###

* When first Logging into dropbox account the login does not register. Another login is required

### Current Issues ###

* ~~Checking of dropbox for valid images takes a long period of time (tested with 104 files, 7 were downloaded. check took 58s!)~~

### Android Application Programmers ###
* Josh Renwick
* Paul Quigley

### Testing the Application ###
If possible, a working prototype of the app could be used at the Edinburgh Comic Con (April 15-16) to test the design and implementation.
In order to test this app, all of the main development targets would need to be met and checked to ensure they are in a working state before April the 14th.

To facilitate changes to the app while in the testing phase and while being in use it will be necessary to implement a backdoor method of data upload to the app to allow the contents of the device's internal
database to be re uploaded if the app needed to be changed and re-installed. This is due to the main database being inaccessable due to a lack of a data connection at the event. This backdoor would allow for
changes to the application to be made while at the event and still being able to use the application for the remainder of the event.