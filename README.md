## Math
According to https://en.wikipedia.org/wiki/Poisson_distribution

P(`at least one hurricane`) = 1-P(`no hurricane`) = 1-e^(-λ)

where λ - Average

## Usage
### In which year and month were the most hurricanes
```sh
$ curl -X GET "http://localhost:8080/most" -H  "accept: application/json"
{"year":2005,"month":"Oct"}
```
### Estimate the possibility of a hurricane by a given month
```sh
$ curl -X GET "http://localhost:8080/possibility/May" -H  "accept: application/json"
{"estimatedPossibility":0.09516258196404048}
```

## Open Swagger UI
[http://localhost:8080/docs](http://localhost:8080/docs)

## Developing in Sbt
```sh
$ sbt
> ~reStart
```
