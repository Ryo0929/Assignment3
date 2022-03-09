# Assignment3

# NTP client server implementation

An NTP client server implementation using Java. 

## Environment

java 11


## Usage

1. use maven to generate .jar file
2. run jar file

## Network setting

```
//Local client connect to public server
    //InetAddress hostAddr = InetAddress.getByName("tick.mit.edu");
    //TimeInfo info = client.getTime(hostAddr,123);

//Local client connect Cloud server
    
    byte[] ipAddr = new byte[] { 34, 125, 117, 102 };
    InetAddress hostAddr = InetAddress.getByAddress(ipAddr);
    TimeInfo info = client.getTime(hostAddr,1023);

//client and server on the same LAN
    //byte[] ipAddr = new byte[] { 10, (byte) 182, 0, 3 };
    //InetAddress hostAddr = InetAddress.getByAddress(ipAddr);
    //TimeInfo info = client.getTime(hostAddr,1023);
```

## Limitation

All features of this project should works, including:
1. local client to public server
2. local client to cloud server
3. client and server on the same LAN, in our case, we deploy both client and server on different VM on GCP on same LAN.

All data has been plot in Assignment 3 analysis.pdf
