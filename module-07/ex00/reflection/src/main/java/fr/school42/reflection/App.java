/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   App.java                                           :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: Younes <Younes@student.42.fr>              +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2025/06/16 14:12:59 by Younes            #+#    #+#             */
/*   Updated: 2025/06/18 14:13:54 by Younes           ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

package fr.school42.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class App 
{

    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main( String[] args ) throws Exception
    {
        printMenuOfClasses();

        String className = scanner.nextLine();
        Class<?> clazz = loadClassByName(className);
        if (clazz != null) {
            printClassFieldsAndMethods(clazz);
            Object object = createObjectFromInput(clazz);
            if (object != null) {
                updateField(object);
                invokeMethod(object);
            }
        }
    }

    private static void printMenuOfClasses() {
        
        System.out.println("Classes:");
        System.out.println("1. User");
        System.out.println("2. Car");
        System.out.println("---------------------");
        System.out.print("Enter class name: ");
    }

    private static Class<?> loadClassByName(String name) {

        String fullName = "fr.school42.classes." + name;
        
        try {
            
            return Class.forName(fullName);
        } catch (ClassNotFoundException e) {
            
            System.out.println("Class not found: " + name);
            return null;
        }
    }

    private static void printClassFieldsAndMethods(Class<?> clazz) {

        System.out.println("---------------------");
        System.out.println("fields:");

        for (Field field : clazz.getDeclaredFields()) {
            System.out.println("    " + field.getType().getSimpleName() + " " + field.getName());
        }

        System.out.println("methods:");
        for (Method method : clazz.getDeclaredMethods()) {
            if (isMethodFromObject(method)) continue;
            System.out.print("    " + method.getReturnType().getSimpleName() + " " + method.getName() + '(');
            Parameter[] parameters = method.getParameters();
            if (parameters.length == 0) {
                System.out.println(")");
                continue;
            }
            for (int i = 0; i < parameters.length; i ++) {
                System.out.print(parameters[i].getType().getSimpleName());
                if (i < parameters.length - 1) System.out.println(", ");
                    
                System.out.println(")");
            }
        }
        
    }

    private static boolean isMethodFromObject(Method method) {
    // Check if the method is from Object class by comparing with Object class methods
        try {
            Object.class.getMethod(method.getName(), method.getParameterTypes());
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private static Object parseValue(Class<?> type, String input) {
        
        if (type == String.class) return input; //i L D b
        if (type == int.class || type == Integer.class) return Integer.parseInt(input);
        if (type == long.class || type == Long.class) return Long.parseLong(input);
        if (type == double.class || type == Double.class) return Double.parseDouble(input);
        if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(input);

        return null;
    }

    private static Object createObjectFromInput(Class<?> clazz) {

        try {
            System.out.println("---------------------");
            System.out.println("Let's create an object.");

            Constructor<?> constructor = clazz.getDeclaredConstructors()[1];
            Parameter[] paramType = constructor.getParameters();
            Object [] args = new Object[paramType.length];

            for (int i = 0; i < paramType.length; i++) {
                System.out.println(paramType[i].getName() + ":");
                System.out.print("-> ");
                
                String input = scanner.nextLine();
                args[i] = parseValue(paramType[i].getType(), input);
            }

            Object object = constructor.newInstance(args);
            System.out.println("Object created: " + object);
            return object;
            
        } catch (Exception e) {
            System.out.println("Error creating object: " + e.getMessage());
            return null;
        }
    }
    
    private static void updateField(Object obj) {
        
        Class<?> clazz = obj.getClass();
        
        System.out.println("---------------------");
        System.out.print("Enter name of the field to change: ");

        String fieldName = scanner.nextLine();
        
        try {
            
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            System.out.println("Enter " + field.getType().getSimpleName() + " value");
            System.out.print("-> ");
            String value = scanner.nextLine();
            
            Object parsedValue = parseValue(field.getType(), value);
            field.set(obj, parsedValue);

            System.out.println("Object updated: " + obj);
            
        } catch (NoSuchFieldException e) {
            
            System.out.println("Field not found." + e);
        } catch (IllegalAccessException e) {
            
            System.out.println("Cannot accesse field.");
        }
    }

    private static void invokeMethod(Object obj) {

        Class<?> clazz = obj.getClass();

        System.out.println("---------------------");
        System.out.print("Enter method name to call: ");

        String methodName = scanner.nextLine();

        try {
            
            Method[] methods = clazz.getDeclaredMethods();
            Method targMethod = null;

            for (Method method : methods) {
                if (methodName.equals(method.getName() + "(" + getParameterTypes(method) + ")"))
                    targMethod = method;
            }
            
            if (targMethod == null) {
                System.err.println("Method not found.");
                return ;
            }

            targMethod.setAccessible(true);

            Class<?>[] paramType = targMethod.getParameterTypes();
            Object[] args = new Object[paramType.length];
            
            for (int i = 0; i < paramType.length; i++) {
                System.out.println("Enter " + paramType[i].getSimpleName() + " value:");
                System.out.print("-> ");
                String value = scanner.nextLine();
                args[i] = parseValue(paramType[i], value);
            }

            Object result = targMethod.invoke(obj, args);

            if (targMethod.getReturnType() != void.class) {
                System.out.println("Method returned: " + result);
            }
            
        } catch (Exception e) {
            System.out.println("Error calling method: " + e.getMessage());
        }
    }
    private static String getParameterTypes(Method method) {
        return Arrays.stream(method.getParameterTypes())
                    .map(Class::getSimpleName)
                    .collect(Collectors.joining(", "));
    }
}
