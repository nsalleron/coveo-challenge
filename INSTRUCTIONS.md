# Review Challenge
​
This challenge is about reviewing code. We’ve built a solution to our normal [backend challenge](https://github.com/coveo/backend-coding-challenge), and we want you to review it. To make things clear, you don't have to prepare any code nor to follow the readme on GitHub. The GitHub link is only given to you to explain what the project in this archive accomplishes. The code to review is in this archive (see instructions below to run it). To complexify things, we added a UI on top of that backend project.
​
All steps listed below will be required during the interview. We expect you to do them BEFORE we meet.
​
-   Make sure you can run the code (backend AND frontend) and understand what's going on.
-   Review the code and take notes on stuff you would improve or change. Assume this code is in production right now and that you have to plan the next few releases. Having an agile process in mind, what would you change in the first release, second release, etc. This will help us focus our discussion on what's important first.
-   Make sure to have a setup that allows you to hit a breakpoint and debug in a step-by-step manner the relevant part(s) for the position you applied for (backend, frontend, or both for a full-stack position). It does't matter which application you use to do it, but make sure you're comfortable debugging in the environment you choose before the interview.
-   Have an editor or IDE ready to code during the interview.
-   Have Git installed.
-   Make sure you understand what's going on in the code.

We didn't follow our normal standards, so you should have something to say about that code.

Whether you are a backend, frontend, or full-stack developer, we expect you to understand the whole project at a minimum and have an opinion on it. We understand that, if you’re applying on a backend job, you won't have as much knowledge on the frontend, and vice versa. It's normal. We’ll adjust the time spent on each part according to the position we target for you.
​
## Running the Backend
​
It’s built using Java 17, Maven, and Spring Boot. To build the backend, you will at least need Java 17 and Maven installed on your machine. To run it, you have two options depending on the position you apply for:
​
-   Backend or full-stack positions: Install an IDE and run the main class (i.e., `./backend/src/main/java/com/coveo/challenge/ReviewChallengeApplication.java`). Usually right-clicking the class allows you to start the application in debug or run mode.

OR
​
-   Frontend position: Build it in the terminal running `mvn clean package -T1C -U -Dmaven.test.skip=true` in the ./backend folder, and then run it with the following command: `java -jar target/review-challenge-0.0.1-SNAPSHOT.jar`. If something is running on 8080 on your machine, you can add `-Dserver.port=8081`, but keep in mind that the frontend is expecting it to run on 8080. You can change the frontend code if you really can't run this on 8080.
​
## Running the Frontend
​
It’s built using TypeScript and React. To run it, you will need Node.js installed. In the frontend folder, run `npm install`, and then `npm run start`. It will open a tab in the browser where you should be able to search cities. Make sure the backend is running, otherwise the search won't work.