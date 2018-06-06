# RSS Reader Back End

Tech Stack: Java(Spring Boot), Pivotal Cloud Foundry

application.properties
default.rss.feed - comma separated values for default rss feed
exclude.words - comma separated values for the words to be excluded in conversion


Web services

root service: /manage-rss

/get-default-list - returns the list of default rss feed based on the application.properties default.rss.feed
/get-feed - returns the rss feed object

/download-feed - returns rss feed text file format

/download-item - returns rss feed item text file format
