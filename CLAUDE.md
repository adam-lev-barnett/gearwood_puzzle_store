# Project initialization
This is a SpringBoot MVC project made with ThymeLeaf. The following classes and HTML files need to exist in their most basic forms. I will build on the classes and methods upon creation. Project requirements and information can be found in FinalProject2026.pdf in the project root.

## Enums

The following enum types should be created

**Difficulty:**
 - EASY
 - MEDIUM
 - HARD

**Category**
- MECHANICAL
- HISTORICAL
- NAUTICAL
- SCI_FI
- FANTASY
- ARCHITECTURE
- DECORATIVE

**OrderStatus**
- PAID
- CANCELLED

## Entities
The following entity classes need to exist along with their respective data fields:

### Product

-   productCode (String), unique across all products, admin provided
-   name (String)
-   manufacturer (a reference to a Manufacturer object)
-   numberOfPieces (Integer)
-   difficulty - one of the enum types listed above
-   category - one of the enum types listed above
-   price (BigDecimal)
-   shortDescription (String)
-   longDescription (String)
-   active (boolean)

### Manufacturer
- name (String)
- address (String)
- contactEmail (String)
- contactPhone (String)

### User 
-   firstName (String)
-   lastName (String)
-   email (String), unique across all users
-   password (String), 6 character minimum
-   role (a reference to a Role object)

## Other classes/objects

### OrderData
  
-   orderNumber (application-defined String)
-   user (a reference to a User object)
-   orderDateTime (a LocalDateTime object)
-   orderStatus
-   totalAmount (BigDecimal)
-   transactionID (String)
-   shippingInfo (name, address, address2, city, state, zip)
-   orderedItems (a collection of OrderItem objects)

### Order History Data

- orderReference (reference to an Order object)
-   productReference (a reference to an Product object)
-   productName at time of purchase (String)
-   unitPriceUponPurchase (BigDecimal)
-   quantity (Integer)
-   lineTotal (BigDecimal)

## ThymeLeaf HTML templates
Create these basic templates and empty, accompanying CSS files of the same name. Please connect each HTML file to its respective CSS file in the HTML code.
  
-   Home
-   ProductList
-   ProductDetails
-   ProductCreateEdit
-   Login
-   Registration
-   Cart
-   Checkout
-   CheckoutSuccess
-   Account
-   EditAccount
-   ChangePassword
-   OrderHistory
- Error