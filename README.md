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

## TODO 
1. Request ordering: Eg if there is an update and a read for the same resource in one request, which one should happen first?
2. Handle headers
3. NB: Security, at the servlet level and per resource level too
