## Introduction

Student-grades app is spring boot application that allows to manage student's personal details and grades.

## Setup

The easiest way to run the project is via docker.

### Install Docker

Install Docker Desktop which is one-click install app for your Mac, Linux, or Windows environment.
https://www.docker.com/products/docker-desktop/

Once you install Docker Desktop, make sure it's running.

### Run Project

From the main directory where docker-compose.yaml resides, open terminal and type:
docker-compose -f docker-compose-local.yml up --force-recreate

This will launch both backend and postgres db.

Open up your browser and head over to:
http://localhost:8080/swagger-ui.html

You should see the api menu with all the endpoints.
Before you start using the app you should authenticate with user credentials:

1) Go to jwt-authentication-controller and create user via /user endpoint. you should receive the token in response body.
2) Click "Authorize" button (top right corner) and type: Bearer <your_token>. The page should reload.

Now you can manage your student's details :)






