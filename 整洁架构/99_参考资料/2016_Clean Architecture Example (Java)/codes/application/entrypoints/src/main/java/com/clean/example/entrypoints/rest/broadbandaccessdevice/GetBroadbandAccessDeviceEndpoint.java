package com.clean.example.entrypoints.rest.broadbandaccessdevice;

import com.clean.example.core.entity.BroadbandAccessDevice;
import com.clean.example.core.usecase.broadbandaccessdevice.getdetails.DeviceNotFoundException;
import com.clean.example.core.usecase.broadbandaccessdevice.getdetails.GetBroadbandAccessDeviceDetailsUseCase;
import com.clean.example.entrypoints.rest.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class GetBroadbandAccessDeviceEndpoint {

    public static final String API_PATH = "/broadbandaccessdevice/{hostname}/";

    private static final Logger LOGGER = LoggerFactory.getLogger(GetBroadbandAccessDeviceEndpoint.class);

    private GetBroadbandAccessDeviceDetailsUseCase getBroadbandAccessDeviceDetailsUseCase;

    public GetBroadbandAccessDeviceEndpoint(GetBroadbandAccessDeviceDetailsUseCase getBroadbandAccessDeviceDetailsUseCase) {
        this.getBroadbandAccessDeviceDetailsUseCase = getBroadbandAccessDeviceDetailsUseCase;
    }

    @RequestMapping(value = API_PATH, method = GET)
    public BroadbandAccessDeviceDto getDetails(@PathVariable String hostname) {
        LOGGER.info("Retrieving details of broadband access device: {}", hostname);
        try {
            BroadbandAccessDevice deviceDetails = getBroadbandAccessDeviceDetailsUseCase.getDeviceDetails(hostname);
            return toDto(deviceDetails);
        } catch (DeviceNotFoundException e) {
            LOGGER.info("Broadband access device not found: {}", hostname);
            throw new NotFoundException();
        }
    }

    private BroadbandAccessDeviceDto toDto(BroadbandAccessDevice deviceDetails) {
        return new BroadbandAccessDeviceDto(
                deviceDetails.getExchange().getCode(),
                deviceDetails.getHostname(),
                deviceDetails.getSerialNumber(),
                deviceDetails.getType().name());
    }

}
