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
    filters:
      - type: "DelayFilter"
        args: |
          {
            "delayTime" : 2000,
            "deltaTime": 1000
          }
    response:
      code: 200
      body: "Good"
      
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

  - path: "/test/404"
    response:
      code: 404
      body: "Not Found"

  - path: "/test/500"
    response:
      code: 500
      body: "Internal error"

  - path: "/test/ok"
    response:
      code: 200
      body: "ok"
```

### Request result with above config
```shell
$ curl http://localhost:8100/books/1
Good%
$ curl http://localhost:8100/books/2
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
