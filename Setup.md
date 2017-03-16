# PostgreSQL Setup
This project relies on [PostgreSQL](https://www.postgresql.org/) for its Datasource. The following are setup instructions to get your environment ready.
1. Download and install [PostgreSQL 9.6](https://www.postgresql.org/download/)
    - Be sure to write down your _username_ and _password_ for later
2. Once installed, the only thing left to do is setup _run configurations_ in your environment. This is required as the the Datasource configuration (application.properties) is reliant on the environment variables These variables and there required values are:
    - **JDBC_DATABASE_URL** = jdbc:postgresql://localhost:5432/postgres
    - **JDBC_DATABASE_USERNAME** = _your username_
    - **JDBC_DATABASE_PASSWORD** = _your password_ 
3. It is recommended that you set up 2 run configurations, one for BootApplication, and one for the Maven _package_ build.
    - To add these variables to the BootApplication configuration, go to _edit configurations..._ then under _Configuration_ tab click on the _"..."_ button on the far right of the _Environment variables_ section. Here you can add all 3 of the required variables.
    - For the Maven _package_ build, you will need to create the configuration by right clicking on the _package_ option under the _Maven Projects_ Window, _Lifecycle_ folder. Select the _Create 'PROJECTNAME' [configuration]_ option. Once in the menu, go to the _Runner_ tab and un-select _Use project settings_ checkbox. Then click on the _"..."_ button on the far right of the _Environment variables_ section. Here you can add all 3 of the required variables.
4. That's it! These 2 configuration will ensure that you can both run and package the application. If you want to run any other configuration, you will need to edit it in order to add the 3 environment variables.