To configure your database connection,

Create a file in src/main/webapp/META-INF/context.xml and add content similar to:

<?xml version="1.0" encoding="UTF-8"?>
 
<Context antiJARLocking="true" path="/">
    <Resource
            name="jdbc/feedbackdb"
            auth="Container"
            type="javax.sql.DataSource"
            username="***"
            password="*****"
            driverClassName="com.mysql.jdbc.Driver"
            url="jdbc:mysql://feedbackdb.com:3306/docs"
            initialSize="0"
            maxActive="20"
            maxIdle="10"
            minIdle="0"
            maxWait="-1"
            validationQuery="SELECT 1"
            testOnBorrow="true"
            poolPreparedStatements="true"
            removeAbandoned="true"
            removeAbandonedTimeout="60"
            logAbandoned="true"/> 
</Context>

