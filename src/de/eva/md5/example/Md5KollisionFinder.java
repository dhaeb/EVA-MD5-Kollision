package de.eva.md5.example;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
		for(int i = lowerBound; i <= upperBound; i++){
			Md5Hash hash = md5Calculator.getHash(i);
			if(hash.equals(searchable)){
				return i;
			}
		}
		throw new NoSuchElementException();
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException, ExecutionException {
		 ExecutorService threadPool = Executors.newFixedThreadPool(16);
		 Md5Hash searchable = new Md5Hash(5103841542109130789L, 33046678707426369L);
		 Integer invokeAny;
		 try {
			invokeAny = threadPool.invokeAny(Arrays.asList(new Md5KollisionFinder[]{
					 new Md5KollisionFinder(0, 5000, searchable),
					 new Md5KollisionFinder(5001, 10000, searchable),
			 }));
			System.out.println("found hash: " + invokeAny);
		} catch (ExecutionException e) {
			System.out.println("no collision could be detected!");
		} 
		 threadPool.shutdown();
		 threadPool.awaitTermination(10, TimeUnit.SECONDS);
	}

	@Override
	public Integer call() throws Exception {
		return findCollision(searchable, from, to);
	}
}
