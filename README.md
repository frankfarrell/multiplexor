# rest-multiplexor
Multiplex REST requests to save bandwidth

## How it works
The client (coming soon!) collects http requests to the server in a configurable time window (eg 1 second) 
and POSTs them all in one payload to the /multiplex endpoint. The payload is of the form
```
[
  {
  	"method": "GET", 
  	"path" : "/dog"
  }, 
  {
  	"method": "POST", 
  	"path" : "/dog", 
    "body" : "mongrel"
  }, 
  {
  	"method": "GET", 
    "path" : "/cat"
  }
]
```

The response is of the form, where the body is a String (potentially containing json, but in principle it can be anything)
```
[{
		"requestMethod": "GET",
		"requestPath": "/dog",
		"statusCode": "200",
		"body": "[\"labrador\",\"pooch\",\"poodle\",\"mongrel\"]",
		"headers": {}
	}, {
		"requestMethod": "POST",
		"requestPath": "/dog",
		"statusCode": "201",
		"body": "[\"labrador\",\"pooch\",\"poodle\",\"mongrel\",\"mongrel\"]",
		"headers": {}
	}, {
		"requestMethod": "GET",
		"requestPath": "/cat",
		"statusCode": "200",
		"body": "[\"lion\",\"jaguar\",\"tiger\"]",
		"headers": {}
	}
]
```

Note that the order of the requests is currently undetermined. 

## TODO 
1. Request ordering: Eg if there is an update and a read for the same resource in one request, which one should happen first?
2. Handle headers
3. NB: Security, at the servlet level and per resource level too
