[![codecov](https://codecov.io/gh/wen-ys/GeneralMock/branch/main/graph/badge.svg?token=5MXZDBWQ3Q)](https://codecov.io/gh/wen-ys/GeneralMock)

# General Mock
Basically return 'Status code 200 / OK' regarding ALL requests without custom setting.

## Mock run with custom option file
```shell
docker run --mount type=bind,source=/{absolute path}/routing_config.yml,target=/service/routing_rule.yml -p 8100:8100 wenys/generalmock
```
Make 'routing_config.yml' with below rules.

## Mock custom configuration

- path: Path for custom routing
- filters: Set filter for custom effect
  - DelayFilter : This filter can make delay before response with delayTime option.
  - HeaderFilter : This filter can add header values on response.
- response:
  - code : custom response code
  - body : custom body data

```yml
routingPolicies:
  - path: "/books/1"
    params:
      number:
        - "1"
        - "2"
      isOn:
        - "on"
    filters:
      - type: "DelayFilter"
        args: |
          {
            "delayTime" : 2000,
            "deltaTime": 1000
          }
    response:
      code: 200
      body: "Good Param"

  - path: "/books/1"
    filters:
    response:
      code: 200
      body: "no Param"

  - path: "/books/2"
    filters:
      - type: "HeaderFilter"
        args: |
          {
            "responseHeader" : "test"
          }
    response:
      code: 200
      body: |
        {
          "title" : "queen"
        }
```

### Request result with above config
```shell
$ curl http://localhost:8100/books/1
Good%
$ curl -v http://localhost:8100/books/2
*   Trying ::1:8100...
* Connected to localhost (::1) port 8100 (#0)
> GET /books/2 HTTP/1.1
> Host: localhost:8100
> User-Agent: curl/7.77.0
> Accept: */*
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 200 OK
< responseHeader: test
< Content-Type: application/json
< Content-Length: 24
<
{
  "title" : "queen"
}
$ curl http://localhost:8100/test/404
Not Found%
$ curl http://localhost:8100/test/500
Internal error%
$ curl http://localhost:8100/test/ok
ok%
```
