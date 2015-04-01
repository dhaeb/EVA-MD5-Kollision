package de.eva.md5.example;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * Not Threadsafe class to find Md5Collisions
 * 
 * @author Yves Annanias, Dan Haeberlein
 *
 */
public class Md5KollisionFinder implements Runnable {

	private Optional<Integer> collisionResult;

	private int lowerBound, upperBound;
	private Md5Hash searchable;

	public Md5KollisionFinder(int lowerBound, int upperBound, Md5Hash searchable) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.searchable = searchable;
	}

	public Optional<Integer> findCollision(Md5Hash searchable, int upperBound) throws NoSuchAlgorithmException {
		return findCollision(searchable, 0, upperBound);
	}

	public Optional<Integer> findCollision(Md5Hash searchable, int lowerBound, int upperBound) throws NoSuchAlgorithmException {
		Optional<Integer> optional = Optional.empty();
		Md5Calculator md5Calculator = new Md5Calculator();
		for (int i = lowerBound; i <= upperBound; i++) {
			Md5Hash hash = md5Calculator.getHash(i);
			if (hash.equals(searchable)) {
				optional = Optional.of(i);
				break;
			}
		}
		return optional;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException {
		Md5KollisionFinder finder = new Md5KollisionFinder(0, 100, new Md5Hash(5103841542109130789L, 33046678707426369L));
		Thread threadForCollisionFinding = new Thread(finder);
		threadForCollisionFinding.start();
		threadForCollisionFinding.join();
		Optional<Integer> hash = finder.getCollisionResult();
		if (hash.isPresent()) {
			System.out.println("found hash: " + hash.get());
		} else {
			System.out.println("no collision could be detected!");
		}
	}

	public Optional<Integer> getCollisionResult() {
		return collisionResult;
	}

	public void setCollisionResult(Optional<Integer> collisionResult) {
		this.collisionResult = collisionResult;
	}

	@Override
	public void run() {
		try {
			setCollisionResult(findCollision(searchable, lowerBound, upperBound));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace(); // sollte niemals auftreten!
		}
	}

}
