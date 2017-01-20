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
