package dev.davivieira.domain.vo;

public class IP {

    private final String address;
    private final Protocol protocol;

    public IP(String address){
        if(address == null)
            throw new IllegalArgumentException("Null IP address");
        this.address = address;
        if(address.length()<=15) {
            this.protocol = Protocol.IPV4;
        } else {
            this.protocol = Protocol.IPV6;
        }
    }

    public static IP fromAddress(String address){
        return new IP(address);
    }

    public String getIPAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "IP{" +
                "address='" + address + '\'' +
                ", protocol=" + protocol +
                '}';
    }
}
