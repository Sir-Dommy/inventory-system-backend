Backend

Github link : https://github.com/Sir-Dommy/inventory-system-backend

Technologies used:

Java version 17

SpringBoot version 3.0.8

MySQL

Note:

MySQL driver in the project is configured to listen to port 3306, if you are using a different port you can alter the application.properties file.

The app is configured to start on port 8080, you can also alter this at your convenience.

Running the app

Clone the github repository (https://github.com/Sir-Dommy/inventory-system-backend)

Navigate to the cloned project and open it in any of your preferred terminal

Run mvn clean install to generate a snapshot.jar file

Run java –jar taget/snapshot.jar file to start the application

Create a new user by performing a POST request to this endpoint with the given payload:

Note: This step is necessary as user details will be need to generate bearer auth tokens.
http://localhost:8080/auth/addNewUser
{
    "name":"sir",
    "password":"123"
}

You can now generate bearer tokens to use when authenticating request by performing a POST request to this endpoint and given payload: 
http://localhost:8080/auth/generateToken 
{
    "username":"sir",
    "password":"123"
}

Note: name is unique and therefore cannot create two users with same name, you can change the payload parameters at your convenience
Adherence to test

1.	Model creation: In the entities folder inside src/main I have created a Products model having these attributes: id (generated), name, description, currentStock, and minimumStockLevel.


2.	Product Repository: I have also included ProductRepository class in the repositories which performs CRUD operations on the Products entity


3.	Inventory Service: I have created a ProductService in the service folder which performs these functions: Adding a new product to the inventory, removing a specific quantity of a product from the inventory, updating the stock level of a product.


4.	REST APIs:  I tested my app on localhost so I will use it to denote the entire url


i.	Adding a new product (Requires Authentication): 
locahost:8080/api/product (POST) 
Note: product name is unique so two products cannot share same name


ii.	Removing a specific quantity of a product (Requires Authentication): http://localhost:8080/api/removeStock/{productId}/{quantityToRemove} (PUT). Here first path variable is the product id and the second one is quantity to be removed.


iii.	Updating product details (Requires Authentication):  http://localhost:8080/api/product/{productId} (PUT), 
{
"name":"Bluetooth",
"description":"F@@",
"currentStock":23,
"minimumStockLevel":118
}

iv.	Retrieving all products (No Authentication): 
http://localhost:8080/api/allProducts (GET)


v.	Retrieving a specific product by ID (Requires Authentication):
http://localhost:8080/api/product/{productId} (GET)

5.	 Security:  The app utilizes bearer authentication by making use JwtService and JwtFilter to make sure that requests are authenticated. Requests are authenticated using bearer authentication tokens, tokens expire after 24 hours. 
You can generate tokens using the endpoint below: 
http://localhost:8080/auth/generateToken (POST) 
{
    "username":"sir",
    "password":"123"
}

No authentication required to access this url, you can alter the request parameter values at your convenience. However, this user must be registered prior to token generation.


6.	Minimum Stock Level: To handle this I have ensured that each product has an associated minimum stock value which can be manipulated using the endpoints provided.


7.	Stock Alerts: I have implemented an alert method in Product Service class which logs a message using java.util.logger library.


Unit Testing

I have included tests for the Products Service to verify its operation and behavior when given both the right and wrong parameters.

I have also included tests for the UserInfo Service behaving similarly as the Products Service tests.

Both @SpringBootTest notation and Mocktito have been used for these tests.

