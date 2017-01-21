# Android Stock Management and POS System #

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

### Implementation ###

##### QR Reader #####
//To be added

##### Item Confirmation / Stock Selection #####
// To be added

##### Transaction Screen #####
// To be added

##### Sub-Total Screen #####
// To be added

##### Payment Selection #####
// To be added

##### Testing #####
To facilitate changes to the app while in the testing phase and while being in use it will be necessary to implement a backdoor method of data upload to the app to allow the contents of the device's internal
database to be reuploaded if the app needed to be changed and re-installed. This is due to the main database being inaccessable due to a lack of a data connection at the event. This backdoor would allow for
changes to the application to be made while at the event and still being able to use the application for the remainder of the event.


### Main Development Targets ###
* Ease of use by the client
* Fully working stock management
* Saving convention stock to device to allow offline use
* POS system with full transaction control
	+ Adding item to transaction
	+ Editing item in transaction
	+ Removing item from transaction
	+ Override transaction or sale price
* Processing and saving a sale to offline database
* Ability to upload offline database to update stock database

#### Stretch Goals ####
* Paypal Here integration

### Android Application Programmers ###
* Josh Renwick
* Paul Quigley

### Testing the Application ###
If possible, a working prototype of the app could be used at the Edinburgh Comic Con (April 15-16) to test the design and implementation.
In order to test this app, all of the main development targets would need to be met and checked to ensure they are in a working state before April the 14th.


