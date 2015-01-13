package com.proto.client;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.proto.provider.AddressBookProtos;

/**
 * Created by Park on 15. 1. 13..
 */
public class AddressBookServiceImpl implements com.proto.provider.AddressBookService {

    private AddressBookProtos.AddressBook addressBook;

    @Override
    public AddressBookProtos.AddressBook rpcAddressBook() {

        AddressBookProto proto = new AddressBookProto();
        RpcController rpcController = new RpcController() {
            @Override
            public void reset() {

            }

            @Override
            public boolean failed() {
                return false;
            }

            @Override
            public String errorText() {
                return null;
            }

            @Override
            public void startCancel() {

            }

            @Override
            public void setFailed(String reason) {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public void notifyOnCancel(RpcCallback<Object> callback) {

            }
        };

        proto.rpcAddressBook(rpcController, null, new RpcCallback<AddressBookProtos.AddressBook>() {
            @Override
            public void run(AddressBookProtos.AddressBook parameter) {
                addressBook = parameter;
            }
        });

        return addressBook;
    }
}
