# Driver
Rest API that simulates a passenger call to driver

## Add Driver

* POST - /drivers

```json
{ "tagCar" : "APRIL-2017",
  "currentPosition" : {
    "x": 10,
    "y": 2
  }
}
```

## Add Passenger

* POST - /passengers

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

* GET - /drivers/request-from-passenger/:passengerUUID

## Move Driver

* POST - /drivers/moves

```json
{
  "passenger": "1310811f-d629-4890-83ea-b697388d2dcf",
  "time": 5
}
```
## Restart Application

* DELETE - /
