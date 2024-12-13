# 3-Player-Chess
Three Player chess game  using a Spring Boot web application

## Project Structure
The project consists of 3 different Modules:
1. webapp: This module contains the Spring web application
2. backend: This module contains the entire game logic for three player chess

## Install
Clone the repository
```sh
git clone https://github.com/ShashankSinha98/Coding-Ninjas-Chess.git
  ```

Run the web application:
```sh
gradlew bootRun
  ```

## Deployment
The web application is available under https://threeplayerchess.onrender.com.

For the deployment there is a Dockerfile in the project directory.
This was used to create a Docker image and push it to DockerHub.
A web service was then created with Render (https://render.com/), which works with the DockerHub image.

    
