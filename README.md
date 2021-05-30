# Jaz-Movie-Ticket
Book Movie Tickets

***********PLEASE CHECKOUT OUT DEVELOPER BRANCH*********

Tech Stacks:
Java 1.8,
Mysql server,
Spring boot,
Spring Security,
Rabbitmq,
Maven


Note: I attached the postman collections and sample data.

Positive Flow:

http://localhost:8080/user/sign-up,
http://localhost:8080/user/sign-in,
http://localhost:8080/movie/create,
http://localhost:8080/movie/list,
http://localhost:8080/movie/{movieId},
http://localhost:8080/movie/get-free-seats,
http://localhost:8080/book/partial

Once this flow done in postman, Please note down the partial booking id from this API response(http://localhost:8080/book/partial)

Step 1: Hit this below URL in UI to see the payment sign-in page.
URL: http://localhost:8080/payment/sign-in

Step 2: Enter the user name and password.
Step 3: You will see the jaz payment page and enter the partial booking id in the Booking Id column. 
Then enter the details random because its not validated. 
For Eg: BookingId: 101, Industry type : Retail, ChannelId: WEB, Amount: 100.   

Step 4: You can pay the payment. Finally, it will ask the payment for Success or not because its only for 
testing in developer environment not originally account activated. 
It will redirect to our report page. i.e http://localhost:8080/payment/validate-payment






Run rabbitmq: rabbitmq-plugins enable rabbitmq_management

