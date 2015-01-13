package com.proto.client;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.proto.provider.AddressBookProtos;

/**
 * Created by Park on 15. 1. 13..
 */
public class AddressBookProto extends AddressBookProtos.AddressBookServiceProto {

    @Override
    public void rpcAddressBook(RpcController controller, AddressBookProtos.NamePattern request, RpcCallback<AddressBookProtos.AddressBook> done) {
        AddressBookProtos.Person.Builder person = AddressBookProtos.Person.newBuilder();

        person.setId(1);
        person.setName("Edard Park");
        person.setEmail("hyokun31@gmail.com");

        AddressBookProtos.Person.PhoneNumber.Builder phoneNumber = AddressBookProtos.Person.PhoneNumber.newBuilder();

        phoneNumber.setNumber("010-1234-5678");
        phoneNumber.setType(AddressBookProtos.Person.PhoneType.MOBILE);

        person.addPhone(phoneNumber);

        AddressBookProtos.AddressBook.Builder addressBook = AddressBookProtos.AddressBook.newBuilder();

        addressBook.addPerson(person);

        done.run(addressBook.build());
    }
}
