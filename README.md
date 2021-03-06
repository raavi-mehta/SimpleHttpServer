# SimpleHttpServer
This is a very simple multithreaded HTTP server that supports non-persistent
HTTP GET requests for simultaneously serving files to multiple clients. I did
this using only Java sockets (Url, UrlConnection, etc. not used) to reduce
abstraction so that I could better understand how HTTP requests and TCP
connections work.

This server is capable of responding with the following status codes:
- 200 OK            (The file exists on the server)
- 404 Not Found     (The file requested was not found on the server)
- 400 Bad Request   (The request was malformed)

## Usage
### Starting the Server
To start the server, compile all the source files in /src/ using

`javac *.java`

and start the server by executing `ServerLauncher` using

`java ServerLauncher`

By default, the server runs on port 2225, however, you may specify a port as an
argument to ServerLauncher:

`java ServerLauncher <port number>`

### Downloading Files
Once the server is running, you may use a browser to download files from your
server. For example, if you are accessing the server locally, you may download
files by typing into your address bar:

`localhost:2225/test_directory/testfile`

where "2225" is the port number the server is running on, and 
"/test_directory/testfile" is the path to the file.

Here, the "root" directory for your server is considered the location from where
the server is running. For example, if your server is in the directory

`/home/raavi/server`

then the path for "testfile" would be

`/home/raavi/server/test_directory/testfile`

within the same directory.