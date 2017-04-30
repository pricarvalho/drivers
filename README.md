# Drivers
Rest API that simulates a passenger call to driver

For testing purposes, the POSTMAN's json file 'drivers.postman_collection' is available on the test folder.

## Add Driver

* POST - https://drivers-99.herokuapp.com/drivers

```json
{
  "tagCar" : "APRIL-2017",
  "currentPosition" : {
    "x": 10,
    "y": 2
  }
}
```

## Add Passenger

* POST - https://drivers-99.herokuapp.com/passengers

```json
{ 
  "originPosition" : {
    "x": 0,
    "y": 0
  },
  "targetPosition" : {
    "x": 14,
    "y": 7
   }
}
```
## Call Driver

* GET - https://drivers-99.herokuapp.com/drivers/request-from-passenger/:passengerUUID

## Move Driver

* POST - https://drivers-99.herokuapp.com/drivers/moves

```json
{
  "passenger": "1310811f-d629-4890-83ea-b697388d2dcf",
  "time": 5
}
```
## Restart Application

* DELETE - https://drivers-99.herokuapp.com/
