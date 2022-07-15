package com.lld;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// "static void main" must be defined in a public class.
class Node
{
    private int key;
    private int val;
    Node next;
    Node prev;
    Node()
    {
        this.key=0;
        this.val=0;
    }
    Node(int key,int val){
        this.key=key;
        this.val=val;
    }

    public int getKey(){ return this.key;}
    public int getVal(){ return this.val;}
}

public class LRUCache {

    private ConcurrentHashMap<Integer,Node> cache;
    private int capacity;
    private AtomicInteger curSize=new AtomicInteger();
    private ReentrantReadWriteLock lock=new ReentrantReadWriteLock();
    private Node tail=new Node();
    private Node head=new Node();

    public LRUCache(int cap)
    {
        this.capacity=cap;
        cache=new ConcurrentHashMap<>();

        //this.head=new Node(0,0);
        //this.tail=new Node(0,0);
        this.tail.prev=this.head;
        this.head.next=this.tail;
    }

    public void remove(Node node)
    {
        try{
            lock.writeLock().lock();
            Node prev=node.prev;
            Node next=node.next;
            prev.next=next;
            next.prev=prev;
        }
        finally{
            lock.writeLock().unlock();
        }
    }

    public void insert(Node node)
    {
        try
        {
            lock.writeLock().lock();
            Node next=this.head.next;
            Node prev=this.head;
            node.next=next;
            node.prev=prev;
            next.prev=node;
            prev.next=node;
        }
        finally{
            lock.writeLock().unlock();
        }
    }


    public int get(int key)
    {
        if(cache.containsKey(key))
        {
            Node node=cache.get(key);
            remove(node);
            insert(node);
            return node.getVal();
        }
        else return -1;
    }

    public void put(int key,int val)
    {
        if(cache.containsKey(key))
            remove(cache.get(key));

        cache.put(key,new Node(key,val));
        //System.out.println("size: "+cache.size()+" "+curSize);
        insert(cache.get(key));

        if(curSize.incrementAndGet()>capacity)
        {
            //System.out.println("size 1: "+cache.size()+" "+curSize);
            Node last=this.tail.prev;
            remove(last);
            cache.remove(last.getKey());
            curSize.decrementAndGet();
        }
        //System.out.println("size 2: "+cache.size()+" "+curSize);
    }

    public static void main(String[] args) {
        LRUCache lru=new LRUCache(2);
        System.out.println(lru.get(1));

        lru.put(1,1);
        lru.put(2,2);
        lru.put(3,3);
        System.out.println(lru.get(1));
        System.out.println(lru.get(2));
        System.out.println(lru.get(3));
    }
}
