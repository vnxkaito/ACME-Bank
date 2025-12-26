# ACME Bank CLI

### A project to explore java different acpects
[Project Planning](https://sharing.clickup.com/90182226880/l/h/6-901814482483-1/fba00e611f91a8a)

[User Stories](/Documentation/User%20Stories%20&%20Test%20Cases.txt)

### Available users
+ customer1, password1
+ customer2, password2
+ customer3, password3
+ banker, password4
note: you cannot create a user in the CLI but you can play around with the files.

### Accounts
+ You can display your accounts will logged in
+ As a banker you can create an account and you can link it to a user
+ As a customer you can change your password

## Entities
### User
+ userId
+ password
+ role
+ name

### Account
+ accountId
+ userId
+ balance
+ accountType
+ cardType
+ isLocked

### Transaction
+ transactionId
+ timestamp
+ from
+ to
+ amount
+ type

### Overdraft
+ overdraftId
+ account
+ amount
+ isPaid

## ERD
![ERD](Documentation/ERD.png)

### Available commands
Please follow the guidlines that appear in the screen while running the CLI
