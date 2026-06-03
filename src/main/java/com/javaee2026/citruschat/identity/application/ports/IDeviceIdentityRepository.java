package com.javaee2026.citruschat.identity.application.ports;

import com.javaee2026.citruschat.identity.domain.model.DeviceIdentity;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;

import java.util.Optional;

public interface IDeviceIdentityRepository {

    void save(DeviceIdentity deviceIdentity);

    Optional<DeviceIdentity> findByDeviceId(DeviceId deviceId);

    boolean existsByDeviceId(DeviceId deviceId);
}