/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   ClientHandler.java                                 :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/03 10:32:08 by Younes            #+#    #+#             */
/*   Updated: 2025/12/22 10:35:48 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import fr.school42.sockets.models.Message;
import fr.school42.sockets.models.Room;
import fr.school42.sockets.models.User;
import fr.school42.sockets.services.MessagesService;
import fr.school42.sockets.services.RoomsService;
import fr.school42.sockets.services.UsersService;
import fr.school42.sockets.shared.Command;

public class ClientHandler extends Thread {

    private final Socket clientSocket;
    private final UsersService usersService;
    private final MessagesService messagesService;
    private final RoomsService roomsService;
    private User signedInUser;
    private Room instanceRoom;

    PrintWriter out;
    
    public ClientHandler(Socket clientSocket, UsersService usersService, MessagesService messagesService, RoomsService roomsService) {
        this.clientSocket = clientSocket;
        this.usersService = usersService;
        this.messagesService = messagesService;
        this.roomsService = roomsService;
        start();
    }


    @Override
    public void run() {
        
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            
            Command command = new Command("response", "server", "Hello from server!");
            
            out.println(command.toJson());
            try {
                while (true) {
                    
                    sendMenu(out);
                    String chosed = Command.fromJson(in.readLine()).getContent();
                    if (chosed == null)
                        return ;
                    switch (chosed) {
                        case "1" -> {
                            if (authenticateUser(in, out)) {
                                handlRoomMenu(in, out);
                            }
                        }
                        case "2" -> signUp(in, out);
                        case "3" -> {
                            out.println(new Command("response", "server", "Goodbye!").toJson());
                            return;
                        }
                        default -> out.println(new Command("error", "server", "Invalid choice. Please try again.").toJson());
                    }
                }
            } catch (IOException e) {
                out.println(new Command("error", "server", e.getMessage()).toJson());
                System.out.println("Caught exception: " + e.getMessage());
            }
            
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
            Server.clientActive.remove(this);
        }
    }

    private void signUp(BufferedReader in, PrintWriter out) throws IOException {
        
        out.println(new Command("response", "server", "Enter username").toJson());
        String username = Command.fromJson(in.readLine()).getContent();

        out.println(new Command("response", "server", "Enter passwowrd").toJson());
        String password = Command.fromJson(in.readLine()).getContent();

        User user = usersService.signUp(username, password);
        if (user != null) {
            out.println(new Command("response", "server", "uccessful!"));
        } else {
            out.println(new Command("response", "error", "Registration failed"));
        }
    }

    private void sendMenu(PrintWriter out) {
        
        Command command = new Command("menu", "server", "Main menu", new String[] {
            "1. SignIn", "2. SignUp", "3. Exit" });

        out.println(command.toJson());
    }

    private boolean authenticateUser(BufferedReader in, PrintWriter out) throws IOException {
        
        out.println(new Command("response", "server", "Enter username:").toJson());
        String username = Command.fromJson(in.readLine()).getContent();

        out.println(new Command("response", "server", "Enter password:").toJson());
        String password = Command.fromJson(in.readLine()).getContent();

        signedInUser = usersService.signIn(username, password);
        
        out.println(new Command("response", "server", "Successfully signed in!").toJson());
        
        return true;
    }

    private void handlRoomMenu(BufferedReader in, PrintWriter out) throws IOException {
        
        while (true) {
            
            sendRoomMenu(out);
            String chosed = Command.fromJson(in.readLine()).getContent();
            if (chosed == null)
                return ;
            switch (chosed) {
                case "1" -> createRoom(in, out);
                case "2" -> {
                    if (choosRoom(in, out)) {
                        handlChat(in, out);
                    }
                }
                case "3" -> {
                    return ;
                }
                default -> {
                }
            }
        }
    }

    private void sendRoomMenu(PrintWriter out) {
        
        Command command = new Command("response", "server", "Room menu", new String[] {
            "1. Create room", "2. Choose room", "3. Exit"});
        out.println(command.toJson());
    }

    private void createRoom(BufferedReader in, PrintWriter out) throws IOException {
        
        out.println(new Command("response", "server", "Enter Room name:").toJson());
        String roomName = Command.fromJson(in.readLine()).getContent();

        Room room = roomsService.createRoom(roomName);
        if (room != null) {
            out.println(new Command("response", "server", "Room created successfully!").toJson());
        } else {
            out.println(new Command("response", "server", "ailed to create room").toJson());
        }
    }

    private boolean choosRoom(BufferedReader in, PrintWriter out) throws IOException {
        
        List<Room> rooms = roomsService.getAllRooms();
        
        String roomsOptin[] = new String[rooms.size()];
        for (int i = 0; i < rooms.size(); i++) {
            roomsOptin[i] = (i + 1) + ". " + rooms.get(i).getName();
        }

        Command command = new Command("response", "server", "Rooms", roomsOptin);
        out.println(command.toJson());

        String chosed = Command.fromJson(in.readLine()).getContent();
        if (chosed == null)
            return false;
        int choseNum;
        try {
            choseNum = Integer.parseInt(chosed);
        } catch (NumberFormatException e) {
            out.println(new Command("error", "server", "Invalid choice").toJson());
            return false;
        }
        if (choseNum > 0 && choseNum <= rooms.size()) {
            
            instanceRoom = rooms.get(choseNum - 1);
            out.println(new Command("response", "server", instanceRoom.getName() + "----").toJson());
            getLastesMessages(out);
            return true;
        } else if (choseNum == rooms.size() + 1) {
            return false;
        } else {
            out.println(new Command("error", "server", "Invalid choice").toJson());
            return false;
        }
    }

    private void getLastesMessages(PrintWriter out) {
        
        List<Message> messages = messagesService.getLatestMessages(instanceRoom.getId(), 30, 0);

        String[] options = new String[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            options[i] = messages.get(i).getSender().getLogin() + ": " + messages.get(i).getMessage();
        }

        Command command = new Command("response", "server", "Latest Messages", options);
        out.println(command.toJson());
    }

    private void handlChat(BufferedReader in, PrintWriter out) throws IOException {
        
        while (true) {
            Command command = Command.fromJson(in.readLine());
            if (command == null)
                return ;
            if ("Exit".equalsIgnoreCase(command.getContent())) {
                out.println(new Command("response", "server", "You have left the chat.").toJson());
                instanceRoom = null;
                break ;
            }
            
            try {
                
                Message message = messagesService.saveMessag(signedInUser.getLogin(), instanceRoom.getId(), command.getContent());
                broadcastMessage(signedInUser.getLogin(), message.getMessage());
            } catch (IllegalArgumentException e) {
                out.println(new Command("error", "server", e.getMessage()).toJson());
            }
        }
    }

    private void broadcastMessage(String senderUsername, String textMessage) {
        
        Command message = new Command("message", senderUsername, textMessage);
        
        for (ClientHandler client : Server.clientActive) {
            if (client.instanceRoom != null && client.instanceRoom.getId().equals(this.instanceRoom.getId())) {
                client.sendMessageToClient(message.toJson().toString());
            }
        }
    }

    private void sendMessageToClient(String jsonMessage) {
        
        try {
            out.println(jsonMessage);
        } catch (Exception e) {
            System.out.println("Failed to send message to client: " + e.getMessage());
        }
    }
    
}
