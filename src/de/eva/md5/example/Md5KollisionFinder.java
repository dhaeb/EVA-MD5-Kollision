package de.eva.md5.example;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Md5KollisionFinder implements Callable<Optional<Integer>>{

	private int from, to;
	private Md5Hash searchable;
	
	public Md5KollisionFinder(int from, int to, Md5Hash searchable) {
		this.from = from;
		this.to = to;
		this.searchable = searchable;
	}

	public Optional<Integer> findCollision(Md5Hash searchable, int lowerBound, int upperBound) throws NoSuchAlgorithmException{
		Optional<Integer> optional = Optional.empty();
		Md5Calculator md5Calculator = new Md5Calculator();
		for(int i = lowerBound; i <= upperBound; i++){
			Md5Hash hash = md5Calculator.getHash(i);
			if(hash.equals(searchable)){
				optional = Optional.of(i);
				break;
			}
		}
		return optional;
	}
	
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
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException, ExecutionException {
		 ExecutorService threadPool = Executors.newFixedThreadPool(16);
		 Future<Optional<Integer>> futureResult = threadPool.submit(new Md5KollisionFinder(0, 100, new Md5Hash(5103841542109130789L, 33046678707426369L)));
		 Optional<Integer> hash = futureResult.get();
		 if(hash.isPresent()){
			 System.out.println("found hash: " + hash.get());
		 } else {
			 System.out.println("no collision could be detected!");
		 }
		 threadPool.shutdown();
		 threadPool.awaitTermination(10, TimeUnit.SECONDS);
	}

	@Override
	public Optional<Integer> call() throws Exception {
		return findCollision(searchable, from, to);
	}
}
