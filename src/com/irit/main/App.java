package com.irit.main;

import com.irit.upnp.AdapterServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        new Thread(new AdapterServer()).run();
    }
}
