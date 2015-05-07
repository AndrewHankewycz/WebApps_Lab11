Begin by navigating to the root directory of the web application folder

Node webserver
  - run 'npm install' to install all necessary packages
  - run the command 'node webserver.js' to start the server and host
    all static content
  - SQL files to be imported from the Cloud Chat have a configurable
    directory for where Glassfish is located. Edit the GLASS_FISH_DIR
    constant inside of config.js

Node CloudChatServer
  - navigate to the CloudChatServer directory
  - run 'npm install' to install all necessary packages
  - run the command 'node server.js' to start up the CloudChat socket server

CloudChatDAO
  - navigate to 'Services/CloudChatDAO/dist' directory within 'CloudChatServer'
  - copy the file 'CloudChatDAO.war' to your Glassfish working directory

CloudChatNavigator
  - from the 'CloudChatServer/Services' directory, navigate to the 'CloudChatNavigator/dist' directory
  - copy the file 'CloudChatNavigator.war' to your Glashfish working directory

CloudChatUserManager
  - from the 'CloudChatServer/Services' directory, navigate to the 'CloudChatUserManager/dist' directory
  - copy the file 'CloudChatUserManager.war' to your tomcat working directory

In Netbeans
  - create a database called 'CloudChatDB' with username 'psu' and password 'wearepennstate'
  - connect to the newly created database
  - open the 'CloudChatDAO' package and navigate to the 'files' tab in Netbeans
  - run the 'CreateMessagesTable.sql', 'CreateRoomsTable.sql' and 'CreateUsersTable.sql' files
    against the newly created 'CloudChatDB' database

Startup Tomcat and Glassfish to deploy the necessary '.war' files

Optional: Copy the necessary '.war' files created in previous labs to the 'Glassfish' or 'Tomcat' directories
similar to what was described above. Alternatively, all services developed in previous labs can also be ran
by opening Netbeans and selecting 'Run' from the menu.
