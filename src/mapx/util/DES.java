package mapx.util;

import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import mapx.core.LogicException;

/**
 * 可逆的数据算法工具，实现DES加密算法，利用指定的密钥对字符串或字节数组进行加密或解密
 * @author Ready
 * @date 2012-11-30
 */
public class DES {

	private static final BASE64Encoder BASE64_ENCODER = new BASE64Encoder();
	private static final BASE64Decoder BASE64_DECODER = new BASE64Decoder();
	Key key;

	/**
	 * 利用指定的密钥构造一个DES工具实例
	 * @param key
	 */
	public DES(String key) {
		setKey(key);
	}

	/**
	 * 利用默认的密钥构造一个DES工具实例
	 */
	public DES() {
		setKey("ready");
	}

	/**
	 * 根据参数生成KEY
	 */
	public void setKey(String strKey) {
		try {
			KeyGenerator generator = KeyGenerator.getInstance("DES");
			generator.init(new SecureRandom(strKey.getBytes()));
			this.key = generator.generateKey();
			generator = null;
		} catch (Exception e) {
			throw new LogicException("设置指定密钥时发生异常!", e);
		}
	}

	/**
	 * 输入指定的明文，使用指定的密钥进行加密，并返回加密后的字符串
	 * @param plaintext 指定的明文
	 * @param encoding 指定的字符编码，例如“UTF-8”、“GBK”
	 * @return
	 */
	public String encode(String plaintext, String encoding) {
		try {
			byte[] srcBytes = plaintext.getBytes(encoding);
			return BASE64_ENCODER.encode(encode(srcBytes));
		} catch (Exception e) {
			throw new LogicException("对指定的明文字符串进行加密时发生异常! ", e);
		}
	}

	/**
	 * 输入指定的明文，使用指定的密钥进行加密，并返回加密后的字符串<br>
	 * 内部使用UTF-8编码进行处理
	 * @param plaintext 指定的明文
	 * @return
	 */
	public String encode(String plaintext) {
		return encode(plaintext, Encrypter.encoding);
	}

	/**
	 * 解密指定的密文字符串，并以明文方式返回
	 * @param ciphertext 指定的密文字符串
	 * @param encoding 指定的字符编码，例如“UTF-8”、“GBK”
	 * @return
	 */
	public String decode(String ciphertext, String encoding) {
		try {
			byte[] srcBytes = BASE64_DECODER.decodeBuffer(ciphertext);
			return new String(decode(srcBytes), encoding);
		} catch (Exception e) {
			throw new LogicException("解密指定的密文字符串时发生异常!", e);
		}
	}

	/**
	 * 解密指定的密文字符串，并以明文方式返回<br>
	 * 内部使用UTF-8编码进行处理
	 * @param ciphertext 指定的密文字符串
	 * @return
	 */
	public String decode(String ciphertext) {
		return decode(ciphertext, Encrypter.encoding);
	}

	/**
	 * 加密指定的字节数组，并返回加密后的字节数组
	 * @param srcBytes
	 * @return
	 */
	public byte[] encode(byte[] srcBytes) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(srcBytes);
		} catch (Exception e) {
			throw new LogicException("对指定的明文字节数组 进行加密时发生异常!", e);
		} finally {
			cipher = null;
		}
	}

	/**
	 * 解密以byte[]密文输入,以byte[]明文输出
	 * @param srcBytes
	 * @return
	 */
	public byte[] decode(byte[] srcBytes) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(srcBytes);
		} catch (Exception e) {
			throw new LogicException("对指定的密文字节数组进行解密时发生异常!", e);
		} finally {
			cipher = null;
		}
	}
}