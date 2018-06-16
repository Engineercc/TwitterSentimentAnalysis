/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zemberek.util;


import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

public class clientProvider {
    private static clientProvider instance = null;
    private static Object lock      = new Object();
    
    private Client client;
    private Node node;

    public static clientProvider instance(){
        
        if(instance == null) { 
            synchronized (lock) {
                if(null == instance){
                    instance = new clientProvider();
                }
            }
        }
        return instance;
    }

    public void prepareClient(){
        node   = nodeBuilder().node();
        client = node.client();
    }

    public void closeNode(){
        
        if(!node.isClosed())
            node.close();

    }
    
    public Client getClient(){
        return client;
    }
    
    
    public void printThis() {
        System.out.println(this);
    }
}
    
