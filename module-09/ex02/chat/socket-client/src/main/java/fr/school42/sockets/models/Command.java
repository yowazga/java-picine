/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Command.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/07/08 15:07:34 by Younes            #+#    #+#             */
/*   Updated: 2025/07/08 15:19:01 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.sockets.models;

import java.util.Arrays;

import org.json.JSONObject;

public class Command {

    private String type;      // "command", "message", "response", "menu", "error"
    private String content;   // Body of message, command string, or instructions
    private String from;      // "client" or "server"
    private String[] options; // For menus, lists, etc.
    
    public Command() {
    }

    public Command(String type, String from, String content) {
        setType(type);
        setFrom(from);
        setContent(content);
    }

    public Command(String type, String from, String content, String[] options) {
        setType(type);
        setFrom(from);
        setContent(content);
        setOptions(options);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type != null && !type.isEmpty() && !type.isBlank() ? type : null;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content != null && !content.isBlank() && !content.isEmpty() ? content : null;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from != null && !from.isBlank() && !from.isEmpty() ? from : null;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "command [type=" + type + ", content=" + content + ", from=" + from + ", options="
                + Arrays.toString(options) + "]";
    }

    public JSONObject toJson() {
        
        JSONObject json = new JSONObject();

        json.put("type", type);
        json.put("content", content);
        json.put("from", from);
        json.put("options", options);
        
        return json;
    }

    public static Command fromJson(JSONObject json) {
        
        Command command = new Command();

        command.setType(json.optString("type", null));
        command.setContent(json.optString("content", null));
        command.setFrom(json.optString("from", null));
        command.
            setOptions(json.has("options") ? json.getJSONArray("options").toList().toArray(new String[0]) : new String[0]);
        return command;
    }
    
    public static Command fromJson(String jsonsString) {
        
        if (jsonsString == null || jsonsString.isEmpty() || jsonsString.isBlank()) {
            throw new IllegalArgumentException("Invalid command");
        }
        JSONObject json = new JSONObject(jsonsString);
        
        return fromJson(json);
    }
    
}
