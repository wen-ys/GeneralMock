# Basic Mock
Basically return 'Status code 200 / OK' regarding ALL requests without custom setting.

## Mock custom configuration

- path: Path for custom routing
- filters: Set filter for custom effect
  - DelayFilter : This filter can make delay before response with delayTime option.
- response:
  - code : custom response code
  - body : custom body data
```yml
  - path: "/v1/greeting"
    filters:
      - type: "DelayFilter"
        args: |
          {
            "delayTime": 2000
          }

    response:
      code: 200
      body: |
        {
          "id" : "say",
          "name" : "Hello"
        }
```
# GeneralMock
