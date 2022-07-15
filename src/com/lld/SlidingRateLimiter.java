package com.lld;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*

CODE : https://github.com/TheTechGranth/thegranths/tree/master/src/main/java/SystemDesign/RateLimiter

BLOG : https://www.enjoyalgorithms.com/blog/design-api-rate-limiter

CAN CHECK THIS LATER : https://codereview.stackexchange.com/questions/252915/rate-limiter-implementation-in-java
*/

public class SlidingRateLimiter {
    public static void main(String[] args) {
        //System.out.println("Hello World!");
        UserBucketCreator userBucketCreator=new UserBucketCreator(1,1,5);
        //UserBucketCreator userBucketCreator2=new UserBucketCreator(2,1,5);
        ExecutorService executors= Executors.newFixedThreadPool(7);
        for(int i=0;i<7;i++)
        {
            executors.execute(()->userBucketCreator.accessService(1));
           // executors.execute(()->userBucketCreator2.accessService(2));
        }

        executors.shutdown();
    }
}

class SlidingWindow{
    Queue<Long> slidingWindow;
    int timeWindowInSeconds;
    int bucketCapacity;

    SlidingWindow(int timeWindowInSeconds, int bucketCapacity)
    {
        this.timeWindowInSeconds=timeWindowInSeconds;
        this.bucketCapacity=bucketCapacity;
        slidingWindow=new ConcurrentLinkedQueue<>();
    }

    public boolean grantAccess() {
        long currentTime = System.currentTimeMillis();
        checkAndUpdateQueue(currentTime);
        if(slidingWindow.size() < bucketCapacity){
            slidingWindow.offer(currentTime);
            return true;
        }
        return false;
    }

    private void checkAndUpdateQueue(Long currentTime){
        if(slidingWindow.isEmpty()) return;

        long time=(currentTime-slidingWindow.peek())/1000;
        while(time>=timeWindowInSeconds)
        {
            slidingWindow.poll();
            if(slidingWindow.isEmpty()) break;
            time=(currentTime-slidingWindow.peek())/1000;
        }
    }

}

class UserBucketCreator{
    Map<Integer,SlidingWindow> bucket;

    UserBucketCreator(int id, int timeWindowInSeconds,int bucketCapacity)
    {
        bucket=new HashMap<>();
        bucket.put(id,new SlidingWindow(timeWindowInSeconds,bucketCapacity));
    }

    public void accessService(int id)
    {
        if(bucket.get(id).grantAccess())
        {
            System.out.println(Thread.currentThread().getName()+" -> able to access service for user Id : "+id);
        }
        else
        {
            System.out.println(Thread.currentThread().getName()+" -> blocked to access service, Please try again after sometime for user Id : "+id);
        }
    }
}