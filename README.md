# Microserviço: transaction-statistics-service

### Method: POST
* *Endpoint:* http://localhost:8080/transaction-statistics-service/transaction
* *Payload:* 
```json5
{
    "timestamp": 1478221904000,
    "amount": 25000.15
}
```
* *Response:*

```
code = 201
message = "If the difference between timestamp transaction and system timestamp is less than 60 seconds, the object is create"
```
```
code = 204
message = "When the difference between timestamp transaction and system timestamp is more than 60 seconds, the object is not create"
```

### Method: GET
* *Endpoint:* http://localhost:8080/transaction-statistics-service/statistics
* *Response:* 
```json5
{
    "sum": 25170.91,
    "min": 20.00,
    "max": 25000.17,
    "avg": 8390.30,
    "count": 3
}
```

## Testes JUnit e Mockito
![alt text](https://github.com/danillorcb/transaction-statistics-service/blob/master/images/junit.PNG "Testes JUnit e Mockito")


## Docker
* Create image with Docker			
`				
docker build -f Dockerfile -t springio/transaction-statistics-service .
`
* Run image on port 8080									
`
docker run -p 8080:8080 -t springio/transaction-statistics-service
`

## Swagger
![alt text](https://github.com/danillorcb/transaction-statistics-service/blob/master/images/swagger.PNG "Swagger Documentation")

`
http://localhost:8080/swagger-ui.html
`