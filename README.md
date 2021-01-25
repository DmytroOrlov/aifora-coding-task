## Run
with `sbt 1.4.6`
```sh
$ sbt run
[info] running hurricane.Main 
I 2021-01-25T02:02:11.849 (NIO1SocketServerGroup.scala:242)  ….nio1.NIO1SocketServerGroup [138:zio-default-async-10] Service bound to address /127.0.0.1:8080
I 2021-01-25T02:02:11.874    (BlazeServerBuilder.scala:357)  …er.blaze.BlazeServerBuilder [142:zio-default-async-14] 
  _   _   _        _ _
 | |_| |_| |_ _ __| | | ___
 | ' \  _|  _| '_ \_  _(_-<
 |_||_\__|\__| .__/ |_|/__/
             |_|
I 2021-01-25T02:02:11.923    (BlazeServerBuilder.scala:360)  …er.blaze.BlazeServerBuilder [142:zio-default-async-14] http4s v0.21.7 on blaze v0.14.13 started at http://127.0.0.1:8080/
```
## Math
According to https://en.wikipedia.org/wiki/Poisson_distribution

P(`at least one hurricane`) = 1-P(`no hurricane`) = 1-e^(-λ)

where λ - Average

## Usage
with `curl 7.54.0`
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
## Test
```sh
$ sbt test
[info] HurricaneTest:
[info] - Logic must fail for no data in most
[info] - Logic must fail for absent month in possibility
[info] - Logic must return possibility for May (average = 0.1)
[info] - Logic must return most = 8
[info] ScalaTest
[info] Run completed in 5 seconds, 866 milliseconds.
[info] Total number of tests run: 4
[info] Suites: completed 1, aborted 0
[info] Tests: succeeded 4, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[info] Passed: Total 4, Failed 0, Errors 0, Passed 4
[success] Total time: 15 s, completed Jan 25, 2021, 2:01:07 AM
```
## Open Swagger UI
[http://localhost:8080/docs](http://localhost:8080/docs)

## Developing in Sbt
```sh
$ sbt
> ~reStart
```
