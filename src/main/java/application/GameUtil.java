package application;

import java.util.Random;

public class GameUtil {
	
	public static String GAME_ID;
	
	public static boolean MULTIPLAYER = false;
	
	public static void setGameId(String id){
		GAME_ID = id;
	}
	
	public static String createGameId(){
		GAME_ID = getSaltString(10);
		return GAME_ID;
	}
	
	public static String getSaltString(int length) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
