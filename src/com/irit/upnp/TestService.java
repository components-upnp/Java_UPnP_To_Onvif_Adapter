package com.irit.upnp;

import org.fourthline.cling.binding.annotations.*;
import org.fourthline.cling.model.types.DoubleDatatype;

/**
 * Created by mkostiuk on 31/05/2017.
 */
@UpnpService(
        serviceId = @UpnpServiceId(value = "TestService"),
        serviceType = @UpnpServiceType(value = "TestService",
        version = 1)
)
public class TestService {

    public TestService() {

    }

    @UpnpStateVariable(datatype = "float", sendEvents = false)
    private Double data = Double.valueOf(222);

    private DoubleDatatype dd = new DoubleDatatype();

    @UpnpAction(out = @UpnpOutputArgument(name = "RandomData"))
    public Double getData() {
        return data;
    }


}
