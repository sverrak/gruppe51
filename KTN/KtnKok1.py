# -*- coding: utf-8 -*-
import socket
import json
import threading
import re
 
class Client(object):
 
    def __init__(self):
        #TCP stream
        self.connection = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
     
    def process_json(self, data):
        index = 0
        while data.find("{", index) >= 0:
            start = data.find("{", index)
            end = data.find("}", start)
            index = end
            self.process_data(data[start:end+1])
     
    def process_data(self, data):
        decoded = json.loads(data)
        if decoded.get("response", "") == "login":
            if decoded.get("error", "") != "":
                print decoded["error"], "(%s)"%decoded.get("username", "")
            else:
                self.logged_in = True
            if decoded.get("messages", "") != "":
                print decoded["messages"].encode('utf-8')
 
        if not self.logged_in:
            return
         
        if decoded.get("response", "") == "logout":
            self.logged_in = False
 
        if decoded.get("response", "") == "message":
            print decoded["message"].encode('utf-8')
                 
 
    def start(self, host, port):
        self.__init__()
        #print "Welcome to Squeak!\nPlease specify server ip:port, or leave blank for the defaults "+host+":"+str(port)
        innInfo="78.91.50.242:9997"
        if innInfo:
            host=innInfo.split(":")[0]
            port=int(innInfo.split(":")[1])
             
        self.connection.connect((host, port))
        self.logged_in = False 
        self.commands = {"/logout":self.disconnect, "/help":self.help, "/names":self.names}
         
        while not self.logged_in:
            self.username = raw_input('Username: ')
            self.login()
            #svar på innlogging
            response = self.connection.recv(1024).strip()
            self.process_json(response)
         
        #make take_input work in a thread.
        t = threading.Thread(target=self.take_input)
        t.setDaemon=True
        t.start()
         
        while self.logged_in:
            received_data = self.connection.recv(1024).strip()
            self.process_json(received_data)
        self.connection.close()
 
 
 
    def send(self, data):
        #Sendall sender all data til server
        self.connection.sendall(data)
         
    def login(self):
        self.send(self.parse({'request':'login', 'username':self.username}))
     
    def help(self):
        self.send(self.parse({'request':'help'}))

    def names(self):
        self.send(self.parse({'request':'names'}))

    def disconnect(self):
        self.send(self.parse({'request':'logout'}))
         
    def parse(self, data):
        #konverterer til streng
        return json.dumps(data)
     
    def take_input(self):
        """lets the user write a message"""
        while True:
            #this thread stops here until it has data, so no need for time.sleep
            data = raw_input()
            command = re.findall("^[/]\w+", data)
            if command:
                if command[0] in self.commands:
                    self.commands[command[0]]()
                elif command[1] in self.commands:
                    self.commands[command[1]]()
                elif command[2] in self.commands:
                    self.commands[command[2]]()
            
            if data != "":
                self.send(self.parse({"request":"message", "message":data}))
 
if __name__ == "__main__":
    client = Client()
    client.start('localhost', 9999)