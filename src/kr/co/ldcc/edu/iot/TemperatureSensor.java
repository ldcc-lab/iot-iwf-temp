package kr.co.ldcc.edu.iot;

import java.io.IOException;

import comus.wp.onem2m.iwf.common.M2MException;
import comus.wp.onem2m.iwf.run.IWF;

public class TemperatureSensor {

	private String OID = "0002000100010001_85558625";

	private IWF vDevice;
	private DHT11 sensor = new DHT11();

	private Float temp;

	private void register() throws Exception {
		try {
			vDevice = new IWF(OID);
		} catch (IOException | M2MException e) {
			e.printStackTrace();
			throw new Exception(">> 선언 실패");
		}
		vDevice.register();
	}

	private void check() throws Exception {
		if (vDevice != null) {
			while (true) {
				Thread.sleep(2000);
				if ((temp = sensor.getTemperature(2)) != null)
					vDevice.putContent("temperature", "text/plain", "" + temp);
				else
					System.out.println(">> 페러티 체크 오류");
			}
		} else {
			throw new Exception(">> 등록이 되어 있지 않음");
		}
	}

	public static void main(String[] args) throws Exception {

		TemperatureSensor sensor = new TemperatureSensor();

		sensor.register();
		sensor.check();

	}
}
