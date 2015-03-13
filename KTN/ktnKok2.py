# -*- coding: utf-8 -*-
from datetime import datetime
import json
import threading
import time
import SocketServer
import re
 
#global variable that holds all the messages being sent by all the clients
#This needs to be global to be able to share some data between all the ClientHandler threads.
#only read from it or append to it.
#whatever you append to all_messages will be broadcasted to all users.
all_messages = []
users = []
 
class ClientHandler(SocketServer.BaseRequestHandler):
    global all_messages
    global users
     
    def timestamp(self):
        return datetime.now().strftime("%H:%M")
     
    def broadcast(self, data):
        """sends data to all clients"""
        all_messages.append(data)
 
    def handle(self):
        #Get a reference to the socket object
        self.connection = self.request
        self.ip = self.client_address[0]
        self.port = self.client_address[1]
        self.logged_in = False
        self.sentdata = 0
             
        #Logg clienter som prøver å koble til
        print 'Client connected @' + self.ip + ':' + str(self.port)
         
                     
        # send_updates to work in a thread.
        self.t = threading.Thread(target=self.send_updates)
        self.t.setDaemon=True
        self.t.start()
         
        while True:
            #wait here for data
            data = self.connection.recv(1024).strip()
            # Check if the data exists
            # (recv could have returned due to a disconnect)
            if data:
                print(data)
                self.process_data(data)
                #print data
            else:
                print ('Client disconnected!')
                break  
         
    def finish(self):
        try:
            users.remove(self.username)
        except:
            pass
         
    def process_data(self, data):
        #Tilbake til json
        decoded = json.loads(data)
         
        if decoded['request'] == 'login':
          self.login(decoded.get('username', '')) 
 
        if not self.logged_in:
            return
         
        if decoded['request'] == 'names':
            print self.users

        elif decoded['request'] == 'logout':
          self.logout()
           
        elif decoded['request'] == 'message':
            if decoded.get("message", "") != "":
                padd=" "*(len(max(users, key=len))-len(self.username))
                message = self.timestamp()+padd+" %s| %s"%(self.username, decoded['message'])
                self.broadcast(message)

        elif decoded['request'] == 'help':
            print "'login + [username]'\n'message + [content]'\n'logout'\n'help'\n'names'"

     
 
    def login(self, username):
        #gyldig brukernavn?
        if(not re.match(r'^[A-Za-z0-9_]+$', username)):
            send({'response':'login', 'error':'Invalid username!', 'username':username}) 
            return
        if not username in users:
            self.username = username
            users.append(username)
            self.logged_in = True
            self.send({'response':'login', 'username':self.username})
            self.broadcast("*** " + self.username + " has joined the chat.")
 
        else:
            self.send({'response': 'login', 'error':'Name already taken!', 'username':username})
         
    def logout(self):
        try:
            users.remove(self.username)
            self.logged_in = False
            self.send({'response': 'logout', 'nick': self.username})
            self.broadcast("*** " + self.username + " has left the chat.")
        except ValueError:
            send({'response': 'logout', 'error':'Not logged in!', 'nick': self.username})
     
    def send(self, data):
        self.request.sendall(json.dumps(data))
     
    def send_updates(self):
        while True:
            if self.sentdata < len(all_messages) and self.logged_in:
                    #couldnt send a list directly, will fix with json later
                    for x in range(self.sentdata, len(all_messages)):
                        self.send({"response":"message", "message":all_messages[x]})
                        self.sentdata += 1
            #Have to sleep it, else it will try to drain the cpu
            time.sleep(0.2) #0.2 seconds
 
 
class ThreadedTCPServer(SocketServer.ThreadingMixIn, SocketServer.TCPServer):
    pass
 
 
# Kjøres når programmet startes
if __name__ == "__main__":
    # Definer host og port for serveren
    HOST = '78.91.50.242'
    PORT = 9996
 
    # Sett opp serveren
    server = ThreadedTCPServer((HOST, PORT), ClientHandler)

 
    # Aktiver serveren. Den vil kjøre til den avsluttes med Ctrl+C
    server.serve_forever()