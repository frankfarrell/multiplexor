# Multiplexor
Multiplex REST requests to save bandwidth, avoid browser throttling and boost performance while keeping your api easy 
to understand and self-documenting! 

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

## Configure: 

Add the following to your Spring Boot applicatoin config 

```
import com.github.frankfarrell.multiplexor.servlet.MultiplexorServlet;
import org.springframework.web.servlet.DispatcherServlet;

...
    private static final MULTIPLEXOR_ENDPOINT = "/multiplexor";
        
    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    @Bean
    public ServletRegistrationBean dispatcherServletRegistration(DispatcherServlet dispatcherServlet) {

        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
        registration.setLoadOnStartup(0);
        registration.setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);

        return registration;
    } 
    
    @Bean
    public MultiplexorServlet multiplexorServlet(DispatcherServlet dispatcherServlet, ObjectMapper objectMapper){
        return new MultiplexorServlet(dispatcherServlet, objectMapper);
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(final MultiplexorServlet multiplexorServlet){
        final ServletRegistrationBean registration = new ServletRegistrationBean(multiplexorServlet, MULTIPLEXOR_ENDPOINT);
        return registration;
    }
```

All requests to `/multiplexor` will be demultiplexed. 

## Why bother?

1. Web browsers enforce a limit on the number of concurrent connections to a host (by rfc its 2, but for latest Chrome it is 6). If you need to make multiple long running calls to 
your server, the total latency could increase because of such stalling: https://www.bluetriangle.com/blog/blocking-web-performance-villain/
2. You could write a multiplexed endpoint yourself but the benefit here is that you get to keep a nice clean api.

## Worth noting 
Each de-multiplexed request is handled in a separate thread as servlet request. You get things like transactions and endpoint level security quite cheaply, but with the overhead of extra threads. 
If typically the requests would consume N threads, the multiplexor request will consume N+1 threads

## TODO 
1. Request ordering: Eg if there is an update and a read for the same resource in one request, which one should happen first?
2. Handle headers
3. NB: Security, at the servlet level and per resource level too
