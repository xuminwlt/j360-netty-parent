package me.j360.netty.core.connection;

import lombok.ToString;

import javax.crypto.Cipher;

/**
 * @author: min_xu
 * @date: 2017/12/27 下午5:17
 * 说明：
 */

@ToString
public final class SessionContext {

    public String userId;
    private String deviceId;
    public int heartbeat = 10000;//10s
    public Cipher cipher;
    private byte clientType;

    public void changeCipher(Cipher cipher) {
        this.cipher = cipher;
    }


    public SessionContext setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public SessionContext setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
    }

    public boolean handshakeOk() {
        return deviceId != null && deviceId.length() > 0;
    }


    public boolean isSecurity() {
        return cipher != null;
    }
}
