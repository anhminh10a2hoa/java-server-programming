����   4 �  servlet/SearchName  javax/servlet/http/HttpServlet serialVersionUID J ConstantValue        object Lservlet/PostRequestHandler; <init> ()V Code
      servlet/PostRequestHandler
  	   
  LineNumberTable LocalVariableTable this Lservlet/SearchName; doGet R(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;)V 
Exceptions  javax/servlet/ServletException   java/io/IOException " 	text/html $ & % &javax/servlet/http/HttpServletResponse ' ( setContentType (Ljava/lang/String;)V $ * + , 	getWriter ()Ljava/io/PrintWriter; . <html>
 0 2 1 java/io/PrintWriter 3 ( println 5 <head> 7 <title>Search</title> 9 </head> ; <body> = <h1>Error</h1> ? 6<p>This servlet does not handle HTTP Get requests!</p> A G<div style='text-align:center'><a href='index.html'>Main page</a></div> C </body> E </html> request 'Ljavax/servlet/http/HttpServletRequest; response (Ljavax/servlet/http/HttpServletResponse; out Ljava/io/PrintWriter; doPost N <title>Info</title> P 
nameSearch R T S %javax/servlet/http/HttpServletRequest U V getParameter &(Ljava/lang/String;)Ljava/lang/String;	  X Y Z data Ljava/util/Vector;
 \ ^ ] java/util/Vector _ ` get (I)Ljava/lang/Object; b servlet/Message
 a d e f getName ()Ljava/lang/String;
 h j i java/lang/String k l contains (Ljava/lang/CharSequence;)Z
 a n o f toString
 \ q r s size ()I u java/lang/StringBuilder w <h1>This name 
 t y  (
 t { | } append -(Ljava/lang/String;)Ljava/lang/StringBuilder;   does not exist</h1>
 t n Ljava/lang/String; count I i StackMapTable 
SourceFile SearchName.java !                 
            B     *� *� Y� � �                                          �     L,!� # ,� ) N--� /-4� /-6� /-8� /-:� /-<� /->� /-@� /-B� /-D� /�       6    #  %  '  (  ) ! * ' + - , 3 - 9 . ? / E 0 K 2    *    L       L F G    L H I   = J K   L            �     �,!� # ,� ) N--� /-4� /-M� /-8� /-:� /+O� Q :66� 1� W� [� a� c� g� -� W� [� a� m� /��� W� p���� -� tYv� x� z~� z� �� /-@� /-B� /-D� /�       R    <  >  @  A  B ! C ' D - F 7 G : H @ I V J h K k H y N ~ O � Q � R � S � T    H    �       � F G    � H I   � J K  7 t P �  : q � �  = < � �  �    � @   R $ 0 h  *� )  �    �