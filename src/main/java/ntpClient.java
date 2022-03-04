import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ntpClient {
    public static void main(String args[]) throws IOException, InterruptedException {

        //while(true){
        //    Thread.sleep(10 * 1000);
            ntpClientImpl client = new ntpClientImpl();
            client.open();
            List<Long> delayValues = new ArrayList<Long>();
            List<Long> offsetValues = new ArrayList<Long>();


            for(int i=0;i<8;i+=1){
//                InetAddress hostAddr = InetAddress.getByName("tick.mit.edu");
                InetAddress hostAddr = InetAddress.getLocalHost();

                TimeInfo info = client.getTime(hostAddr,1023);
                info.computeDetails(); // compute offset/delay if not already done

                TimeStamp t1=info.getMessage().getOriginateTimeStamp();
                TimeStamp t2=info.getMessage().getReceiveTimeStamp();
                TimeStamp t3=info.getMessage().getTransmitTimeStamp();
                TimeStamp t4=info.getMessage().getReferenceTimeStamp();

                Long offsetValue = info.getOffset();
                Long delayValue = info.getDelay();
                String delay = (delayValue == null) ? "N/A" : delayValue.toString();
                String offset = (offsetValue == null) ? "N/A" : offsetValue.toString();

                delayValues.add(delayValue);
                offsetValues.add(offsetValue);

                System.out.println(" Roundtrip delay(ms)=" + delay
                        + ", clock offset(ms)=" + offset); // offset in ms
            }

            Long updateValueTheta= Collections.min(delayValues);
            int minIndex=delayValues.indexOf(updateValueTheta);
            Long updateValueDelta= offsetValues.get(minIndex);
            String theta = (updateValueTheta == null) ? "N/A" : updateValueTheta.toString();
            String delta = (updateValueDelta == null) ? "N/A" : updateValueDelta.toString();
            System.out.println(" Update values ("+theta+", "+delta+")");

            client.close();
        //}
    }
}
