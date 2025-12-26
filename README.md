# ACME Bank CLI

### A project to explore java different acpects
[Project Planning](https://sharing.clickup.com/90182226880/l/h/4-90188894364-1/95c1aee49e954a7)

[User Stories](/Documentation/User%20Stories%20&%20Test%20Cases.txt)

### Available users
+ customer1, password1
+ customer2, password2
+ customer3, password3
+ banker, password4

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
![ERD](https://i.imgur.com/CviUeHl.png)

### Available commands
