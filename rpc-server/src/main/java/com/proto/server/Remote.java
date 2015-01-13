package com.proto.server;

import com.proto.provider.AddressBookProtos;
import com.proto.provider.AddressBookService;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.httpinvoker.HttpInvokerRequestExecutor;

/**
 * Created by Park on 15. 1. 13..
 */
public class Remote {

    private HttpInvokerRequestExecutor requestExecutor;

    public AddressBookService getRemoteService() {
        HttpInvokerProxyFactoryBean factoryBean = new HttpInvokerProxyFactoryBean();

        String url = "http://localhost:18050/api/rm";

        factoryBean.setServiceUrl(url);
        factoryBean.setServiceInterface(AddressBookService.class);
        factoryBean.setHttpInvokerRequestExecutor(this.requestExecutor);
        factoryBean.afterPropertiesSet();

        return (AddressBookService) factoryBean.getObject();
    }
}
