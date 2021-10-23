# elec5619-backend

## List all used libraries and their versions

1. com.auth0 java-jwt version: 3.8.2


2. org.mockito mockito-core version: 2.23.4
      
3. com.vaadin.external.google android-json version: 0.0.20131108.vaadin1

4. com.vaadin.external.google android-json version: 0.0.20131108.vaadin1


## List all working functionalities of the project-
#### User authentication
- After user login, this user will get a token. We will use this token to verify this user
- When user want to login, Users need to click on the slider verification code first which given by GEETEST (external API)
- When user in the login page, there will have encouraged word randomly produced given by external API 



#### User
- User can register, login and logout
- User can change the role to creator
- User can change the profile picture
- User can follow other creator
- User can search name of creator
- User can click creator picture and see the creator page
- User can unfollow other creator
- User can subscribe some creators



#### Creator
- Creator can change the role to user



## A quick guide on how to run your application
#### Frontend:
We use Vue.js as presentation layer, in order to compile and run the project successfully, you should make sure the **NPM** has been installed successfully in your computer.

1. cd PATH_TO_ROOT_OF_PROJECT
2. npm install
3. npm run server

The server will be run on localhost:8080

#### Backend
We use SpringBoot in the backend, firstly, we need to download the project backend code and run it in the IDE (can use intellji or eclipse) using maven. The server will be run on localhost:8080/api

#### After running frontend and backend, we can open website localhost:8080 and now we are using this website

