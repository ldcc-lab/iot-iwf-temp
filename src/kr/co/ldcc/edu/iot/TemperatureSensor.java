package kr.co.ldcc.edu.iot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import comus.wp.onem2m.iwf.common.M2MException;
import comus.wp.onem2m.iwf.run.IWF;

public class TemperatureSensor {
	private static final Logger LOG = LoggerFactory.getLogger(TemperatureSensor.class);

	
	Runtime rt = Runtime.getRuntime();
	  Process p = null;

	  private IWF vDevice;
	  private String temperature;
	  private String OID = "0001000100010001_sample";                                                 // 디바이스 식별체계

	  private void register() {
	    //
	    try {
	      vDevice = new IWF(OID);                                                   // 1. 환경설정 (OID, conf, log)
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    vDevice.register();                                                         // 2. IWF 등록
	  }

	  private void check() throws InterruptedException {
	    //
	    if (vDevice != null) {
	      while (true) {
	        try {
	          p = rt.exec("python /home/pi/py/dht11_example.py");
	          BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
	          temperature = br.readLine();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        if (temperature != null && !"".equals(temperature)) {
	          vDevice.putContent("temperature", "text/plain", "" + temperature);     // 센서 데이터 업로드
	        } else {
	          System.out.print(".");
	          continue;
	        }
	        System.out.println();
	        Thread.sleep(10000);
	      }
	    } else {
	      System.out.println("등록을 먼저 해주세요.");
	    }
	  }


	public static void main(String[] args) throws Exception {

		TemperatureSensor sensor = new TemperatureSensor();

		sensor.register();
		sensor.check();

	}
}
