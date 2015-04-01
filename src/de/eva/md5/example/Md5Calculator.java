package de.eva.md5.example;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Klasse zur Berechnung von MD5 Hashes von ganzen Zahlen.
 *
 * <p><b>Die Klasse ist nicht Thread Safe!!!</b></p>
 * 
 * @author Yves Annanias, Dan Haeberlein
 *
 */
public class Md5Calculator {

	/**
	 * Das Objekt md5 vom Typ MessageDigest erlaubt die Ausfuehrung des MD5-Algorithmus
	 *	http://download.oracle.com/javase/7/docs/api/java/security/MessageDigest.html
	 */
	private MessageDigest md5;
	
	public Md5Calculator() throws NoSuchAlgorithmException {
		md5 =  MessageDigest.getInstance("MD5");
	}

	/**
	 * Methode, um den Hash einer ganzen Zahl zu berechnen.
	 * 
	 * @param hashable ganze Zahl
	 * @return Hash-Wert der ganzen Zahl
	 */
	public Md5Hash getHash(int hashable){
		// notwendig, um die MD5 Berechnung mehrmals durchfuehren zu koennen
		md5.reset();
		// Mit dieser Anweisung wird ein ByteBuffer gross genug fuer ein int-Wert angelegt,
		// in den Buffer wird der Wert des Feldes "hashable" geschrieben
		// und als array in die Variable message geschrieben
		//
		byte[] message = ByteBuffer.allocate(4).putInt(hashable).array();
		// Message repraesentiert unsere Klartext-Nachricht, 
		// welche im naechsten Schritt der MD5 Hash Berechnung unterzogen wird
		md5.update(message);
		// Umwandlung in die Md5Hash Klasse, um den Kollisionstest durchführen zu koennen 
		return new Md5Hash(md5.digest());
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		// vorgegebener Hash-Wert der ganzen Zahl 5 
		Md5Hash md5HashOfIntValue5 = new Md5Hash(-4954387507941444427L, 7434082506426283588L);
		// Instantiierung der Hashberechnungsklasse
		Md5Calculator md5Calculator = new Md5Calculator();
		// errechneter Hash-Wert der ganzen Zahl 5
		Md5Hash calculatedHashValue = md5Calculator.getHash(5);
		// Kollisionstest
		boolean hasSameHash = calculatedHashValue.equals(md5HashOfIntValue5);
		// Ausgabe Ergebnis
		if(hasSameHash) {
			System.out.println("encoded und hash sind gleich");
		} else {
			System.out.println("encoded und hash sind nicht gleich");
		}
	}

}
