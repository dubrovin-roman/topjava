#### get All Users
curl -s http://localhost:8080/topjava/rest/admin/users
#### get Users 100001
curl -s http://localhost:8080/topjava/rest/admin/users/100001
#### get All Meals
curl -s http://localhost:8080/topjava/rest/meals
#### get Meals 100003
curl -s http://localhost:8080/topjava/rest/meals/100003
#### filter Meals
curl -s "http://localhost:8080/topjava/rest/meals/filter?endDate=2020-01-30&startTime=13:00:00&endTime=21:00:00"
#### get Meals not found
curl -s -v http://localhost:8080/topjava/rest/meals/100025
#### delete Meals
curl -s -X DELETE http://localhost:8080/topjava/rest/meals/100003
#### create Meals
curl -s -X POST -d '{"dateTime":"2024-04-03T12:00:00","description":"Lunch","calories":1000}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/topjava/rest/meals
#### update Meals
curl -s -X PUT -d '{"dateTime":"2024-04-03T08:00:00", "description":"Updated", "calories":1000}' -H 'Content-Type: application/json' http://localhost:8080/topjava/rest/meals/100005