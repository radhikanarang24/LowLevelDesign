package com.lld;

import java.util.HashMap;

public class FixedWindowRateLimiter {
    public static void main (String args[]){


        //5 requests are allowed peer 3 sec
        FixedRateLimiter fixedRateLimiter = new FixedRateLimiter(5, 3);

        System.out.println(fixedRateLimiter.rateLimit("user1", 1));//t
        System.out.println(fixedRateLimiter.rateLimit("user1", 2));//t
        System.out.println(fixedRateLimiter.rateLimit("user1", 2));//t
        System.out.println(fixedRateLimiter.rateLimit("user1", 3));//t
        System.out.println(fixedRateLimiter.rateLimit("user1", 3));//t
        System.out.println(fixedRateLimiter.rateLimit("user1", 3));//false

        System.out.println(fixedRateLimiter.rateLimit("user1", 6)); //true




    }
}

class UserData {

    private int count;
    private int startTime;

    public UserData(int count, int startTime){
        this.count = count;
        this.startTime= startTime;
    }

    public int getCount(){
        return count;
    }
    public void incCount(int val){
        this.count += val;
    }

    public int getStartTime(){
        return startTime;
    }

    public void setStartTime(int startTime){
        this.startTime = startTime;
    }
}

class FixedRateLimiter{

    private HashMap<String, UserData> data;
    private int allowedRequests;
    private int timeInterval;

    public FixedRateLimiter(int allowedRequests, int timeInterval){
        this.data = new HashMap<>();
        this.allowedRequests = allowedRequests;
        this.timeInterval = timeInterval;
    }

    public boolean rateLimit(String userId, int hitTime){

        if(data.get(userId)==null){

            data.put(userId, new UserData(1,hitTime));
            return true;
        } else {

            UserData userdata = data.get(userId);

            if(hitTime-userdata.getStartTime()<timeInterval){

                if((userdata.getCount() + 1) <= allowedRequests){

                    userdata.incCount(1);
                    return true;
                }else {

                    return false;
                }
            } else {
                //resetting info as we are now in next window

                userdata = new UserData(1, hitTime);
                return true;
            }

        }
    }
}