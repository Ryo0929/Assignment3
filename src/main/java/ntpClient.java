import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ntpClient {
    public static void main(String args[]) throws IOException, InterruptedException {
        File fout = new File("offset_and_delay.txt");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        File fout2 = new File("raw.txt");
        FileOutputStream fos2 = new FileOutputStream(fout2);
        BufferedWriter raw = new BufferedWriter(new OutputStreamWriter(fos2));
        raw.write("<burst #, pair #> T1, T2, T3, T4");
        raw.newLine();

        File fout3 = new File("update_values.txt");
        FileOutputStream fos3 = new FileOutputStream(fout3);
        BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(fos3));

        int count=0;
        while(count<15){

            ntpClientImpl client = new ntpClientImpl();
            client.open();
            List<Long> delayValues = new ArrayList<Long>();
            List<Long> offsetValues = new ArrayList<Long>();

            for(int i=0;i<8;i+=1){

                //Public server
                    //InetAddress hostAddr = InetAddress.getByName("tick.mit.edu");
                    //TimeInfo info = client.getTime(hostAddr,123);

                //Cloud server
                byte[] ipAddr = new byte[] { 10, (byte) 182, 0, 3 }; // internal
                //byte[] ipAddr = new byte[] { 34, 125, 117, 102 }; // external

                InetAddress hostAddr = InetAddress.getByAddress(ipAddr);
                TimeInfo info = client.getTime(hostAddr,1023);

                info.computeDetails();
                TimeStamp t1=info.getMessage().getOriginateTimeStamp();
                TimeStamp t2=info.getMessage().getReceiveTimeStamp();
                TimeStamp t3=info.getMessage().getTransmitTimeStamp();
                TimeStamp t4=info.getMessage().getReferenceTimeStamp();
                raw.write("<burst "+count+", pair "+i+"> "+t1.toString()+", "+t2.toString()+", "+t3.toString()+", "+t4.toString());
                raw.newLine();

                Long offsetValue = info.getOffset();
                Long delayValue = info.getDelay();
                String delay = (delayValue == null) ? "N/A" : delayValue.toString();
                String offset = (offsetValue == null) ? "N/A" : offsetValue.toString();

                delayValues.add(delayValue);
                offsetValues.add(offsetValue);

                System.out.println(" Roundtrip delay(ms)=" + delay + ", clock offset(ms)=" + offset); // offset in ms
                bw.write("<"+count+"/"+i+">, "+delay+", "+offset);
                bw.newLine();
            }

            Long updateValueTheta= Collections.min(delayValues);
            int minIndex=delayValues.indexOf(updateValueTheta);
            Long updateValueDelta= offsetValues.get(minIndex);
            String theta = (updateValueTheta == null) ? "N/A" : updateValueTheta.toString();
            String delta = (updateValueDelta == null) ? "N/A" : updateValueDelta.toString();
            System.out.println(" Update values ("+theta+", "+delta+")");
            bw2.write("<"+count+">"+", "+theta+", "+delta);

            client.close();
            count+=1;
            System.out.println("sleep...");
            Thread.sleep(120 * 1000);
        }

        bw.close();
        raw.close();
    }
}
