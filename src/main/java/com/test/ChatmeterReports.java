package com.test;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.*;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class ChatmeterReports {

    AmazonS3 s3 = new AmazonS3Client();

    public void getChatmeterLocationsReports() {
        HashMap<String, ChatmeterAccountLocations> locationsFromChatmeter = getLocationsCountFromChatmeter();
        HashMap<String, ChatmeterAccountLocations> locationsFromS3 = getLocationsCountFromS3();
        Set<ChatmeterAccountLocations> comparedLocations = getComparedLocationsCount(locationsFromChatmeter,
                                                                locationsFromS3);
    }

    private HashMap<String, ChatmeterAccountLocations> getLocationsCountFromChatmeter() {
        HashMap locations = new HashMap<String, ChatmeterAccountLocations>();
        ChatmeterAccountLocations account1 = new ChatmeterAccountLocations("aaa", "111");
        ChatmeterAccountLocations account2 = new ChatmeterAccountLocations("bbb", "222");
        ChatmeterAccountLocations account3 = new ChatmeterAccountLocations("ccc", "333");

        locations.put("aaa", account1);
        locations.put("bbb", account2);
        locations.put("ccc", account3);

        return locations;
    }

    private HashMap<String, ChatmeterAccountLocations> getLocationsCountFromS3() {
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));
        S3Object locationsFile = s3.getObject(new GetObjectRequest("bvlocal", "test/locations_report/ChatmeterLocationsReport"));
        ObjectInputStream ois = null;
        HashMap<String, ChatmeterAccountLocations> accounts = new HashMap<String, ChatmeterAccountLocations>();

        try {
            ois = new ObjectInputStream(locationsFile.getObjectContent());
            accounts = (HashMap) ois.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return accounts;
    }

    private Set<ChatmeterAccountLocations> getComparedLocationsCount(
            HashMap<String, ChatmeterAccountLocations> locationsFromChatmeter,
            HashMap<String, ChatmeterAccountLocations> locationsFromS3) {
        TreeSet<ChatmeterAccountLocations> result = new TreeSet<ChatmeterAccountLocations>();

        for (String accountName: locationsFromChatmeter.keySet()) {
            ChatmeterAccountLocations accountFromChatmeter = locationsFromChatmeter.get(accountName);
            ChatmeterAccountLocations accountFromS3 = locationsFromChatmeter.get(accountName);

            if (locationsFromS3.containsKey(accountName)) {
                accountFromChatmeter.setAuditLocationsDiff(accountFromChatmeter.getAuditLocationsCount()
                        - accountFromS3.getAuditLocationsCount());
                accountFromChatmeter.setBillableLocationsDiff(accountFromChatmeter.getBillableLocationsCount()
                        - accountFromS3.getBillableLocationsCount());
            } else {
                accountFromChatmeter.setAuditLocationsDiff(accountFromChatmeter.getAuditLocationsCount());
                accountFromChatmeter.setBillableLocationsDiff(accountFromChatmeter.getBillableLocationsCount());
            }
            locationsFromS3.remove(accountName);
            result.add(accountFromChatmeter);
        }

        if (locationsFromS3.size() > 0) {
            for (String accountName: locationsFromS3.keySet()) {
                ChatmeterAccountLocations accountFromS3 = locationsFromChatmeter.get(accountName);
                accountFromS3.setAuditLocationsDiff(-accountFromS3.getAuditLocationsCount());
                accountFromS3.setBillableLocationsDiff(-accountFromS3.getBillableLocationsCount());
                result.add(accountFromS3);
            }
        }
        return result;
    }
}
