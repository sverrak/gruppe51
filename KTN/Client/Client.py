# -*- coding: utf-8 -*-
import socket

class Client:
    """
    This is the chat client class
    """
    username = None;

    def __init__(self, host, server_port):
        """
        This method is run when creating a new Client object
        """

        # Set up the socket connection to the server
        self.connection = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.login()
        self.run()

        
        # TODO: Finish init process with necessary code

    def run(self):
        # Initiate the connection to the server
        self.connection.connect((self.host, self.server_port))

    def login(self):
        username = input("Your username: ")
    def logout(self):
        username = None;

    def disconnect(self):
        # TODO: Handle disconnection

        self.connection = None;

        pass

    def receive_message(self, message):
        print self + ": " + message
        pass

    def send_payload(self, data):
        #hva som gjøres når en melding sendes
        # TODO: Handle sending of a payload
        pass


if __name__ == '__main__':
    """
    This is the main method and is executed when you type "python Client.py"
    in your terminal.

    No alterations is necessary
    """
    client = Client('localhost', 9998)
