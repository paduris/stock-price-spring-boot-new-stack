
This application demonstrates latest version of spring cloud frameworks.

1. Consul for service discovery ( For mac users use brew to install consul )
2. stock-app-gateway -> proxy server, filters, routes and handles circuit breakers
3. google-stock -> Show some google stock info from yahoo financials ( uses delay profile for demostrating circuit breaker configuration)
   for quick run -> run with -Dspring.profiles.active=delay vm args to test the feature
4. Intel-stock -> Shows Intel stock info from yahoo financials 





  


