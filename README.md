# Spring Boot + Angular buy a ticket app
Simple Spring Boot REST service. It uses local MongoDb database, connected by Spring MongoRepository.
App sends confirmation email when you add a ticket. Controllers are tested through @WebMvcTest.
REST service enables jwt authentication.

## Endpoints (avaliable at the moment, some features will be added in the future)
```
/api/authenticate
```
post - authenticates user, generates jwt if authenticated
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
