# -*- coding: utf-8 -*-
import socket
import json
import threading
import re
from MessageReceiver import MessageReceiver
 
class Client(object):
 
    def __init__(self):
        #TCP stream
        self.connection = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
#        self.messageReceiver = MessageReceiver(self, self.connection) #nytt. FEILMELDING: module object is not callable
     
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
            print "You are now logged out."
            self.logged_in = False
            self.connection = None
            self.disconnect()
 
        if decoded.get("response", "") == "message":
            print decoded["message"].encode('utf-8')

        if decoded.get("response", "") == "names":
            print self.nameprinter(decoded["names"].encode('utf-8'))

        if decoded.get("response", "") == "help":
            print decoded["help"].encode('utf-8')
            # her skjer det fint lite
 
    def start(self, host, port):
        self.__init__()

        inInfo="78.91.83.113:9949"
        if inInfo:
            host=inInfo.split(":")[0]
            port=int(inInfo.split(":")[1])
             
        self.connection.connect((host, port))
       # messageReceiver.start()       #nytt. i stedet for run. virker rart, men det Daniel sa
        self.logged_in = False 
        self.commands = {"logout":self.disconnect, "help":self.help, "names":self.names}
         
        while not self.logged_in:
            self.username = raw_input('Username: ')
            self.login()
            #svar på innlogging
            response = self.connection.recv(1024).strip()
            self.process_json(response)
         
        t = threading.Thread(target=self.take_input)
        t.setDaemon=True
        t.start()
        print "\nType 'help' if you don't know what to do\n"
        print "Chat history:"
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

    def nameprinter(self, data):
        colors = Colors()
        return colors.OKGREEN + data + colors.ENDC

    def message(self, data):
        self.send(self.parse({"request":"message", "message":data}))

    def disconnect(self):
        self.send(self.parse({'request':'logout'}))
         
    def parse(self, data):
        #konverterer til streng
        return json.dumps(data)
     
    def take_input(self):
        """lets the user write a message"""
        while True:
            
            data = raw_input()
            command = data
            command = command.split(" ")[0]
            if command:
                if command in self.commands:
                    self.commands[command]()
                    continue

            if command == "msg":
                self.send(self.parse({"request":"message", "message":data[3:]}))

class Colors():
    OKGREEN = '\033[92m'
    ENDC = '\033[0m'


if __name__ == "__main__":
    client = Client()
    client.start('localhost', 9999)
