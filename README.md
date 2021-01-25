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

## Developing in Sbt
```sh
$ sbt
> ~reStart
```
## Open Swagger UI
[http://localhost:8080/docs](http://localhost:8080/docs)
