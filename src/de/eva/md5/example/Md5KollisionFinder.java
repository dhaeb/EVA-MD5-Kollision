package de.eva.md5.example;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class Md5KollisionFinder {

	public Optional<Integer> findCollision(Md5Hash searchable, int upperBound) throws NoSuchAlgorithmException{
		Optional<Integer> optional = Optional.empty();
		Md5Calculator md5Calculator = new Md5Calculator();
		for(int i = 0; i <= upperBound; i++){
			Md5Hash hash = md5Calculator.getHash(i);
			if(hash.equals(searchable)){
				optional = Optional.of(i);
				break;
			}
		}
		return optional;
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		 Md5KollisionFinder finder = new Md5KollisionFinder();
		 Optional<Integer> hash = finder.findCollision(new Md5Hash(5103841542109130789L, 33046678707426369L), 100);
		 if(hash.isPresent()){
			 System.out.println("found hash: " + hash.get());
		 } else {
			 System.out.println("no collision could be detected!");
		 }
	}
}
