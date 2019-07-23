package com.byf.disruptor.uuid;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

public class KeyUtil {
    public static String generateUUID(){
        TimeBasedGenerator timeBasedGenerator = Generators.timeBasedGenerator(EthernetAddress.constructMulticastAddress());
        return timeBasedGenerator.generate().toString();
    }

    public static void main(String[] args) {
        System.out.println(KeyUtil.generateUUID());
        System.out.println(KeyUtil.generateUUID());
    }
}
