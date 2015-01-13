package com.proto.server;


import com.proto.provider.AddressBookProtos;

/**
 * Created by Park on 15. 1. 13..
 */
public class Server {

    public static void main(String args[]) {
        Remote remote = new Remote();

        AddressBookProtos.AddressBook addressBook = remote.getRemoteService().rpcAddressBook();

        System.out.println(addressBook.toString());
    }
}
