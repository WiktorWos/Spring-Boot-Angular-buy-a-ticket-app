# buy-a-ticket-rest
Simple Spring Boot REST service. It uses local MongoDb database, connected with app by Spring MongoRepository.
App sends confirmation email when you add a ticket. Controllers are tested through @WebMvcTest.

## Endpoints
```
/api/users
```
get - get all users

post - add a user
```
/api/users/{id}
```

get - get one user

put - update user

delete - delete user
```
/api/tickets
```
get - get all tickets

delete - delete inactive tickets
```
/api/tickets/{userId}
```
get - get user tickets

put - add ticket to the user
