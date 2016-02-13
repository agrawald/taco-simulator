# storm-tester

A simple tool to test trooper and footman. This tool will imitate the behaviour of TACO and will replay all the message.

The tool is made to perform following tasks

1. Act as a consumer and listen to the TACO messages from rabbit and save it to mongodb to be replayed later. This can be done using Aplpication.java file.
2. Can simulate the behviour of TACO and can replay the messages from mongodb in order to the rabbitmq for trooper and footman. This is done using starting the application using DumperApplication.java.

This tool will let the developer test the storm topologies with actual load test data.
