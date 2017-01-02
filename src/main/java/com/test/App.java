package com.test;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        ChatmeterReports reports = new ChatmeterReports();
        reports.getChatmeterLocationsReports();
    }
}
