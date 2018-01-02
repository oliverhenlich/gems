package com.mangospice.gems.net;

import java.net.*;
import java.io.*;

public class TestHTTPS {

    public static void main(String[] args) throws Exception{
        URL url = new URL(args[0]);
        URLConnection con = url.openConnection();
        con.setDoOutput(true);
        con.connect();

        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        wr.write("Request data....");
        wr.flush();
        
        BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
        wr.close();
        rd.close();
    }
}