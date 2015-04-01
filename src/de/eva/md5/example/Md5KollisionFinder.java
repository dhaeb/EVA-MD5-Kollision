package de.eva.md5.example;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
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
		Md5Hash searchable = new Md5Hash(8248337957008271949L, 7217552169022328908L);
		List<Md5KollisionFinder> finders= new ArrayList<Md5KollisionFinder>();
		List<Thread> finderThreads = new ArrayList<Thread>();
		int countThreads = 16;
		int fractionForThread = Integer.MAX_VALUE / countThreads;
		for(int i = 0; i < countThreads ; i++){
			Md5KollisionFinder curFinder = new Md5KollisionFinder(i * fractionForThread, (i+1) * fractionForThread, searchable);
			finders.add(curFinder);
			Thread t = new Thread(curFinder);
			finderThreads.add(t);
			t.start();
		}
		for(Thread curThread : finderThreads){
			curThread.join();
		}
		for(Md5KollisionFinder finder : finders){
			checkForCollision(finder);
		}
	}

	private static void checkForCollision(Md5KollisionFinder finder) {
		Optional<Integer> hash = finder.getCollisionResult();
		int from = finder.getLowerBound();
		int to = finder.getUpperBound();
		if (hash.isPresent()) {
			System.out.println("found hash: " + hash.get());
		} else {
			System.out.println(String.format("no collision could be detected in the range of %d to %d!", from, to));
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

	public int getLowerBound() {
		return lowerBound;
	}

	public int getUpperBound() {
		return upperBound;
	}

}
