package mapx.util;

import java.io.UnsupportedEncodingException;
import org.apache.commons.logging.Log;

public class Test {

	private static Log log = X.getLog();

	public static void testX() {
		String str = "中国";
		try {
			byte[] bytes = str.getBytes("UNICODE");
			for (int i = 0; i < bytes.length; i++) {
				System.out.println(Integer.toHexString(bytes[i] & 0xff));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(StringUtil.unicode(str));
	}

	public static void main(String[] args) {
		String a = "Hello";
		String b = " world";
		String str = a + b + " !";
		System.out.println(str);
	}
}
