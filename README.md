# elec5619-backend

## List all used libraries and their versions

| Dependency                                             | Version              |
| ------------------------------------------------------ | -------------------- |
| auth0 / java-jwt com.auth0 : java-jwt                  | 3.8.2                |
| com.vaadin.external.google : android-json              | 0.0.20131108.vaadin1 |
| junit-team / junit4 junit : junit                      |                      |
| mysql / mysql-connector-j mysql : mysql-connector-java |                      |
| org.apache.tomcat.embed : tomcat-embed-jasper          |                      |
| mockito / mockito org.mockito:mockito-core             | 2.23.4               |
| org.springframework.boot                               | 2.0.4                |


## List all working functionalities of the project-

### User
#### User authentication
- After user login, this user will get a token. We will use this token to verify this user
- When user want to login, Users need to click on the slider verification code first which given by GEETEST (external API)


#### Normal user
- User can register, login and logout
- User can change the role to a creator
- User can change the profile picture
- User can follow other creator
- User can search the name of a creator
- User can click creator picture and see the creator page
- User can unfollow other creators
- Users can subscribe to some creators


#### Creator
- Creator can change the role to user
- Creator can upload their works
- Creator can delete their works
- Creator can edit their works
- Creator can choose the category of their work

### Subscription

#### Subscribe
- Logged in users can manage their subscription

#### Subscribe Type
- Logged in creators can manage their subscription price



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

