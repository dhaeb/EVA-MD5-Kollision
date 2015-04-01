package de.eva.md5.example;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Md5KollisionFinder implements Callable<Integer>{

	private int from, to;
	private Md5Hash searchable;
	
	public Md5KollisionFinder(int from, int to, Md5Hash searchable) {
		this.from = from;
		this.to = to;
		this.searchable = searchable;
	}

	public Integer findCollision(Md5Hash searchable, int lowerBound, int upperBound) throws NoSuchAlgorithmException{
		Md5Calculator md5Calculator = new Md5Calculator();
		for(int i = lowerBound; i <= upperBound && !Thread.currentThread().isInterrupted(); i++){
			Md5Hash hash = md5Calculator.getHash(i);
			if(hash.equals(searchable)){
				return i;
			}
		}
		throw new NoSuchElementException(String.format("no element between %d and %d", from, to));
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException, ExecutionException {
		 int countTheads = 16;
		ExecutorService threadPool = Executors.newFixedThreadPool(countTheads);
		 Md5Hash searchable = new Md5Hash(	8248337957008271949L, 7217552169022328908L);
		 Integer result;
		 try {
			 int fractionProThread = Integer.MAX_VALUE / countTheads;
			 List<Md5KollisionFinder> finders = new ArrayList<Md5KollisionFinder>();
			 for(int i = 0; i < countTheads; i++){
				 finders.add(new Md5KollisionFinder(i * fractionProThread, (i+1) * fractionProThread, searchable));
			 }
			result = threadPool.invokeAny(finders);
			System.out.println("found hash: " + result);
		} catch (ExecutionException e) {
			System.out.println("no collision could be detected!");
		} 
		 threadPool.shutdownNow();
		 threadPool.awaitTermination(10, TimeUnit.MINUTES);
	}

	@Override
	public Integer call() throws NoSuchAlgorithmException {
		return findCollision(searchable, from, to);
	}
}
